package com.ml.mnc.http.service;

import com.ml.base.query.PageModel;
import com.ml.base.service.BaseService;
import com.ml.mnc.http.domain.HttpMQ;
import com.ml.mnc.http.domain.HttpMsgParams;
import com.ml.mnc.http.domain.HttpMsgVO;

/**
 * <pre>
 * http消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午3:39:38
 */
public interface HttpMsgService extends BaseService<Long, HttpMsgVO> {
	/**
	 * <pre>
	 * 回调消息service
	 * 
	 * </pre>
	 * 
	 * @return HttpCallbackMsgService
	 */
	HttpCallbackMsgService callbackMsgService();

	/**
	 * <pre>
	 * 推送记录service
	 * 
	 * </pre>
	 * 
	 * @return HttpMsgSendRecordService
	 */
	HttpMsgSendRecordService sendRecordService();

	/**
	 * <pre>
	 * http消息落地
	 * 
	 * </pre>
	 * 
	 * @param httpMq HttpMQ
	 * @return HttpMsgVO
	 */
	HttpMsgVO saveMq(HttpMQ httpMq);
	
	/**
	 * <pre>
	 * http消息更新
	 * 
	 * </pre>
	 * @param httpMsg HttpMsgVO
	 * @return HttpMsgVO
	 * */
	HttpMsgVO modifyMsg(HttpMsgVO httpMsg);
	/**
	 * <pre>
	 * 分页查找延时待推送消息
	 * 
	 * </pre>
	 * 
	 * @param params HttpMsgParams
	 * @return PageModel<HttpMsgVO>
	 * */
	PageModel<HttpMsgVO> findDelaySendMsg(HttpMsgParams params);
	
	Integer findDelaySendMsgCount(HttpMsgParams params);
	/**
	 * <pre>
	 * 分页查找重试待推送消息
	 * 
	 * </pre>
	 * 
	 * @param params HttpMsgParams
	 * @return PageModel<HttpMsgVO>
	 */
	PageModel<HttpMsgVO> findRetrySendMsg(HttpMsgParams params);
	
	Integer findRetrySendMsgCount(HttpMsgParams params);
	
	/**
	 * <pre>
	 * 分页查找推送失败消息
	 * 
	 * </pre>
	 * 
	 * @param params HttpMsgParams
	 * @return PageModel<HttpMsgVO>
	 */
	PageModel<HttpMsgVO> findSendFailureMsg(HttpMsgParams params);
	
	Integer findSendFailureMsgCount(HttpMsgParams params);

	/**
	 * <pre>
	 * 推送成功，更新消息状态
	 * </pre>
	 * 
	 * @param returnJson
	 * @param httpMsg        HttpMsgVO
	 * @param beginTimeMills long
	 */
	void sendSuccess(String returnJson, HttpMsgVO httpMsg, long beginTimeMills);

	/**
	 * <pre>
	 * 推送失败，更新消息状态
	 * </pre>
	 * 
	 * @param httpMsg HttpMsgVO
	 * @param errMsg  String
	 */
	void sendFailure(HttpMsgVO httpMsg, long beginTimeMills, String errMsg);
	/**
	 * <pre>
	 * 自动重试推送失败，更新消息状态
	 * 
	 * 描述
	 * 不持久化消息，避免重复推送
	 * </pre>
	 * 
	 * @param httpMsg HttpMsgVO
	 * @param errMsg  String
	 */
	void autoRetryFailure(HttpMsgVO httpMsg, long beginTimeMills, String errMsg);

}
