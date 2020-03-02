package com.ml.mnc.http.job;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;
import com.ml.mnc.admin.rpc.DingdingMsgProducer;
import com.ml.mnc.http.domain.HttpMsgParams;
import com.ml.mnc.http.service.HttpMsgService;

/**
 * <pre>
 * http消息推送失败数监控任务
 * 
 * </pre>
 * @author mecarlen.Wang 2019年11月26日 下午6:23:26
 */
@Slf4j
@JobHandler("sendFailureHttpMsgCountMonitorJob")
@Component
public class SendFailureHttpMsgCountMonitorJob extends IJobHandler  {
	@Resource
	private HttpMsgService httpMsgService;
	@Value("${usc.http.msg-overstock.max-fail-count}")
	private Integer maxFailureCount;
	@Resource
    private DingdingMsgProducer dingdingMsgProducer;
	@Value("${spring.profiles.active}")
    private String env;
	
	@Override
	public ReturnT<String> execute(String jobParams) throws Exception {
		final long currTimeMills = System.currentTimeMillis();
		log.info("currTimeMills:{},params:{}", currTimeMills, jobParams);
		MonitorParams mtparams = JSON.parseObject(jobParams, MonitorParams.class);
		HttpMsgParams params = JSON.parseObject(jobParams, HttpMsgParams.class);
		if(null == params.getCurrentSendTimeMills()) {
			//默认取当前时间
			params.setCurrentSendTimeMills(currTimeMills);
		}
		int failureCount = httpMsgService.findSendFailureMsgCount(params);
		if (failureCount > (null != mtparams.getMaxOverstockCount() ? mtparams.getMaxOverstockCount()
				: maxFailureCount)) {
			String title = "%s:http推送失败消息量监控";
			String content = "http推送失败消息量提醒 %s \r\n 当前24小时内推送失败的消息数:%d";
			StringBuilder vccInfo = new StringBuilder("\r\n vccId:");
			if (null != params.getVccId()) {
				vccInfo.append(params.getVccId());
			} else if (null != params.getVccIds() && !params.getVccIds().isEmpty()) {
				vccInfo.append("[");
				StringBuilder vccIds = new StringBuilder("");
				params.getVccIds().forEach(vccId -> {
					if ("".equals(vccIds.toString())) {
						vccIds.append(vccId).append(",");
					} else {
						vccIds.append(vccId);
					}
				});
				vccInfo.append(vccIds.toString()).append("]");
			} else {
				vccInfo.append("所有");
			}

			if (null != params.getExcludeVccIds() && !params.getExcludeVccIds().isEmpty()) {
				vccInfo.append(" \r\n excludeVccIds:[");
				StringBuilder vccIds = new StringBuilder("");
				params.getVccIds().forEach(vccId -> {
					if ("".equals(vccIds.toString())) {
						vccIds.append(vccId).append(",");
					} else {
						vccIds.append(vccId);
					}
				});
				vccInfo.append(vccIds.toString()).append("]");
			}

			dingdingMsgProducer.send(String.format(title, env),
					String.format(content, vccInfo.toString(), failureCount));
		}
		return SUCCESS;
	}
}
