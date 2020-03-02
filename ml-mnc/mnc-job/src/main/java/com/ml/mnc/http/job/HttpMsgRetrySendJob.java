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
import com.xxl.job.core.util.ShardingUtil;
import com.xxl.job.core.util.ShardingUtil.ShardingVO;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * http重试消息推送
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月16日 下午3:00:00
 */
@Slf4j
@JobHandler("httpMsgRetrySendJob")
@Component
public class HttpMsgRetrySendJob extends IJobHandler {
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
		log.info("httpMsgRetrySend currTimeMills:{},params:{}", currTimeMills, jobParams);
		PageParams<?> pageParams = JSON.parseObject(jobParams, PageParams.class);
		HttpMsgParams params = JSON.parseObject(jobParams, HttpMsgParams.class);
		if(null == params.getCurrentSendTimeMills()) {
			//默认取当前时间
			params.setCurrentSendTimeMills(currTimeMills);
		}
		// 分片参数
		ShardingVO shardingParams = ShardingUtil.getShardingVo();
		if (null != shardingParams && shardingParams.getTotal() > 1) {
			params.setShardIdx(shardingParams.getIndex());
			params.setShards(shardingParams.getTotal());
			log.info("httpMsgRetrySend shardingParams:{}", JSON.toJSONString(shardingParams));
		}
		XxlJobLogger.log("httpMsgRetrySend begin");
		log.info("httpMsgRetrySend begin,params:{}", jobParams);
		// 设置分页
		PageThreadLocal.setParams(pageParams);
		//排除目标URL
		List<String> excludeTargetUrls = Lists.newArrayList();
		cacheUtils.hGetAll(SendUrl.URL_SEND_FAILURE_RECORD_CACHEKEY).entrySet().forEach(entry->{
			if (Integer.valueOf(entry.getValue()) > threshold) {
				excludeTargetUrls.add(entry.getKey());
			}
		});
		if(!excludeTargetUrls.isEmpty()) {
			params.setExcludeTargetUrls(excludeTargetUrls);
		}
		// 查询消息列表
		PageModel<HttpMsgVO> msglist = httpMsgService.findRetrySendMsg(params);
		msglist.getData().forEach(msg -> httpMsgSender.send(msg));
		log.info("httpMsgRetrySend over,params:{}", jobParams);
		XxlJobLogger.log("httpMsgRetrySend over");
		return SUCCESS;
	}

}
