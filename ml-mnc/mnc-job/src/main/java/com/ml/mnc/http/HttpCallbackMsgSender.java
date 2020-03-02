package com.ml.mnc.http;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import com.ml.mnc.Message.SendStatus;
import com.ml.mnc.http.domain.HttpCallbackMsgVO;
import com.ml.mnc.http.producer.HttpCallbacMsgProducer;

/**
 * <pre>
 * 回调消息推送者
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月26日 上午10:16:57
 */
@Slf4j
@Component
public class HttpCallbackMsgSender {
	@Resource
	private HttpCallbacMsgProducer httpCallbacMsgProducer;
	public void send(HttpCallbackMsgVO callbackMsg) {
		try {
			//推送消息
			httpCallbacMsgProducer.send(callbackMsg);
			//成功状态
			callbackMsg.setSendStatus(SendStatus.SUCCESS.code());
			callbackMsg.addRetryCount();
		} catch(Exception ex) {
			//失败更新
			callbackMsg.setSendStatus(SendStatus.FAILURE.code());
			callbackMsg.addRetryCount();
			log.error("unknowException,skipCallbackMsgSend",ex);
		}
	}
}
