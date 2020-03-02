package com.ml.mnc.admin.rpc;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ml.mnc.http.HttpMsg;
import com.ml.mnc.http.domain.HttpMsgVO;
import com.ml.util.cache.CacheUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * SLA消息生产
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年11月7日 下午2:47:32
 */
@Slf4j
@Component
public class SlaMsgProducer {
	@Resource
	private AmqpTemplate slaTemplate;
	@Value("${rabbitmq.sla.exchange}")
	private String slaExchange;
	@Value("${rabbitmq.sla.routekey}")
	private String slaRoutekey;
	@Resource
	private CacheUtils cacheUtils;

	/**
	 * <pre>
	 * 推送SLA
	 * 
	 * </pre>
	 * 
	 * @param httpMsg HttpMsgVO
	 */
	public void sendSla(HttpMsgVO httpMsg) {
		if (!httpMsg.isFirstSend()) {
			return;
		}
		SlaMessage slaMsg = new SlaMessage();
		slaMsg.setBusinessId(httpMsg.getBusinessId());
		slaMsg.setAppCode("mnc-http");
		try {
			// IN_QUEUE
			slaMsg.setNodeCode(CdrSLANode.IN_QUEUE.code());
			slaMsg.setNodeTime(httpMsg.getAcceptTimeMills() - httpMsg.getBusinessAcceptTimeMills());
			send(slaMsg);
			// OUT_MNC
			slaMsg.setNodeCode(CdrSLANode.OUT_MNC.code());
			slaMsg.setNodeTime(httpMsg.getFirstSendTimeMills() - httpMsg.getAcceptTimeMills());
			send(slaMsg);
			// VCC_RETURN
			slaMsg.setNodeCode(CdrSLANode.VCC_RETURN.code());
			slaMsg.setNodeTime(httpMsg.getSendRecords().get(0).getEndTimeMills()
					- httpMsg.getSendRecords().get(0).getBeginTimeMills());
			send(slaMsg);
		} catch (Exception ex) {
			cacheUtils.lPush(HttpMsg.HTTP_SLAMSG_CACHE_KEY, httpMsg);
			log.error(String.format("sendSLAException,businessId:%s", httpMsg.getBusinessId()), ex);
		}
	}

	/**
	 * <pre>
	 * 推送SLA消息
	 * 
	 * </pre>
	 * 
	 * @param slaMsg SLAMessage
	 */
	public void send(SlaMessage slaMsg) {
		log.info("SLA send begin businessId:{},nodeCode:{},nodeTime:{}", slaMsg.businessId, slaMsg.nodeCode,
				slaMsg.nodeTime);
		slaTemplate.convertAndSend(slaExchange, slaRoutekey, JSON.toJSONString(slaMsg));
		log.info("SLA send success businessId:{},nodeCode:{},nodeTime:{}", slaMsg.businessId, slaMsg.nodeCode,
				slaMsg.nodeTime);

	}

	/**
	 * <pre>
	 * SLA消息体
	 * 
	 * </pre>
	 */
	@Setter
	@Getter
	public static class SlaMessage {
		/** SLA队列锁 */
		final public static String SLA_MSG_QUEUE_LOCK = "mnc:http:sla:job-lock";

		private String appCode;
		private String businessId;
		private Integer nodeCode;
		private Long nodeTime;
	}

	/**
	 * <pre>
	 * 话单业务在MNC处SLA节点
	 * 
	 * </pre>
	 */
	public enum CdrSLANode {
		/** 队列等待耗时 */
		IN_QUEUE(3),
		/** MNC推送耗时 */
		OUT_MNC(4),
		/** 企业接口返回耗时 */
		VCC_RETURN(5);
		private int code;

		private CdrSLANode(int code) {
			this.code = code;
		}

		public int code() {
			return code;
		}
	}
}
