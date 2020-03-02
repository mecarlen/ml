package com.ml.mnc.admin.rpc.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import com.ml.base.BussinessException.BizErrorCode;
import com.ml.base.controller.WebResponse;
import com.ml.mnc.admin.rpc.MncAdmHttpClient;
import com.ml.mnc.admin.rpc.MncAdmHttpClient.InstanceOf;
import com.ml.mnc.admin.rpc.MsgQueueRpcService;
import com.ml.mnc.admin.service.MessageQueueService;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 消息队列
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年11月5日 上午11:38:29
 */
@Slf4j
@Service("msgQueueRpcService")
public class MsgQueueRpcServiceImpl implements MsgQueueRpcService {
	@Resource
	private RabbitAdmin rabbitAdmin;
	@Resource
	private MessageQueueService messageQueueService;
	@Resource
	private MncAdmHttpClient mncAdmHttpClient;

	@Override
	public WebResponse<Boolean> extendQueue(String exchangeName, String queueName, String routeKey) {
		log.info("extendQueue begin,exchange:{},queue:{},routeKey:{}", exchangeName, queueName, routeKey);
		WebResponse<Boolean> response = new WebResponse<>();
		if (StringUtils.isAnyBlank(exchangeName, queueName, routeKey)) {
			response.result(Boolean.FALSE);
			response.code(BizErrorCode.INVALID_PARAM);
			log.warn("invalidParam extendQueue falure,exchange:{},queue:{},routeKey:{}", exchangeName, queueName,
					routeKey);
			return response;
		}

		try {
			// queue
			Queue queue = new Queue(queueName);
			rabbitAdmin.declareQueue(queue);
			// exchange
			DirectExchange exchange = new DirectExchange(exchangeName);
			// binding
			rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routeKey));
			response.result(Boolean.TRUE);
			log.warn("extendQueue success,exchange:{},queue:{},routeKey:{}", exchangeName, queueName, routeKey);
		} catch (Exception ex) {
			log.warn("extendQueueException,exchange:{},queue:{},routeKey:{}", exchangeName, queueName, routeKey);
			response.setErrorMsg("extendQueueException");
			response.result(Boolean.FALSE);
		}
		return response;
	}

	@Override
	public WebResponse<Integer> extendConsumer(String appCode, String queueName, int concurrentConsumers,
			int prefetchCount) {
		final String ecUrlFormat = "%squeue/extend/%s/%d/%d";
		int successCount = 0;
		WebResponse<Integer> response = new WebResponse<>();
		if (StringUtils.isAnyBlank(appCode, queueName) || concurrentConsumers <= 0 || prefetchCount <= 0) {
			response.result(successCount);
			return response;
		}
		// 通过appCode从注册中心获取app节点列表
		List<InstanceOf> instances = mncAdmHttpClient.getAppInstances(appCode);
		if (instances.isEmpty()) {
			response.result(successCount);
			return response;
		}
		// 循环调用动态扩展消费者API
		for (InstanceOf instance : instances) {
			if (InstanceOf.UP_STATUS_INSTANCE.equalsIgnoreCase(instance.getStatus())) {
				// 健康实例才调
				if (mncAdmHttpClient.extendConsumer(String.format(ecUrlFormat, instance.getRootUrl(), queueName,
						concurrentConsumers, prefetchCount))) {
					successCount++;
				}
			}
		}
		response.result(successCount);
		return response;
	}

}
