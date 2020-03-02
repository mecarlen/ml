package com.ml.mnc.admin;

import com.ml.base.domain.Entity;

/**
 * <pre>
 * 应用信息
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月4日 下午5:28:08
 */
public interface App extends Entity<Long>{
	/** 应用缓存 key */
	final public static String APP_CACHE_KEY="mnc:adm:app";
	/** 应用所属产品线编码 */
	String getPlCode();
	/** 应用所属产品线名称 */
	String getPlName();
	/** 应用编码 */
	String getAppCode();
	/** 应用名称 */
	String getAppName();
	/** 应用负责人ERP */
	String getOwnerErp();
	/** 应用负责人名称 */
	String getOwnerName();
	/** 应用告警钉钉 */
	String getDingding();
	/** 应用成员告警手机号列表 */
	String getMemberMobiles();
	/** 应用成员告警邮箱列表 */
	String getMemberEmails();
	/** 应用告警状态 */
	Integer getAlarmStatus();
	/** 是否结果回传 */
	Boolean getNeedCallback();
	/** 备注 */
	String getRemark();
	
	
}
