package com.ml.mnc.admin.rpc;

import com.ml.base.controller.WebResponse;

/**
 * <pre>
 * http消息队列
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年11月5日 上午10:10:29
 */
public interface HttpMsgQueueRpcService {
	/**
	 * <pre>
	 * 扩展已有队列消费者
	 * 
	 * 描述
	 * 1、仅支持已有队列
	 * 2、仅支持手动签收
	 * </pre>
	 * 
	 * @param queueName           String
	 * @param concurrentConsumers int
	 * @param prefetchCount       int
	 * @return WebResponse<Boolean>
	 */
	WebResponse<Boolean> extendConsumer(String queueName, int concurrentConsumers, int prefetchCount);

}
