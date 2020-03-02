package com.ml.mnc.admin.job;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;
import com.ml.mnc.admin.MessageQueue;
import com.ml.mnc.admin.rpc.DingdingMsgProducer;
import com.ml.mnc.http.rpc.MessageQueueRpcService;

/**
 * com.ml.mnc.admin.job
 *
 * @author yansongda <me@yansongda.cn>
 * @version 2019/11/6 下午5:57
 */
@Slf4j
@Component
@JobHandler("httpQueueMsgOverstockMonitorJob")
public class HttpQueueMsgOverstockMonitorJob extends IJobHandler {
	@Resource
	private ConnectionFactory connectionFactory;
	private Channel channel;
	
	@Value("${rabbitmq.http.max-overstock-count}")
	private Long maxOverstockCount;

	@Resource
	private MessageQueueRpcService messageQueueRpcService;

	@Resource
	private DingdingMsgProducer dingdingMsgProducer;

	@Value("${spring.profiles.active}")
	private String env;

	@Override
	public void init() {
		if(null == channel) {
			Connection connection = connectionFactory.createConnection();
			channel = connection.createChannel(false);
			log.info("<<<<<<<<<<< http monitor channel init >>>>>>>>>");
		}
	}
	
	@Override
	public ReturnT<String> execute(String s) {
		try {
			List<MessageQueue> queues = messageQueueRpcService.queryNormalHttpMsgQueues();
			for (MessageQueue queue : queues) {
				long currOverstockCount = channel.messageCount(queue.getQueueName());
				log.info("queue: {}; currOverstockCount: {}", queue.getQueueName(), currOverstockCount);
				if (currOverstockCount > maxOverstockCount) {
					log.warn("queue:{} message count greater maxOverstockCount. current: {}; maxOverstockCount: {}",queue.getQueueName(),
							currOverstockCount, maxOverstockCount);
					String content = "队列: %s\r\n当前值: %s\r\n阙值: %s";
					dingdingMsgProducer.send(env + ": 队列积压通知",
							String.format(content, queue.getQueueName(), currOverstockCount, maxOverstockCount));
				}

			}
		} catch (IOException ex) {
			log.error("queryQueueOverstockCountException", ex);
		}

		return SUCCESS;
	}
}
