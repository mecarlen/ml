package com.ml.mnc.dingding.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import com.ml.mnc.Message.IntervalType;

/**
 * <pre>
 * 钉钉查询参数
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月8日 上午9:50:13
 */
@Setter
@Getter
@Builder
public class DingdingMsgParams {
	private String plCode;
	private String appCode;
	private String queueName;
	private String routeKey;
	private Long vccId;
	private String businessType;
	private Integer maxRetryCount;
	/** 当前推送时间 - 消息推送最大时间窗口为8小时 */
	private Long beforeSendTimeMills;
	private Long currentSendTimeMills;
	private Integer shards;
	private Integer shardIdx;

	public void setCurrentSendTimeMills(Long currentSendTimeMills) {
		this.currentSendTimeMills = currentSendTimeMills;
		this.beforeSendTimeMills = this.currentSendTimeMills - IntervalType.HOUR.mills() * 8;
	}

	@Tolerate
	public DingdingMsgParams() {
	}
}
