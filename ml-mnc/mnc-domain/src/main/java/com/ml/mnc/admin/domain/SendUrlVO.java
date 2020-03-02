package com.ml.mnc.admin.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseVO;
import com.ml.mnc.admin.SendUrl;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 推送URL
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月20日 上午11:09:23
 */
@Setter
@Getter
public class SendUrlVO extends BaseVO<Long, SendUrlEntity> implements SendUrl {
	private AppVO app;
	private MessageQueueVO messageQueue;
	private Long vccId;
	private String businessType;
	private String urlAddress;
	private String successFieldCode;
	private String authField;
	private Integer authMethod;
	private AuthRenewalVO authRenewal;
	private String remark;

	@Override
	public SendUrlVO id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public SendUrlVO state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public SendUrlVO createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public SendUrlVO updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public SendUrlEntity toEntity() {
		return SendUrlEntity.getInstance(this);
	}

	final public static SendUrlVO getInstance(SendUrl sendUrl) {
		if (null == sendUrl) {
			return null;
		}
		SendUrlVO target = new SendUrlVO();
		BeanUtils.copyProperties(sendUrl, target, new String[] { "app", "messageQueue","authRenewal" });
		target.setApp(AppVO.getInstance(sendUrl.getApp()));
		target.setMessageQueue(MessageQueueVO.getInstance(sendUrl.getMessageQueue()));
		target.setAuthRenewal(AuthRenewalVO.getInstance(sendUrl.getAuthRenewal()));
		return target;
	}
}
