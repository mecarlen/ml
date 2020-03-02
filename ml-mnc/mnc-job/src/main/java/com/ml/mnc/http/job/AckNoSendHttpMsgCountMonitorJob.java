package com.ml.mnc.http.job;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ml.mnc.admin.rpc.DingdingMsgProducer;
import com.ml.mnc.http.HttpMsg;
import com.ml.util.cache.CacheUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 签收未推送的http消息积压量监控
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年12月19日 上午10:34:35
 */

@Slf4j
@JobHandler("ackNoSendHttpMsgCountMonitorJob")
@Component
public class AckNoSendHttpMsgCountMonitorJob extends IJobHandler {
	@Resource
	private CacheUtils cacheUtils;
	@Resource
	private DingdingMsgProducer dingdingMsgProducer;
	@Value("${usc.http.msg-overstock.max-acknosend-count}")
	private Integer maxAckNoSendOverstockCount;
	@Value("${spring.profiles.active}")
	private String env;

	@Override
	public ReturnT<String> execute(String jobParams) throws Exception {
		final long currTimeMills = System.currentTimeMillis();
		log.info("ackNoSendHttpMsgCount monitor currTimeMills:{},params:{}", currTimeMills, jobParams);
		MonitorParams mtparams = JSON.parseObject(jobParams, MonitorParams.class);
		XxlJobLogger.log("ackNoSendHttpMsgCount monitor begin");
		log.info("ackNoSendHttpMsgCount monitor begin,params:{}", jobParams);
		long overstockCount = cacheUtils.hLen(HttpMsg.HTTP_MSG_CACHE_KEY);
		if (overstockCount > (null != mtparams.getMaxOverstockCount() ? mtparams.getMaxOverstockCount()
				: maxAckNoSendOverstockCount)) {
			String title = "%s:http签收未推送的消息积压量监控";
			String content = "http签收未推送的消息出现积压 \r\n 当前消息数:%d";
			dingdingMsgProducer.send(String.format(title, env), String.format(content, overstockCount));
		}
		log.info("ackNoSendHttpMsgCount monitor over,params:{}", jobParams);
		XxlJobLogger.log("ackNoSendHttpMsgCount monitor over");
		return SUCCESS;
	}

}
