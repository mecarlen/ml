package com.ml.mnc.admin.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseEntity;
import com.ml.mnc.admin.MessageQueue;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 消息队列信息
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月4日 下午4:30:51
 */
@Setter
@Getter
public class MessageQueueEntity extends BaseEntity<Long, MessageQueueVO> implements MessageQueue{
	private AppEntity app;
	private String queueName;
	private String routeKey;
	private Integer maxRetryCount;
	private String retryIntervals;
	private boolean needCallback;
	private String remark;
	
	@Override
	public MessageQueueEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public MessageQueueEntity state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public MessageQueueEntity createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public MessageQueueEntity updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public MessageQueueVO toVO() {
		return MessageQueueVO.getInstance(this);
	}
	
	final public static MessageQueueEntity getInstance(MessageQueue messageQueue) {
		if(null==messageQueue) {
			return null;
		}
		MessageQueueEntity target = new MessageQueueEntity();
		BeanUtils.copyProperties(messageQueue, target,new String[] {"app"});
		target.setApp(AppEntity.getInstance(messageQueue.getApp()));
		return target;
	}
}
