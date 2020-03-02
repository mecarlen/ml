package com.ml.mnc.dingding.rpc;

import com.ml.base.rpc.BaseDTO;
import com.ml.mnc.admin.rpc.AppDTO;
import com.ml.mnc.dingding.DingdingMsg;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

/**
 * <pre>
 * 钉钉消息
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月9日 上午10:25:07
 */
@Setter
@Getter
@Builder
public class DingdingMsgDTO extends BaseDTO<Long> implements DingdingMsg{
	private AppDTO app;
	private String plCode;
	private String appCode;
	private String queueName;
	private String routeKey;
	private Long vccId;
	private String businessId;
	private String businessType;
	private Long businessAcceptTimeMills;
	private String msgTitle;
	private String msgContent;
	private String dingdingUrl;
	private Long specifySendTimeMills;
	private String retryInterval;
	private Long acceptTimeMills;
	private Long firstSendTimeMills;
	private Long nextSendTimeMills;
	private Long firstSendSuccessTimeMills;
	private Integer retryCount;
	private Integer sendStatus;
	
	@Tolerate
	public DingdingMsgDTO() {}
}
