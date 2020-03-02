package com.ml.mnc.http.consumer;

import javax.annotation.Resource;

import com.ml.base.rpc.AbstractMQListener;
import com.ml.mnc.http.HttpMsgSender;
import com.ml.mnc.http.domain.HttpMQ;
import com.ml.mnc.http.domain.HttpMsgVO;
import com.ml.mnc.http.service.HttpMsgService;

/**
 * <pre>
 * http消息监听 
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月16日 下午2:34:35
 */
public class HttpMsgListener extends AbstractMQListener<HttpMQ> {
	@Resource
	private HttpMsgService httpMsgService;
	@Resource
	private HttpMsgSender httpMsgSender;
	
	@Override
	public boolean onMessage(HttpMQ message) throws Exception {
		//落地
		HttpMsgVO httpMsg = httpMsgService.saveMq(message);
		//是否立即推送
		if(!httpMsg.toEntity().isFirstNeedDelay()) {
			if(!httpMsgSender.isCloseImmediatelySend(httpMsg)) {
				//目标URL正常时直接推送
				httpMsgSender.send(httpMsg);
			} else {
				//URL当前不可用直接入库
				httpMsgService.sendFailure(httpMsg, httpMsg.getAcceptTimeMills(), "timeout");
			}
		} else {
			//延时推送的直接入库
			httpMsgService.create(httpMsg);
		}
		
		return true;
	}
	
}
