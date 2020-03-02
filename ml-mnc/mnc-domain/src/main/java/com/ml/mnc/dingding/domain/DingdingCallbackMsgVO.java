package com.ml.mnc.dingding.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseVO;
import com.ml.mnc.admin.domain.AppVO;
import com.ml.mnc.dingding.DingdingCallbackMsg;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 钉钉回调应用消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月5日 上午11:14:28
 */
@Setter
@Getter
public class DingdingCallbackMsgVO extends BaseVO<Long, DingdingCallbackMsgEntity> implements DingdingCallbackMsg {
	private AppVO app;
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
	public DingdingCallbackMsgVO id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public DingdingCallbackMsgVO state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public DingdingCallbackMsgVO createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public DingdingCallbackMsgVO updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public DingdingCallbackMsgEntity toEntity() {
		return DingdingCallbackMsgEntity.getInstance(this);
	}

	final public static DingdingCallbackMsgVO getInstance(DingdingCallbackMsg dingdingCallBackMsg) {
		if (null == dingdingCallBackMsg) {
			return null;
		}
		DingdingCallbackMsgVO target = new DingdingCallbackMsgVO();
		BeanUtils.copyProperties(dingdingCallBackMsg, target, new String[] { "app" });
		target.setApp(AppVO.getInstance(dingdingCallBackMsg.getApp()));
		return target;
	}
	
	/**
	 * <pre>
	 * 重试次数累加
	 * 
	 * </pre>
	 * 
	 * */
	public DingdingCallbackMsgVO addRetryCount() {
		if(null==retryCount) {
			retryCount = 0;
		}
		this.retryCount += 1; 
		return this;
	}
}
