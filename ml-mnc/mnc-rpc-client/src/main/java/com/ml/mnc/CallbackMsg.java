package com.ml.mnc;

import com.ml.mnc.admin.App;

/**
 * <pre>
 * 回调消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午2:32:02
 */
public interface CallbackMsg {
	/** 所属应用 */
	App getApp();

	/** 所属产品线编码 */
	String getPlCode();

	/** 所属应用编码 */
	String getAppCode();

	/** 消息队列 */
	String getQueueName();

	/** 消息路由 */
	String getRouteKey();

	/** 业务类型 */
	String getBusinessType();

	/** 所属企业 */
	Long getVccId();

	/** 业务id */
	String getBusinessId();

	/** 推送状态 */
	Integer getSendStatus();

	/** 重试次数 */
	Integer getRetryCount();

	/** 接口返回json */
	String getReturnJson();

	/** 消息正文 */
	String getMsgContent();
}
