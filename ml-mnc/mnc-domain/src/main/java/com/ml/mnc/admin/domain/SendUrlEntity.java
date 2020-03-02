package com.ml.mnc.admin.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseEntity;
import com.ml.mnc.admin.SendUrl;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 推送URL
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月19日 下午6:01:31
 */
@Setter
@Getter
public class SendUrlEntity extends BaseEntity<Long, SendUrlVO> implements SendUrl {
	private AppEntity app;
	private MessageQueueEntity messageQueue;
	private Long vccId;
	private String businessType;
	private String urlAddress;
	private String successFieldCode;
	private String authField;
	private Integer authMethod;
	private AuthRenewalVO authRenewal;
	private String remark;

	@Override
	public SendUrlEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public SendUrlEntity state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public SendUrlEntity createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public SendUrlEntity updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public SendUrlVO toVO() {
		return SendUrlVO.getInstance(this);
	}

	final public static SendUrlEntity getInstance(SendUrl sendUrl) {
		if (null == sendUrl) {
			return null;
		}
		SendUrlEntity target = new SendUrlEntity();
		BeanUtils.copyProperties(sendUrl, target, new String[] { "app", "messageQueue" });
		target.setApp(AppEntity.getInstance(sendUrl.getApp()));
		target.setMessageQueue(MessageQueueEntity.getInstance(sendUrl.getMessageQueue()));
		return target;
	}

}
