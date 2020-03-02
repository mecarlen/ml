package com.ml.mnc.admin.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseVO;
import com.ml.mnc.admin.MessageQueue;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 消息队列信息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月4日 下午5:20:09
 */
@Setter
@Getter
public class MessageQueueVO extends BaseVO<Long, MessageQueueEntity> implements MessageQueue {
	private AppVO app;
	private String queueName;
	private String routeKey;
	private Integer maxRetryCount;
	private String retryIntervals;
	private boolean needCallback;
	private String remark;
	
	@Override
	public MessageQueueVO id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public MessageQueueVO state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public MessageQueueVO createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public MessageQueueVO updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public MessageQueueEntity toEntity() {
		return MessageQueueEntity.getInstance(this);
	}
	
	final public static MessageQueueVO getInstance(MessageQueue messageQueue) {
		if(null==messageQueue) {
			return null;
		}
		MessageQueueVO target = new MessageQueueVO();
		//TODO 深度复制
		BeanUtils.copyProperties(messageQueue, target,new String[] {"app"});
		target.setApp(AppVO.getInstance(messageQueue.getApp()));
		return target;
	}
}
