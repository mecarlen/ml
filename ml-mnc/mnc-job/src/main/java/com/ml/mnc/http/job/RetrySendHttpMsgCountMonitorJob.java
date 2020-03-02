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
import com.ml.mnc.http.domain.HttpMsgParams;
import com.ml.mnc.http.service.HttpMsgService;

/**
 * <pre>
 * 待重推http消息数监控任务
 * 
 * </pre>
 * @author mecarlen.Wang 2019年11月26日 下午6:22:15
 */
@Slf4j
@JobHandler("retrySendHttpMsgCountMonitorJob")
@Component
public class RetrySendHttpMsgCountMonitorJob extends IJobHandler {
	@Resource
	private HttpMsgService httpMsgService;
	@Value("${usc.http.msg-overstock.max-retry-count}")
	private Integer maxRetryOverstockCount;
	@Resource
    private DingdingMsgProducer dingdingMsgProducer;
	@Value("${spring.profiles.active}")
    private String env;
	
	@Override
	public ReturnT<String> execute(String jobParams) throws Exception {
		final long currTimeMills = System.currentTimeMillis();
		log.info("retrySendHttpMsgCount monitor currTimeMills:{},params:{}", currTimeMills, jobParams);
		MonitorParams mtparams = JSON.parseObject(jobParams, MonitorParams.class);
		HttpMsgParams params = JSON.parseObject(jobParams, HttpMsgParams.class);
		if(null == params.getCurrentSendTimeMills()) {
			//默认取当前时间
			params.setCurrentSendTimeMills(currTimeMills);
		}
		XxlJobLogger.log("retrySendHttpMsgCount monitor monitor begin");
		log.info("retrySendHttpMsgCount monitor begin,params:{}", jobParams);
		int overstockCount = httpMsgService.findRetrySendMsgCount(params);
		if (overstockCount > (null != mtparams.getMaxOverstockCount() ? mtparams.getMaxOverstockCount()
				: maxRetryOverstockCount)) {
			String title = "%s:http重推消息积压量监控";
			String content = "http重推出现消息积压 %s \r\n 当前24小时内处于推送中的消息数:%d";
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
		log.info("retrySendHttpMsgCount monitor over,params:{}", jobParams);
		XxlJobLogger.log("retrySendHttpMsgCount monitor monitor over");
		return SUCCESS;
	}
}
