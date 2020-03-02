package com.ml.mnc.http.service;

import java.util.List;

import com.ml.base.service.BaseService;
import com.ml.mnc.http.domain.HttpMsgSendRecordVO;

/**
 * <pre>
 * http消息推送记录
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月26日 上午11:16:25
 */
public interface HttpMsgSendRecordService extends BaseService<Long, HttpMsgSendRecordVO> {
	/**
	 * <pre>
	 * 查询具体消息推送记录
	 * 
	 * </pre>
	 * 
	 * @param businessUid String
	 * @return List<HttpMsgSendRecordVO>
	 */
	List<HttpMsgSendRecordVO> findByBusinessUid(String businessUid);
}
