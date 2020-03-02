package com.ml.mnc.dingding.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseVO;
import com.ml.mnc.MsgSendRecord;
import com.ml.mnc.admin.domain.AppVO;
import com.ml.mnc.dingding.DingdingMsg;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 钉钉消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月4日 下午6:04:09
 */
@Setter
@Getter
public class DingdingMsgVO extends BaseVO<Long, DingdingMsgEntity> implements DingdingMsg {
	private AppVO app;
	private String plCode;
	private String appCode;
	private String queueName;
	private String routeKey;
	private Long vccId;
	private String businessId;
	private String businessType;
	private Long businessAcceptTimeMills;
	private String msgTitle;
	private String msgContent;
	private String dingdingUrl;
	private Long specifySendTimeMills;
	private String retryInterval;
	private Long acceptTimeMills;
	private Long firstSendTimeMills;
	private Long nextSendTimeMills;
	private Long firstSendSuccessTimeMills;
	private Integer retryCount;
	private Integer sendStatus;

	public DingdingMsgVO() {
		sendStatus = SendStatus.INITIAL.code();
		retryCount = 0;
		vccId = 0L;
		yn = State.NORMAL.code();
	}

	/**
	 * <pre>
	 * 是否推送成功
	 * 
	 * </pre>
	 * 
	 * @return boolean
	 */
	public boolean isSuccess() {
		return SendStatus.SUCCESS == SendStatus.getInstance(sendStatus);
	}

	/**
	 * <pre>
	 * 是否推送失败
	 * 
	 * </pre>
	 * 
	 * @return boolean
	 */
	public boolean isFailure() {
		return SendStatus.FAILURE == SendStatus.getInstance(sendStatus);
	}

	/**
	 * <pre>
	 * 是否推送中
	 * </pre>
	 * 
	 * @return boolean
	 */
	public boolean isSending() {
		return SendStatus.SENDING == SendStatus.getInstance(sendStatus);
	}

	/**
	 * <pre>
	 * 是否初始状态
	 * 
	 * </pre>
	 * 
	 * @return boolean
	 */
	public boolean isInitial() {
		return SendStatus.INITIAL == SendStatus.getInstance(sendStatus);
	}

	/**
	 * <pre>
	 * 重试次数累加
	 * 
	 * </pre>
	 */
	public DingdingMsgVO addRetryCount() {
		if (null == retryCount) {
			retryCount = 0;
		}
		this.retryCount += 1;
		return this;
	}

	/**
	 * <pre>
	 * UID格式：{acceptTimeMills}-{businessId}-{queueName}-{appCode}
	 * 
	 * </pre>
	 * @return String
	 */
	public String getBusinessUid() {
		return String.format(MsgSendRecord.RECORD_UID, acceptTimeMills, businessId, queueName, appCode);
	}

	@Override
	public DingdingMsgVO id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public DingdingMsgVO state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public DingdingMsgVO createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public DingdingMsgVO updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public DingdingMsgEntity toEntity() {
		return DingdingMsgEntity.getInstance(this);
	}

	final public static DingdingMsgVO getInstance(DingdingMsg dingdingMsg) {
		if (null == dingdingMsg) {
			return null;
		}
		DingdingMsgVO target = new DingdingMsgVO();
		BeanUtils.copyProperties(dingdingMsg, target);
		target.setApp(AppVO.getInstance(dingdingMsg.getApp()));
		return target;
	}
}
