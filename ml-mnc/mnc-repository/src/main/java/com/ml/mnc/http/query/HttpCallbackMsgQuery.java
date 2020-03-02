package com.ml.mnc.http.query;

import java.util.List;

import com.ml.base.query.BaseQuery;
import com.ml.mnc.http.domain.HttpCallbackMsgParams;
import com.ml.mnc.http.domain.HttpCallbackMsgVO;

/**
 * <pre>
 * Http回调消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午3:28:24
 */
public interface HttpCallbackMsgQuery extends BaseQuery<Long, HttpCallbackMsgVO> {
	/**
	 * <pre>
	 * 取待推送的消息
	 * 
	 * </pre>
	 * @param params HttpCallbackMsgParams
	 * @return List<HttpCallbackMsgVO>
	 * */
	List<HttpCallbackMsgVO> selectToSendMsgByPage(HttpCallbackMsgParams params);
}
