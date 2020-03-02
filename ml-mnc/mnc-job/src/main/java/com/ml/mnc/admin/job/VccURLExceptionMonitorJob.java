package com.ml.mnc.admin.job;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ml.mnc.admin.SendUrl;
import com.ml.mnc.admin.rpc.DingdingMsgProducer;
import com.ml.util.cache.CacheUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * com.ml.mnc.admin.job
 *
 * @author yansongda <me@yansongda.cn>
 * @version 2019/11/7 下午1:45
 */
@Slf4j
@Component
@JobHandler("vccURLExceptionMonitorJob")
public class VccURLExceptionMonitorJob extends IJobHandler {

	@Resource
	private CacheUtils cacheUtils;

	@Value("${rabbitmq.notification.url-threshold}")
	private Integer threshold;

	@Resource
	private DingdingMsgProducer dingdingMsgProducer;

	@Value("${spring.profiles.active}")
	private String env;

	@Override
	public ReturnT<String> execute(String s) {
		cacheUtils.hGetAll(SendUrl.URL_SEND_FAILURE_RECORD_CACHEKEY).entrySet().forEach(entry -> {
			long currentTimes = Long.parseLong(entry.getValue());
			if (currentTimes > threshold) {
				log.warn("failed times of url:{} greater threshold. current: {}; threshold: {}",entry.getKey(), currentTimes, threshold);

				String content = "key: %s\r\n当前值: %s\r\n阙值: %s";
				dingdingMsgProducer.send(env + ": 第三方 API 请求失败通知",
						String.format(content, entry.getKey(), currentTimes, threshold));
			}
		});

		return ReturnT.SUCCESS;
	}
}
