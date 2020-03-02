package com.ml.mnc.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ml.base.service.impl.BaseServiceImpl;
import com.ml.mnc.admin.MessageQueue;
import com.ml.mnc.admin.domain.MessageQueueEntity;
import com.ml.mnc.admin.domain.MessageQueueVO;
import com.ml.mnc.admin.query.MessageQueueQuery;
import com.ml.mnc.admin.repository.MessageQueueRepository;
import com.ml.mnc.admin.service.MessageQueueService;
import com.ml.mnc.admin.service.SendUrlService;
import com.ml.util.cache.CacheUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 消息队列信息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月6日 上午11:22:49
 */
@Slf4j
@Service("messageQueueService")
public class MessageQueueServiceImpl
		extends BaseServiceImpl<Long, MessageQueueEntity, MessageQueueVO, MessageQueueRepository, MessageQueueQuery>
		implements MessageQueueService {
	@Resource
	private CacheUtils cacheUtils;
	@Resource
	private SendUrlService sendUrlService;

	@Resource
	public void setMessageQueueRepository(MessageQueueRepository messageQueueRepository) {
		this.setRepository(messageQueueRepository);
	}

	@Resource
	public void setMessageQueueQuery(MessageQueueQuery messageQueueQuery) {
		this.setQuery(messageQueueQuery);
	}

	@Override
	public SendUrlService sendUrlService() {
		return sendUrlService;
	}

	@Override
	public MessageQueueVO getMessageQueue(String appCode, String queueName, String routeKey) {
		if (StringUtils.isAnyBlank(appCode, queueName, routeKey)) {
			log.warn("params exists empty return null,appCode:{},queueName:{},routeKey:{}", appCode, queueName,
					routeKey);
			return null;
		}
		String key = String.format(MessageQueue.QUEUE_CACHE_KEY_FORMAT, appCode.toLowerCase());
		String field = String.format(MessageQueue.QUEUE_CACHE_FIELD_FORMAT, queueName, routeKey);
		MessageQueueVO queue = cacheUtils.hGet(key, field, MessageQueueVO.class);
		if (null != queue) {
			return queue;
		}
		log.info("getMessageQueueFromDB,appCode:{},queueName:{},routeKey:{}", appCode, queueName, routeKey);
		queue = query.selectByAppAndQueue(appCode.toLowerCase(), queueName, routeKey);
		if (null != queue) {
			boolean res = cacheUtils.hSet(key, field, queue);
			log.info("getMessageQueueFromDB and cache messageQueue,key:{},field:{},value:{},return:{}", key, field,
					JSON.toJSONString(queue), res);
		}
		return queue;
	}

	@Override
	public List<MessageQueueVO> findNormalHttpMsgQueues() {
		return query.selectNormalHttpMsgQueues();
	}
	
	

}
