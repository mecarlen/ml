package com.ml.mnc.http.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ml.base.query.PageModel;
import com.ml.base.query.PageParams;
import com.ml.base.query.PageThreadLocal;
import com.ml.mnc.admin.SendUrl;
import com.ml.mnc.http.HttpMsgSender;
import com.ml.mnc.http.domain.HttpMsgParams;
import com.ml.mnc.http.domain.HttpMsgVO;
import com.ml.mnc.http.service.HttpMsgService;
import com.ml.util.cache.CacheUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 异常企业URL重试
 * 
 * </pre>
 * @author mecarlen.Wang 2019年12月17日 下午5:46:08
 */
@Slf4j
@JobHandler("exceptionURLMsgRetryJob")
@Component
public class ExceptionURLMsgRetryJob extends IJobHandler {
	@Resource
	private HttpMsgService httpMsgService;
	@Resource
	private HttpMsgSender httpMsgSender;
	@Resource
	private CacheUtils cacheUtils;
	@Value("${rabbitmq.notification.url-threshold}")
	private Integer threshold;
	
	@Override
	public ReturnT<String> execute(String jobParams) throws Exception {
		final long currTimeMills = System.currentTimeMillis();
		log.info("currTimeMills:{},params:{}", currTimeMills, jobParams);
		PageParams<?> pageParams = JSON.parseObject(jobParams, PageParams.class);
		HttpMsgParams params = JSON.parseObject(jobParams, HttpMsgParams.class);
		if(null == params.getCurrentSendTimeMills()) {
			//默认取当前时间
			params.setCurrentSendTimeMills(currTimeMills);
		}
		XxlJobLogger.log("ExceptionURLMsg begin");
		log.info("ExceptionURLMsg begin,params:{}", jobParams);
		// 设置分页
		PageThreadLocal.setParams(pageParams);
		//目标URL
		List<String> targetUrls = Lists.newArrayList();
		cacheUtils.hGetAll(SendUrl.URL_SEND_FAILURE_RECORD_CACHEKEY).forEach((url,failCount)->{
			if (Integer.valueOf(failCount) > threshold) {
				targetUrls.add(url);
			}
		});
		if(!targetUrls.isEmpty()) {
			for(String targetUrl:targetUrls) {
				params.setTargetUrls(Lists.newArrayList(targetUrl));
				// 查询消息列表
				PageModel<HttpMsgVO> page = httpMsgService.findRetrySendMsg(params);
				List<HttpMsgVO> msglist = page.getData();
				for(HttpMsgVO msg:msglist) {
					httpMsgSender.send(msg);
				}
			}
		}
		log.info("ExceptionURLMsg over,params:{}", jobParams);
		XxlJobLogger.log("ExceptionURLMsg over");
		return SUCCESS;
	}

}
