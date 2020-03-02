package com.ml.mnc.dingding.query;

import java.util.List;

import com.ml.base.query.BaseQuery;
import com.ml.mnc.dingding.domain.DingdingMsgParams;
import com.ml.mnc.dingding.domain.DingdingMsgVO;

/**
 * <pre>
 * 钉钉消息
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月5日 下午7:42:29
 */
public interface DingdingMsgQuery extends BaseQuery<Long, DingdingMsgVO>{
	/**
	 * <pre>
	 * 查询待推送的消息
	 * 
	 * </pre>
	 * @param params DingtalkMsgParams
	 * @return List<DingtalkMsgVO>
	 * */
	List<DingdingMsgVO> selectToSendMsgByPage(DingdingMsgParams params);
}
