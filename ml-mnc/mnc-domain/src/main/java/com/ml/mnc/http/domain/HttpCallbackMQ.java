package com.ml.mnc.http.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * http回调消息体
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午3:58:22
 */
@Setter
@Getter
public class HttpCallbackMQ {
	private String appCode;
	private Long vccId;
	private String businessId;
	private String businessType;
	private Long sendBeginTimeMills;
	private Long sendEndTimeMills;
	private Integer sendStatus;
	private Integer sendCount;
	private String sender;
	private Long nextSendTimeMills;
	private String returnContent;
	private String errorMsg;
}
