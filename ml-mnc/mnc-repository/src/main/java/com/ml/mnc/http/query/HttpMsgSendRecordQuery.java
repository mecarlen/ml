package com.ml.mnc.http.query;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ml.base.query.BaseQuery;
import com.ml.mnc.http.domain.HttpMsgSendRecordVO;

/**
 * <pre>
 * http消息推送记录
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月5日 下午7:35:19
 */
public interface HttpMsgSendRecordQuery extends BaseQuery<Long, HttpMsgSendRecordVO> {
	/**
	 * <pre>
	 * 查询具体消息推送记录
	 * 
	 * </pre>
	 * 
	 * @param businessUid String
	 * @return List<HttpMsgSendRecordVO>
	 */
	List<HttpMsgSendRecordVO> selectByBusinessUid(@Param("businessUid")String businessUid);
}
