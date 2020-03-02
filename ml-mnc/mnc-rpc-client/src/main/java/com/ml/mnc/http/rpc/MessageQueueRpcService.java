package com.ml.mnc.http.rpc;

import java.util.List;

import com.ml.mnc.admin.MessageQueue;

/**
 * <pre>
 * 消息队列
 * 
 * </pre>
 * @author mecarlen.Wang 2019年11月27日 下午5:06:32
 */
public interface MessageQueueRpcService {
	/**
	 * <pre>
	 * 所有在用的http消息队列
	 * 
	 * </pre>
	 * @param 
	 * @return List<MessageQueue>
	 * */
	List<MessageQueue> queryNormalHttpMsgQueues();
	
}
