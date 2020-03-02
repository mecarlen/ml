package com.ml.mnc.admin;

import com.ml.base.domain.Entity;

/**
 * <pre>
 * 推送URL
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月20日 上午11:39:49
 */
public interface SendUrl extends Entity<Long> {
	/** 推送URL缓存key格式 mnc:adm:{appCode}:{queueName}:{routeKey}:sendurl */
	String SENDURL_CACHEKEY_FORMAT=" mnc:adm:%s:%s:%s:sendurl";

	/**
	 * url推送失败记录缓存 key
	 *
	 * 格式：mnc:adm:url:fail-count
	 *
	 * redis 存储类型： String
	 */
	String URL_SEND_FAILURE_RECORD_CACHEKEY = "mnc:adm:url:fail-count";

	/** 所属应用 */
	App getApp();
	/** 所属队列 */
	MessageQueue getMessageQueue();
	/** 企业ID */
	Long getVccId();
	/** 业务类型 */
	String getBusinessType();
	/** 推送URL地址 */
	String getUrlAddress();
	/** 成功判断字段 */
	String getSuccessFieldCode();
	/** 认证字段 */
	String getAuthField();
	/** 认证方式 */
	Integer getAuthMethod();
	/** 认证续签信息 */
	AuthRenewal getAuthRenewal();
	/** 备注 */
	String getRemark();
}
