package com.ml.mnc.http.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.ml.base.domain.BaseVO;
import com.ml.mnc.admin.domain.AppVO;
import com.ml.mnc.http.HttpCallbackMsg;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * http回调消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午2:00:11
 */
@Setter
@Getter
public class HttpCallbackMsgVO extends BaseVO<Long, HttpCallbackMsgEntity> implements HttpCallbackMsg {
	private AppVO app;
	private String plCode;
	private String appCode;
	private String queueName;
	private String routeKey;
	private String businessType;
	private String sendUrl;
	private Long vccId;
	private String businessId;
	private String returnJson;
	private String msgContent;
	private Integer sendStatus;
	private Integer retryCount;
	private Integer preSendStatus;

	@Override
	public HttpCallbackMsgVO id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public HttpCallbackMsgVO state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public HttpCallbackMsgVO createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public HttpCallbackMsgVO updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	public HttpCallbackMsgVO returnJson(String returnJson) {
		this.setReturnJson(returnJson);
		return this;
	}

	public HttpCallbackMsgVO msgContent(HttpCallbackMQ callbackMq) {
		if (null != callbackMq) {
			this.setMsgContent(JSON.toJSONString(callbackMq));
		}
		return this;
	}

	@Override
	public HttpCallbackMsgEntity toEntity() {
		return HttpCallbackMsgEntity.getInstance(this);
	}

	final public static HttpCallbackMsgVO getInstance(HttpCallbackMsg httpCallbackMsg) {
		if (null == httpCallbackMsg) {
			return null;
		}
		HttpCallbackMsgVO target = new HttpCallbackMsgVO();
		BeanUtils.copyProperties(httpCallbackMsg, target, new String[] { "app" });
		target.setApp(AppVO.getInstance(httpCallbackMsg.getApp()));
		return target;
	}

	/**
	 * <pre>
	 * 重试次数累加
	 * </pre>
	 * 
	 * */
	public HttpCallbackMsgVO addRetryCount() {
		if(null==retryCount) {
			retryCount = 0;
		}
		this.retryCount += 1; 
		return this;
	}
	
	/**
	 * <pre>
	 * 消息体解析
	 * 
	 * </pre>
	 * 
	 * @return HttpCallbackMQ
	 */
	public HttpCallbackMQ getCallbackMq() {
		return JSON.parseObject(msgContent, HttpCallbackMQ.class);
	}

}
