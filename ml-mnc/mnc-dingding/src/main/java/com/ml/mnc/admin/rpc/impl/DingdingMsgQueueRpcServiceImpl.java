package com.ml.mnc.admin.rpc.impl;

import javax.annotation.Resource;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.ml.base.controller.WebResponse;
import com.ml.mnc.admin.rpc.DingdingMsgQueueRpcService;
import com.ml.mnc.dingding.consumer.DingdingMsgListener;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 钉钉消息队列
 * 
 * </pre>
 * @author mecarlen.Wang 2019年11月5日 上午10:52:19
 */
@Slf4j
@Service("dingdingMsgQueueRpcService")
public class DingdingMsgQueueRpcServiceImpl implements DingdingMsgQueueRpcService{
	@Resource
	private ConnectionFactory connectionFactory;
	@Resource
	private RabbitAdmin rabbitAdmin;
	@Resource
	private DingdingMsgListener dingdingMsgListener;
	
	@Override
	public WebResponse<Boolean> extendConsumer(String queueName, int concurrentConsumers, int prefetchCount) {
		log.info("extendConsumer begin,queue:{},concurrentConsumers:{},prefetchCount:{}", queueName,
				concurrentConsumers, prefetchCount);
		WebResponse<Boolean> response = new WebResponse<>();
		try {
			Queue queue = new Queue(queueName);
			SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
			container.setRabbitAdmin(rabbitAdmin);
			container.setConnectionFactory(connectionFactory);
			container.setQueues(queue);
			container.setPrefetchCount(prefetchCount);
			container.setConcurrentConsumers(concurrentConsumers);
			container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
			container.setMessageListener(dingdingMsgListener);
			container.start();
			response.result(Boolean.TRUE);
			log.info("extendConsumer success,queue:{},concurrentConsumers:{},prefetchCount:{}", queueName,
					concurrentConsumers, prefetchCount);
		} catch (Exception ex) {
			log.warn("extendConsumerException,queue:{},concurrentConsumers:{},prefetchCount:{}", queueName,
					concurrentConsumers, prefetchCount, ex);
			response.setErrorMsg("extendConsumerException");
			response.result(Boolean.FALSE);
		}
		return response;
	}
}
