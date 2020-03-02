package com.ml.mnc.admin.rpc;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ml.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 话单消息模拟生产
 * 
 * </pre>
 * @author mecarlen.Wang 2019年11月6日 下午2:33:54
 */
@Slf4j
@Component
public class CTIEventMsgProducer {
	@Resource
	private AmqpTemplate rabbitTemplate;
	
	/**
	 * <pre>
	 * 消息推送
	 * 
	 * </pre>
	 * */
	public boolean send(CTIEventMsg msg) {
		msg.setAppCode("mnc-admin");
		msg.setSuccessFieldCode("code=200");
		msg.setBusinessId(UUID.id32().substring(16,32));
		msg.setBusinessAcceptTimeMills(System.currentTimeMillis());
		msg.setAutoRetry(true);
		try {
			rabbitTemplate.convertAndSend("mnc.http.exchange","http.default",JSON.toJSONString(msg));
		}catch(Exception ex) {
			log.error("sendMsgException",ex);
			return false;
		}
		return true;
	}
	
	/**
	 * <pre>
	 * 话单消息
	 * 
	 * </pre>
	 * */
	@Setter
	@Getter
	public static class CTIEventMsg{
		private String appCode;
	    private Long vccId;
	    private String businessId;
	    private String businessType;
	    private Long businessAcceptTimeMills;
	    private String msgContent;
	    private String sendUrl;
	    private String successFieldCode;
	    private String authJson;
	    private Long authExpireTimeMills;
	    private Long specifySendTimeMills;
	    private String retryInterval;
	    private Boolean autoRetry;
	}
}
