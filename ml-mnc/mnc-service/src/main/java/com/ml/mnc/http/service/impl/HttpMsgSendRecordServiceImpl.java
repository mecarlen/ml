package com.ml.mnc.http.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ml.base.service.impl.BaseServiceImpl;
import com.ml.mnc.http.domain.HttpMsgSendRecordEntity;
import com.ml.mnc.http.domain.HttpMsgSendRecordVO;
import com.ml.mnc.http.query.HttpMsgSendRecordQuery;
import com.ml.mnc.http.repository.HttpMsgSendRecordRepository;
import com.ml.mnc.http.service.HttpMsgSendRecordService;

/**
 * <pre>
 * 消息推送记录
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:27:16
 */
@Service("httpMsgSendRecordService")
public class HttpMsgSendRecordServiceImpl
		extends BaseServiceImpl<Long, HttpMsgSendRecordEntity, HttpMsgSendRecordVO, HttpMsgSendRecordRepository, HttpMsgSendRecordQuery>
		implements HttpMsgSendRecordService {
	
	@Resource
	public void setMsgSendRecordRepository(HttpMsgSendRecordRepository httpMsgSendRecordRepository) {
		this.setRepository(httpMsgSendRecordRepository);
	}

	@Resource
	public void setMsgSendRecordQuery(HttpMsgSendRecordQuery httpMsgSendRecordQuery) {
		this.setQuery(httpMsgSendRecordQuery);
	}

	@Override
	public List<HttpMsgSendRecordVO> findByBusinessUid(String businessUid) {
		return query.selectByBusinessUid(businessUid);
	}
	
	
}
