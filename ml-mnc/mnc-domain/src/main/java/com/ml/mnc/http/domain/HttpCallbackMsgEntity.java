package com.ml.mnc.http.domain;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseEntity;
import com.ml.mnc.admin.domain.AppEntity;
import com.ml.mnc.http.HttpCallbackMsg;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * Http回调消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午1:59:34
 */
@Setter
@Getter
public class HttpCallbackMsgEntity extends BaseEntity<Long, HttpCallbackMsgVO> implements HttpCallbackMsg {
	private AppEntity app;
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
	public HttpCallbackMsgEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public HttpCallbackMsgEntity state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public HttpCallbackMsgEntity createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public HttpCallbackMsgEntity updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public HttpCallbackMsgVO toVO() {
		return HttpCallbackMsgVO.getInstance(this);
	}

	final public static HttpCallbackMsgEntity getInstance(HttpCallbackMsg httpCallbackMsg) {
		if (null == httpCallbackMsg) {
			return null;
		}
		HttpCallbackMsgEntity target = new HttpCallbackMsgEntity();
		BeanUtils.copyProperties(httpCallbackMsg, target, new String[] { "app" });
		target.setApp(AppEntity.getInstance(httpCallbackMsg.getApp()));
		return target;
	}
}
