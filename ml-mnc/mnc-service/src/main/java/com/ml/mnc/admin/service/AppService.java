package com.ml.mnc.admin.service;

import com.ml.base.service.BaseService;
import com.ml.mnc.admin.domain.AppVO;

/**
 * <pre>
 * 应用信息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:07:13
 */
public interface AppService extends BaseService<Long, AppVO> {
	/**
	 * <pre>
	 * 消息队列
	 * </pre>
	 * 
	 * @return SendUrlService
	 */
	MessageQueueService messageQueueService();

	/**
	 * <pre>
	 * 推送URL
	 * </pre>
	 * 
	 * @return SendUrlService
	 */
	SendUrlService sendUrlService();

	/**
	 * <pre>
	 * 根据应用编码取应用
	 * 
	 * 描述
	 * 	1、优先从缓存中取，未取从DB取
	 * 	2、应用编码存储用小写
	 * </pre>
	 * 
	 * @param appCode String
	 * @return AppVO
	 */
	AppVO getApp(String appCode);
}
