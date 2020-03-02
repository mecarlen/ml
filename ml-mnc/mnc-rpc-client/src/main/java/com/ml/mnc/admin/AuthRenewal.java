package com.ml.mnc.admin;

import com.ml.base.domain.Entity;

/**
 * <pre>
 * 认证续签
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 上午9:43:53
 */
public interface AuthRenewal extends Entity<Long>{
	/** 推送URL */
	SendUrl getSendUrl();
	/** 所属应用编码 */
	String getAppCode();
	/** 所属队列 */
	String getQueueName();
	/** 所属路由 */
	String getRouteKey();
	/** 企业ID */
	Long getVccId();
	/** 业务类型 */
	String getBusinessType();
	/** 推送URL地址，不含参数 */
	String getSendUrlAddress();
	/** 续签URL地址 */
	String getRenewalUrlAddress();
	/** 授权信息字段 */
	String getAuthDataField();
	/** 备注 */
	String getRemark();
}
