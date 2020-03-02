package com.ml.mnc.http.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import com.ml.mnc.Message.IntervalType;

/**
 * <pre>
 * http查询参数
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午1:51:09
 */
@Setter
@Getter
public class HttpMsgParams {
	private String plCode;
	private String appCode;
	private String queueName;
	private String routeKey;
	private Long vccId;
	private String businessType;
	private Integer maxRetryCount;
	/** 当前推送时间 - 消息推送最大时间窗口为24小时 */
	private Long beforeSendTimeMills;
	private Long currentSendTimeMills;
	private Integer shards;
	private Integer shardIdx;
	/** 多个企业id */
	private List<Long> vccIds;
	/** 例外企业id */
	private List<Long> excludeVccIds;
	/** 目标URL */
	private List<String> targetUrls;
	/** 例外URL */
	private List<String> excludeTargetUrls;
	
	public void setCurrentSendTimeMills(Long currentSendTimeMills) {
		this.currentSendTimeMills = currentSendTimeMills;
		this.beforeSendTimeMills = this.currentSendTimeMills - IntervalType.HOUR.mills() * 24;
	}

	@Tolerate
	public HttpMsgParams() {
	}
}
