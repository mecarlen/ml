package com.ml.mnc.dingding.service;

import java.util.List;

import com.ml.base.service.BaseService;
import com.ml.mnc.dingding.domain.DingdingMsgSendRecordVO;

/**
 * <pre>
 * 消息推送记录
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:08:38
 */
public interface DingdingMsgSendRecordService extends BaseService<Long, DingdingMsgSendRecordVO> {

	/**
	 * <pre>
	 * 查询具体消息推送记录
	 * 
	 * </pre>
	 * 
	 * @param businessUid String
	 * @return List<MsgSendRecordVO>
	 */
	List<DingdingMsgSendRecordVO> findByBusinessUid(String businessUid);
}
