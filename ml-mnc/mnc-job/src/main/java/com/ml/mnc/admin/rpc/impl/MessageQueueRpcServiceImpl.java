package com.ml.mnc.admin.rpc.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import com.ml.mnc.admin.MessageQueue;
import com.ml.mnc.admin.service.MessageQueueService;
import com.ml.mnc.http.rpc.MessageQueueRpcService;

/**
 * <pre>
 * 消息队列
 * 
 * </pre>
 * @author mecarlen.Wang 2019年11月27日 下午5:07:38
 */
@Service("messageQueueRpcService")
public class MessageQueueRpcServiceImpl implements MessageQueueRpcService {
	@Resource
	private MessageQueueService messageQueueService;
	
	@Override
	public List<MessageQueue> queryNormalHttpMsgQueues() {
		return Lists.newArrayList(messageQueueService.findNormalHttpMsgQueues());
	}

	
}
