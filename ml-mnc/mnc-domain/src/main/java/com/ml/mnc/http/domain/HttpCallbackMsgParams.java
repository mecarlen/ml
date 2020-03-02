package com.ml.mnc.http.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

/**
 * <pre>
 * http回调查询参数
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月25日 下午6:25:32
 */
@Setter
@Getter
@Builder
public class HttpCallbackMsgParams {
	private String plCode;
	private String appCode;
	private String queueName;
	private String routeKey;
	private Long vccId;
	private String businessType;
	private Integer maxRetryCount;
	private Integer shards;
	private Integer shardIdx;

	@Tolerate
	public HttpCallbackMsgParams() {
	}
}
