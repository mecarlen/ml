package com.ml.mnc.http.job;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.ml.base.query.PageModel;
import com.ml.base.query.PageParams;
import com.ml.base.query.PageThreadLocal;
import com.ml.mnc.http.HttpMsgSender;
import com.ml.mnc.http.domain.HttpMsgParams;
import com.ml.mnc.http.domain.HttpMsgVO;
import com.ml.mnc.http.service.HttpMsgService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import com.xxl.job.core.util.ShardingUtil.ShardingVO;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * http推送失败重试任务
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年11月26日 下午6:18:54
 */
@Slf4j
@JobHandler("sendFailureHttpMsgRetryJob")
@Component
public class SendFailureHttpMsgRetryJob extends IJobHandler {
	@Resource
	private HttpMsgService httpMsgService;
	@Resource
	private HttpMsgSender httpMsgSender;

	@Override
	public ReturnT<String> execute(String jobParams) throws Exception {
		final long currTimeMills = System.currentTimeMillis();
		log.info("currTimeMills:{},params:{}", currTimeMills, jobParams);
		PageParams<?> pageParams = JSON.parseObject(jobParams, PageParams.class);
		HttpMsgParams params = JSON.parseObject(jobParams, HttpMsgParams.class);
		if (null == params.getCurrentSendTimeMills()) {
			// 默认取当前时间
			params.setCurrentSendTimeMills(currTimeMills);
		}
		// 分片参数
		ShardingVO shardingParams = ShardingUtil.getShardingVo();
		if (null != shardingParams && shardingParams.getTotal() > 1) {
			params.setShardIdx(shardingParams.getIndex());
			params.setShards(shardingParams.getTotal());
			log.info("shardingParams:{}", JSON.toJSONString(shardingParams));
		}
		XxlJobLogger.log("httpMsgSend begin");
		log.info("httpMsgSend begin,params:{}", jobParams);
		// 设置分页
		PageThreadLocal.setParams(pageParams);
		// 查询任务列表
		PageModel<HttpMsgVO> msglist = httpMsgService.findSendFailureMsg(params);
		Set<String> exceptionUrlSet = Sets.newHashSet();
		msglist.getData().forEach(msg -> {
			String url = msg.getSendUrl().split("\\?")[0];
			if(httpMsgSender.isCloseImmediatelySend(msg)&&!exceptionUrlSet.contains(url)) {
				httpMsgSender.send(msg);
				exceptionUrlSet.add(url);
			} else if(!httpMsgSender.isCloseImmediatelySend(msg)) {
				httpMsgSender.send(msg);
			}
		});
		log.info("httpMsgSend over,params:{}", jobParams);
		XxlJobLogger.log("httpMsgSend over");
		return SUCCESS;
	}

}
