package com.ml.mnc.dingding.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ml.base.service.impl.BaseServiceImpl;
import com.ml.mnc.dingding.domain.DingdingCallbackMsgEntity;
import com.ml.mnc.dingding.domain.DingdingCallbackMsgVO;
import com.ml.mnc.dingding.query.DingdingCallbackMsgQuery;
import com.ml.mnc.dingding.repository.DingdingCallbackMsgRepository;
import com.ml.mnc.dingding.service.DingdingCallbackMsgService;

/**
 * <pre>
 * 回调应用消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:18:50
 */
@Service("dingdingCallbackMsgService")
public class DingdingCallbackMsgServiceImpl
		extends BaseServiceImpl<Long, DingdingCallbackMsgEntity, DingdingCallbackMsgVO, DingdingCallbackMsgRepository, DingdingCallbackMsgQuery>
		implements DingdingCallbackMsgService {
	@Resource
	public void setDingdingCallbackMsgRepository(DingdingCallbackMsgRepository callbackMsgRepository) {
		this.setRepository(callbackMsgRepository);
	}
	
	@Resource
	public void setDingdingCallbackMsgQuery(DingdingCallbackMsgQuery callbackMsgQuery) {
		this.setQuery(callbackMsgQuery);
	}

}
