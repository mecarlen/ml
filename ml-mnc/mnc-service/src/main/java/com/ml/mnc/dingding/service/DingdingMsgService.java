package com.ml.mnc.dingding.service;

import com.ml.base.query.PageModel;
import com.ml.base.service.BaseService;
import com.ml.mnc.dingding.domain.DingdingMQ;
import com.ml.mnc.dingding.domain.DingdingMsgParams;
import com.ml.mnc.dingding.domain.DingdingMsgVO;

/**
 * <pre>
 * 钉钉消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:09:00
 */
public interface DingdingMsgService extends BaseService<Long, DingdingMsgVO> {
	/**
	 * <pre>
	 * 回调消息service
	 * 
	 * </pre>
	 * 
	 * @return DingdingCallbackMsgService
	 */
	DingdingCallbackMsgService callbackMsgService();

	/**
	 * <pre>
	 * 推送记录service
	 * 
	 * </pre>
	 * 
	 * @return DingdingMsgSendRecordService
	 */
	DingdingMsgSendRecordService sendRecordService();

	/**
	 * <pre>
	 * 钉钉消息落地
	 * 
	 * </pre>
	 * 
	 * @param dingdingMq DingtalkMQ
	 * @return DingtalkMsgVO
	 */
	DingdingMsgVO saveMq(DingdingMQ dingdingMq);

	/**
	 * <pre>
	 * 分页查找待推送消息
	 * 
	 * </pre>
	 * 
	 * @param params DingtalkMsgParams
	 * @return PageModel<DingtalkMsgVO>
	 */
	PageModel<DingdingMsgVO> findToSendMsg(DingdingMsgParams params);

	/**
	 * <pre>
	 * 推送成功，更新消息状态
	 * </pre>
	 * 
	 * @param dingdingMsg    DingtalkMsgVO
	 * @param beginTimeMills long
	 * @param returnJson String
	 */
	void sendSuccess(DingdingMsgVO dingdingMsg, long beginTimeMills,String returnJson);

	/**
	 * <pre>
	 * 推送失败，更新消息状态
	 * </pre>
	 * 
	 * @param dingdingMsg DingtalkMsgVO
	 * @param beginTimeMills long
	 * @param errMsg      String
	 */
	void sendFailure(DingdingMsgVO dingdingMsg, long beginTimeMills, String errMsg);

}
