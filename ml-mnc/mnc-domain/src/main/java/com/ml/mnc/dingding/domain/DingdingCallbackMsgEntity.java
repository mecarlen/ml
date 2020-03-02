package com.ml.mnc.dingding.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseEntity;
import com.ml.mnc.admin.domain.AppEntity;
import com.ml.mnc.dingding.DingdingCallbackMsg;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 钉钉回调应用消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月5日 上午11:11:11
 */
@Setter
@Getter
public class DingdingCallbackMsgEntity extends BaseEntity<Long, DingdingCallbackMsgVO> implements DingdingCallbackMsg {
	private AppEntity app;
	private String plCode;
	private String appCode;
	private String queueName;
	private String routeKey;
	private String businessType;
	private Long vccId;
	private String businessId;
	private String returnJson;
	private String msgContent;
	private Integer sendStatus;
	private Integer retryCount;

	@Override
	public DingdingCallbackMsgEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public DingdingCallbackMsgEntity state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public DingdingCallbackMsgEntity createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public DingdingCallbackMsgEntity updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public DingdingCallbackMsgVO toVO() {
		return DingdingCallbackMsgVO.getInstance(this);
	}

	final public static DingdingCallbackMsgEntity getInstance(DingdingCallbackMsg dingdingCallBackMsg) {
		if (null == dingdingCallBackMsg) {
			return null;
		}
		DingdingCallbackMsgEntity target = new DingdingCallbackMsgEntity();
		BeanUtils.copyProperties(dingdingCallBackMsg, target, new String[] { "app" });
		target.setApp(AppEntity.getInstance(dingdingCallBackMsg.getApp()));
		return target;
	}

}
