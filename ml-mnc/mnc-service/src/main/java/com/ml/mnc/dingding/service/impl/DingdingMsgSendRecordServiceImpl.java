package com.ml.mnc.dingding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ml.base.service.impl.BaseServiceImpl;
import com.ml.mnc.dingding.domain.DingdingMsgSendRecordEntity;
import com.ml.mnc.dingding.domain.DingdingMsgSendRecordVO;
import com.ml.mnc.dingding.query.DingdingMsgSendRecordQuery;
import com.ml.mnc.dingding.repository.DingdingMsgSendRecordRepository;
import com.ml.mnc.dingding.service.DingdingMsgSendRecordService;

/**
 * <pre>
 * 消息推送记录
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:27:16
 */
@Service("dingdingMsgSendRecordService")
public class DingdingMsgSendRecordServiceImpl
		extends BaseServiceImpl<Long, DingdingMsgSendRecordEntity, DingdingMsgSendRecordVO, DingdingMsgSendRecordRepository, DingdingMsgSendRecordQuery>
		implements DingdingMsgSendRecordService {
	
	@Resource
	public void setMsgSendRecordRepository(DingdingMsgSendRecordRepository msgSendRecordRepository) {
		this.setRepository(msgSendRecordRepository);
	}

	@Resource
	public void setMsgSendRecordQuery(DingdingMsgSendRecordQuery msgSendRecordQuery) {
		this.setQuery(msgSendRecordQuery);
	}

	@Override
	public List<DingdingMsgSendRecordVO> findByBusinessUid(String businessUid) {
		return query.selectByBusinessUid(businessUid);
	}
	
	
}
