package com.ml.mnc.http.domain;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Document;

import com.google.common.collect.Lists;
import com.ml.base.domain.BaseVO;
import com.ml.mnc.MsgSendRecord;
import com.ml.mnc.admin.domain.AppVO;
import com.ml.mnc.http.HttpMsg;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * Http消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月16日 下午4:00:09
 */
@Document(indexName = "http", type = "message")
@Setter
@Getter
public class HttpMsgVO extends BaseVO<Long, HttpMsgEntity> implements HttpMsg {
	private AppVO app;
	private String plCode;
	private String appCode;
	private String queueName;
	private String routeKey;
	private Long vccId;
	private String businessId;
	private String businessType;
	private Long businessAcceptTimeMills;
	private String msgContent;
	private String sendUrl;
	private String authJson;
	private Long authExpireTimeMills;
	private String successFieldCode;
	private Long specifySendTimeMills;
	private String retryInterval;
	private Long acceptTimeMills;
	private Long firstSendTimeMills;
	private Long nextSendTimeMills;
	private Long firstSendSuccessTimeMills;
	private Integer retryCount;
	private Integer autoRetryCount;
	private Integer sendStatus;
	private List<HttpMsgSendRecordVO> sendRecords = Lists.newArrayList();

	public HttpMsgVO() {
		sendStatus = SendStatus.INITIAL.code();
		retryCount = 0;
		autoRetryCount = 0;
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
	 * 是否首次推送
	 * 
	 * </pre>
	 */
	public boolean isFirstSend() {

		return 1 == retryCount;
	}

	/**
	 * <pre>
	 * 重试次数累加
	 * 
	 * </pre>
	 */
	public HttpMsgVO addRetryCount() {
		if (null == retryCount) {
			retryCount = 0;
		}
		retryCount += 1;
		
		return this;
	}
	
	public HttpMsgVO addAutoRetryCount() {
		if (null == retryCount) {
			retryCount = 0;
		}
		retryCount += 1;
		
		if (null == autoRetryCount) {
			autoRetryCount = 0;
		}
		autoRetryCount += 1;
		
		return this;
	}

	/**
	 * <pre>
	 * UID格式：{acceptTimeMills}-{businessId}-{queueName}-{appCode}
	 * 
	 * </pre>
	 * 
	 * @return String
	 */
	public String getBusinessUid() {
		return String.format(MsgSendRecord.RECORD_UID, acceptTimeMills, businessId, queueName, appCode);
	}

	@Override
	public HttpMsgVO id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public HttpMsgVO state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public HttpMsgVO createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public HttpMsgVO updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public HttpMsgEntity toEntity() {
		return HttpMsgEntity.getInstance(this);
	}

	final public static HttpMsgVO getInstance(HttpMsg httpMsg) {
		if (null == httpMsg) {
			return null;
		}
		HttpMsgVO target = new HttpMsgVO();
		BeanUtils.copyProperties(httpMsg, target, new String[] { "app" });
		target.setApp(AppVO.getInstance(httpMsg.getApp()));
		return target;
	}

	/**
	 * <pre>
	 * 构建回调消息
	 * 
	 * </pre>
	 * 
	 * @return HttpCallbackMsgVO
	 */
	public HttpCallbackMsgVO buildCallbackMsg() {
		HttpCallbackMsgVO callback = new HttpCallbackMsgVO();
		callback.setPlCode(getPlCode());
		callback.setApp(getApp());
		callback.setAppCode(getAppCode());
		callback.setQueueName(getQueueName());
		callback.setRouteKey(getRouteKey());
		callback.setVccId(getVccId());
		callback.setBusinessType(getBusinessType());
		callback.setBusinessId(getBusinessId());
		callback.setSendUrl(getSendUrl());
		return callback;
	}

	/**
	 * <pre>
	 * 构建回调MQ消息体
	 * 
	 * </pre>
	 * 
	 * @param record MsgSendRecordVO
	 * @return HttpCallbackMQ
	 */
	public HttpCallbackMQ buildCallbackMq(HttpMsgSendRecordVO record) {
		HttpCallbackMQ cbmq = new HttpCallbackMQ();
		cbmq.setAppCode(getAppCode());
		cbmq.setVccId(getVccId());
		cbmq.setBusinessId(getBusinessId());
		cbmq.setBusinessType(getBusinessType());
		cbmq.setNextSendTimeMills(getNextSendTimeMills());
		cbmq.setSendCount(getRetryCount());
		cbmq.setSendBeginTimeMills(record.getBeginTimeMills());
		cbmq.setSendEndTimeMills(record.getEndTimeMills());
		cbmq.setSender(record.getSenderErp());
		if (SendStatus.FAILURE.code() == getSendStatus()) {
			cbmq.setErrorMsg(record.getRemark());
		} else {
			cbmq.setReturnContent(record.getRemark());
		}
		cbmq.setSendStatus(record.getSendStatus());
		return cbmq;
	}
}
