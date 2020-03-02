package com.ml.mnc.http.service;

import java.util.List;

import com.ml.base.query.PageModel;
import com.ml.base.service.BaseService;
import com.ml.mnc.http.domain.HttpCallbackMsgParams;
import com.ml.mnc.http.domain.HttpCallbackMsgVO;

/**
 * <pre>
 * http回调消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午3:39:52
 */
public interface HttpCallbackMsgService extends BaseService<Long, HttpCallbackMsgVO> {
	/**
	 * <pre>
	 * 批量保存消息
	 * 
	 * </pre>
	 * @param callbackMsglist List<HttpCallbackMsgVO>
	 * */
	void save(List<HttpCallbackMsgVO> callbackMsglist);
	
	/**
	 * <pre>
	 * 分页查找待推送消息
	 * 
	 * </pre>
	 * 
	 * @param params HttpCallbackMsgParams
	 * @return PageModel<HttpCallbackMsgVO>
	 */
	PageModel<HttpCallbackMsgVO> findToSendMsg(HttpCallbackMsgParams params);
	
}
