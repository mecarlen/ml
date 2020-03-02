package com.ml.mnc.dingding.query;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ml.base.query.BaseQuery;
import com.ml.mnc.dingding.domain.DingdingMsgSendRecordVO;

/**
 * <pre>
 * 消息推送记录
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月5日 下午7:35:19
 */
public interface DingdingMsgSendRecordQuery extends BaseQuery<Long, DingdingMsgSendRecordVO> {
	/**
	 * <pre>
	 * 查询具体消息推送记录
	 * 
	 * </pre>
	 * 
	 * @param businessUid String
	 * @return List<MsgSendRecordVO>
	 */
	List<DingdingMsgSendRecordVO> selectByBusinessUid(@Param("businessUid")String businessUid);
}
