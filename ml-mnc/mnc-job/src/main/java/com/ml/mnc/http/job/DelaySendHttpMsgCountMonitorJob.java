package com.ml.mnc.http.job;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;
import com.ml.mnc.admin.rpc.DingdingMsgProducer;
import com.ml.mnc.http.HttpMsgSender;
import com.ml.mnc.http.domain.HttpMsgParams;
import com.ml.mnc.http.service.HttpMsgService;

/**
 * <pre>
 * 延时推送http消息数监控
 * 
 * </pre>
 * @author mecarlen.Wang 2019年12月18日 下午3:55:16
 */
@Slf4j
@JobHandler("delaySendHttpMsgCountMonitorJob")
@Component
public class DelaySendHttpMsgCountMonitorJob extends IJobHandler {
	@Resource
	private HttpMsgService httpMsgService;
	@Resource
	private HttpMsgSender httpMsgSender;
	@Resource
    private DingdingMsgProducer dingdingMsgProducer;
	@Value("${usc.http.msg-overstock.max-delay-count}")
	private Integer maxDelayOverstockCount;
	@Value("${spring.profiles.active}")
    private String env;
	@Override
	public ReturnT<String> execute(String jobParams) throws Exception {
		final long currTimeMills = System.currentTimeMillis();
		log.info("delaySendHttpMsgCount monitor currTimeMills:{},params:{}", currTimeMills, jobParams);
		MonitorParams mtparams = JSON.parseObject(jobParams, MonitorParams.class);
		HttpMsgParams params = JSON.parseObject(jobParams, HttpMsgParams.class);
		if(null == params.getCurrentSendTimeMills()) {
			//默认取当前时间
			params.setCurrentSendTimeMills(currTimeMills);
		}
		XxlJobLogger.log("delaySendHttpMsgCount monitor begin");
		log.info("delaySendHttpMsgCount monitor begin,params:{}", jobParams);
		int overstockCount = httpMsgService.findDelaySendMsgCount(params);
		if (overstockCount > (null != mtparams.getMaxOverstockCount() ? mtparams.getMaxOverstockCount()
				: maxDelayOverstockCount)) {
			String title = "%s:http延时推送消息积压量监控";
			String content = "http延时推送出现消息积压 %s \r\n 当前24小时处理初始化的消息数:%d";
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
					String.format(content, vccInfo.toString(), overstockCount));
		}
		log.info("delaySendHttpMsgCount monitor over,params:{}", jobParams);
		XxlJobLogger.log("delaySendHttpMsgCount monitor over");
		return SUCCESS;
	}
	
}
