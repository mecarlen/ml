package com.ml.mnc.dingding.consumer;

import javax.annotation.Resource;

import com.ml.base.rpc.AbstractMQListener;
import com.ml.mnc.dingding.DingdingMsgSender;
import com.ml.mnc.dingding.domain.DingdingMQ;
import com.ml.mnc.dingding.domain.DingdingMsgVO;
import com.ml.mnc.dingding.service.DingdingMsgService;

/**
 * <pre>
 * 钉钉消息监听 
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月9日 上午9:59:38
 */
public class DingdingMsgListener extends AbstractMQListener<DingdingMQ>{
	@Resource
	private DingdingMsgService dingdingMsgService;
	@Resource
	private DingdingMsgSender dingdingMsgSender;
	
	@Override
	public boolean onMessage(DingdingMQ message) throws Exception {
		//落地
		DingdingMsgVO dingdingMsg = dingdingMsgService.saveMq(message);
		//是否立即推送
		if(!dingdingMsg.toEntity().isFirstNeedDelay()) {
			//异步请求钉钉
			dingdingMsgSender.send(dingdingMsg, dingdingMsg.getAcceptTimeMills());
		}
		return true;
	}
	
}
