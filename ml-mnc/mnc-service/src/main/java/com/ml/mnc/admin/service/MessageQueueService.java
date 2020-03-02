package com.ml.mnc.admin.service;

import java.util.List;

import com.ml.base.service.BaseService;
import com.ml.mnc.admin.domain.MessageQueueVO;

/**
 * <pre>
 * 消息队列信息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:08:09
 */
public interface MessageQueueService extends BaseService<Long, MessageQueueVO> {
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
	 * 取队列信息
	 * 
	 * 描述：
	 *   1、优先从缓存取，未取到再读数据库
	 *   2、存储时应用编码都用小写
	 * </pre>
	 * 
	 * @param appCode   String 应用编码
	 * @param queueName String 队列名称
	 * @param routeKey  String 路由key
	 * @return MessageQueueVO
	 */
	MessageQueueVO getMessageQueue(String appCode, String queueName, String routeKey);
	
	/**
	 * <pre>
	 * 查询MNC所有在用http消息队列
	 * 
	 * </pre>
	 * 
	 * List<MessageQueueVO>
	 * */
	List<MessageQueueVO> findNormalHttpMsgQueues();
}
