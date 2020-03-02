package com.ml.mnc.http.query;

import java.util.List;

import com.ml.base.query.BaseQuery;
import com.ml.mnc.http.domain.HttpMsgParams;
import com.ml.mnc.http.domain.HttpMsgVO;

/**
 * <pre>
 * http消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 上午10:48:57
 */
public interface HttpMsgQuery extends BaseQuery<Long, HttpMsgVO> {
	/**
	 * <pre>
	 * 查询延时待推送消息
	 * </pre>
	 * 
	 * @param params HttpMsgParams
	 * @return List<HttpMsgVO>
	 * */
	List<HttpMsgVO> selectDelayMsgByPage(HttpMsgParams params);
	
	/**
	 * <pre>
	 * 查询重试待推送消息
	 * 
	 * </pre>
	 * 
	 * @param params HttpMsgParams
	 * @return List<HttpMsgVO>
	 */
	List<HttpMsgVO> selectRetryMsgByPage(HttpMsgParams params);

	/**
	 * <pre>
	 * 查询推送失败的消息
	 * 
	 * </pre>
	 * 
	 * @param params HttpMsgParams
	 * @return List<HttpMsgVO>
	 */
	List<HttpMsgVO> selectSendFailureMsgByPage(HttpMsgParams params);

	/**
	 * <pre>
	 * 查询当前延时推送中的消息数
	 * 
	 * 描述
	 *   1、初始化任务
	 *   2、推送尝试中的任务
	 * </pre>
	 */
	Integer selectDelaySendMsgCount(HttpMsgParams params);
	
	/**
	 * <pre>
	 * 查询当前重试推送中的消息数
	 * 
	 * 描述
	 *   1、初始化任务
	 *   2、推送尝试中的任务
	 * </pre>
	 */
	Integer selectRetrySendMsgCount(HttpMsgParams params);
	
	/**
	 * <pre>
	 * 查询当前推送失败消息数
	 * 
	 * </pre>
	 */
	Integer selectSendFailureMsgCount(HttpMsgParams params);
}
