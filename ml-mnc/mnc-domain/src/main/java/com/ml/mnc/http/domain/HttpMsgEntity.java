package com.ml.mnc.http.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ml.base.domain.BaseEntity;
import com.ml.mnc.admin.domain.AppEntity;
import com.ml.mnc.http.HttpMsg;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * Http消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月16日 下午3:59:58
 */
@Setter
@Getter
public class HttpMsgEntity extends BaseEntity<Long, HttpMsgVO> implements HttpMsg {
	private AppEntity app;
	private String plCode;
	private String appCode;
	private String queueName;
	private String routeKey;
	private Long vccId;
	private String businessId;
	private String businessType;
	private Long businessAcceptTimeMills;
	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart", store = true)
	private String msgContent;
	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart", store = true)
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

	public HttpMsgEntity() {
		sendStatus = SendStatus.INITIAL.code();
		retryCount = 0;
		autoRetryCount = 0;
		vccId = 0L;
		yn = State.NORMAL.code();
	}

	@Override
	public HttpMsgEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public HttpMsgEntity state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public HttpMsgEntity createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public HttpMsgEntity updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public HttpMsgVO toVO() {
		return HttpMsgVO.getInstance(this);
	}

	final public static HttpMsgEntity getInstance(HttpMsg httpMsg) {
		if (null == httpMsg) {
			return null;
		}
		HttpMsgEntity target = new HttpMsgEntity();
		BeanUtils.copyProperties(httpMsg, target, new String[] { "app" });
		target.setApp(AppEntity.getInstance(httpMsg.getApp()));
		return target;
	}

	/**
	 * <pre>
	 * 取当前待推送时间
	 * 
	 * </pre>
	 * 
	 * @return long
	 */
	public long getToSendTimeMills() {
		//任务重试次数=重试次数-自动重试次数
		return getToSendTimeMills(retryCount - autoRetryCount, retryInterval);
	}

	/**
	 * <pre>
	 * 取待推送时间
	 * 
	 * <b>计算算法</b>
	 * <li>指定发送时间/specifySendTimeMills不为空的，指定发送时间>0的，待推送时间=指定发送时间
	 * <li>指定发送时间/specifySendTimeMills不为空的，指定发送时间<=0的，待推送时间=0
	 * <li>延迟时间间隔/delayInterval不为空的，且业务接受时间>0的，推送时间=业务接收时间+延迟间隔时长
	 * <li>延迟时间间隔/delayInterval不为空的，且业务接受时间<=0的，推送时间=接收时间+延迟间隔时长 
	 * <b>返回值含义</b>
	 * <li>0-收到消息立即执行
	 * 
	 * </pre>
	 * 
	 * @param retryCount int 重试次数 从0开始
	 * @return long
	 */
	public long getToSendTimeMills(int retryCount) {
		return getToSendTimeMills(retryCount, retryInterval);
	}

	/**
	 * <pre>
	 * 根据指定延迟间隔配置取推送时间
	 * 
	 * </pre>
	 * 
	 * @param retryInterval String
	 * @return long
	 */
	public long getToSendTimeMills(String retryInterval) {
		return getToSendTimeMills(retryCount, retryInterval);
	}

	/**
	 * <pre>
	 * 计算推送时间
	 * </pre>
	 * 
	 * @param retryCount    int
	 * @param delayInterval String
	 * @return long
	 */
	public long getToSendTimeMills(int retryCount, String retryInterval) {
		// 指定时间
		if (0 == retryCount && null != specifySendTimeMills) {
			return specifySendTimeMills > 0 ? specifySendTimeMills : 0L;
		}
		// 间隔重试
		if (StringUtils.isBlank(retryInterval)) {
			return 0L;
		}
		// 重试次数超过间隔配置最大次数时，间隔采用轮询方式间隔
		String[] intervalArray = retryInterval.split(",");
		int idx = retryCount < intervalArray.length ? retryCount : retryCount % intervalArray.length;
		// 间隔单位:s,m,h
		IntervalType interval = IntervalType.getInstance(intervalArray[idx].substring(intervalArray[idx].length() - 1));
		if (null == interval) {
			// 未知间隔单位，识为立即执行
			return 0L;
		}
		// 指定间隔首次执行时间=接收时间+间隔时间，后面=当前时间+间隔时间
		long actMills = acceptTimeMills;
		if (retryCount == 0) {
			if (null != businessAcceptTimeMills && businessAcceptTimeMills > 0) {
				actMills = businessAcceptTimeMills;
			}
		} else {
			actMills = System.currentTimeMillis();
		}
		// 接收时间毫秒数+延迟时间毫秒数
		return actMills
				+ Integer.valueOf(intervalArray[idx].substring(0, intervalArray[idx].length() - 1)) * interval.mills();
	}

	/**
	 * <pre>
	 * 首次是否需求延时推送
	 * 
	 * 未来5秒以内推送的立即推送
	 * </pre>
	 * 
	 * @return boolean
	 */
	public boolean isFirstNeedDelay() {
		// 首次推送时间<接收时间+1s的直接推送
		return isNeedDelay(acceptTimeMills);
	}

	/**
	 * <pre>
	 * 指定时间是否需求延时推送
	 * 
	 * 未来5秒以外推送的立即推送
	 * </pre>
	 * 
	 * @return boolean
	 */
	public boolean isNeedDelay(long currTimeMills) {
		return getToSendTimeMills(0) >= (currTimeMills + IntervalType.SECOND.mills() * 5);
	}

	/**
	 * <pre>
	 * 推送接口返回是否成功
	 * 
	 * </pre>
	 * 
	 * @param returnJson String
	 * @return boolean
	 */
	public boolean isSendSuccess(String returnJson) {
		if (StringUtils.isAnyBlank(returnJson, successFieldCode)) {
			// 接口无返回或未传成功判断方式的按协议200返回成功
			return true;
		}
		// 接口返回判断逻辑
		String[] keyValue = successFieldCode.split(SUCCESS_FIELD_CODE_INTERVAL_CHAR);
		if (keyValue.length < 2) {
			// 格式不正确按协议200返回成功
			return true;
		}
		List<String> fields = Lists.newArrayList(keyValue[0].split(FIELD_INTERVAL_CHAR));
		JSONObject robj = null;
		try {
			robj = JSONObject.parseObject(returnJson);
			for (int idx = 0; idx < fields.size(); idx++) {
				if (1 == (fields.size() - idx)) {
					break;
				}
				JSONObject obj = robj.getJSONObject(fields.get(idx));
				if (null != obj) {
					robj = obj;
				} else {
					robj = null;
					break;
				}
			}
		} catch (JSONException ex) {
			robj = null;
			// do nothing
		}
		if (null == robj) {
			// 格式不正确按协议200返回成功
			return false;
		}
		return keyValue[1].equals(robj.getString(fields.get(fields.size() - 1)));
	}

	/**
	 * <pre>
	 * 认证是否过期
	 * 
	 * </pre>
	 */
	public boolean authDataIsExpired() {

		return false;
	}

}
