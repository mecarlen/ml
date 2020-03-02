package com.ml.mnc.admin.rpc;

import com.ml.base.controller.WebResponse;

/**
 * <pre>
 * 消息队列
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年11月5日 上午11:37:32
 */
public interface MsgQueueRpcService {
	/**
	 * <pre>
	 * 扩展队列
	 * 
	 * 描述
	 * 1、仅支持已有exchange
	 * 
	 * </pre>
	 * 
	 * @param exchangeName String
	 * @param queueName    String
	 * @param routeKey     String
	 * @return WebResponse<Boolean>
	 */
	WebResponse<Boolean> extendQueue(String exchangeName, String queueName, String routeKey);

	/**
	 * <pre>
	 * 扩展消息数
	 * 
	 * </pre>
	 * 
	 * @param appCode             String
	 * @param queueName           String
	 * @param concurrentConsumers int
	 * @param prefetchCount       int
	 * @return WebResponse<Integer> 返回成功执行节点数
	 */
	WebResponse<Integer> extendConsumer(String appCode, String queueName, int concurrentConsumers, int prefetchCount);
}
