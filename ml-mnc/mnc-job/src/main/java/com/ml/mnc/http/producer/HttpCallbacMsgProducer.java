package com.ml.mnc.http.producer;

import org.springframework.amqp.core.AmqpTemplate;

import com.ml.base.BussinessException;
import com.ml.mnc.http.domain.HttpCallbackMsgVO;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 回调消息生产者
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月25日 下午6:40:10
 */
@Slf4j
public class HttpCallbacMsgProducer {
	/** 回调消息队列路由格式 */
	final public static String QUEUE_ROUTE_FORMAT = "%s.default";
	private String callbackExchange;
	
	private AmqpTemplate rabbitTemplate;
	
	public void setCallbackExchange(String callbackExchange) {
		this.callbackExchange = callbackExchange;
	}
	
	public void setRabbitTemplate(AmqpTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void send(HttpCallbackMsgVO callbackMsg) {
		log.info("sendCallbackMsg begin,appCode:{},mqBody:{}", callbackMsg.getAppCode(), callbackMsg.getMsgContent());
		String routeKey = String.format(QUEUE_ROUTE_FORMAT, callbackMsg.getAppCode());
		try {
			rabbitTemplate.convertAndSend(callbackExchange, routeKey, callbackMsg.getMsgContent());
		} catch (Exception ex) {
			log.error("sendCallbackMsgFailre,appCode:{},mqBody:{}", callbackMsg.getAppCode(),
					callbackMsg.getMsgContent(),ex);
			throw new BussinessException("sendCallbackMsgFailre,msgId:" + callbackMsg.getId());
		}
		log.info("sendCallbackMsg over,appCode:{},mqBody:{}", callbackMsg.getAppCode(), callbackMsg.getMsgContent());
	}
}
