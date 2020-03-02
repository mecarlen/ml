package com.ml.mnc.admin.service;

import com.ml.base.service.BaseService;
import com.ml.mnc.admin.domain.SendUrlVO;

/**
 * <pre>
 * 推送URL
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月25日 下午4:11:48
 */
public interface SendUrlService extends BaseService<Long, SendUrlVO> {
	/**
	 * <pre>
	 * 认证续签
	 * 
	 * </pre>
	 * 
	 * @return AuthRenewalService
	 */
	AuthRenewalService authRenewalService();

	/**
	 * <pre>
	 * 取推送URL配置信息
	 * 
	 * 描述
	 * 优先取缓存，再取DB
	 * </pre>
	 * 
	 * @param appCode    String
	 * @param queueName  String
	 * @param routeKey   String
	 * @param urlAddress String
	 * @return SendUrlVO
	 */
	SendUrlVO getSendUrl(String appCode, String queueName, String routeKey, String urlAddress);
}
