package com.ml.mnc.admin.query;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ml.base.query.BaseQuery;
import com.ml.mnc.admin.domain.MessageQueueVO;

/**
 * <pre>
 * 消息队列信息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月5日 下午7:36:42
 */
public interface MessageQueueQuery extends BaseQuery<Long, MessageQueueVO> {
	/**
	 * <pre>
	 * 查询队列
	 * 
	 * </pre>
	 * 
	 * @param appCode   String
	 * @param queueName String
	 * @param routeKey  String
	 * @return MessageQueueVO
	 */
	MessageQueueVO selectByAppAndQueue(@Param("appCode") String appCode, @Param("queueName") String queueName,
			@Param("routeKey") String routeKey);
	/**
	 * <pre>
	 * 查询所有在用的http消息队列
	 * 
	 * </pre>
	 * @return List<MessageQueueVO>
	 * */
	List<MessageQueueVO> selectNormalHttpMsgQueues();
}
