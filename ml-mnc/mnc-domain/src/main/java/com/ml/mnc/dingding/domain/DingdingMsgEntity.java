package com.ml.mnc.dingding.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.ml.base.domain.BaseEntity;
import com.ml.mnc.admin.domain.AppEntity;
import com.ml.mnc.dingding.DingdingMsg;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 钉钉消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月4日 下午4:26:37
 */
@Setter
@Getter
public class DingdingMsgEntity extends BaseEntity<Long, DingdingMsgVO> implements DingdingMsg {
	private AppEntity app;
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

	public DingdingMsgEntity() {
		sendStatus = SendStatus.INITIAL.code();
		retryCount = 0;
		vccId = 0L;
		yn = State.NORMAL.code();
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
	 * @param tryCount int 重试次数 从0开始
	 * @return long
	 */
	public long getToSendTimeMills(int tryCount) {
		return getToSendTimeMills(tryCount, retryInterval);
	}

	/**
	 * <pre>
	 * 计算推送时间
	 * </pre>
	 * 
	 * @param tryCount      int
	 * @param retryInterval String
	 * @return long
	 */
	public long getToSendTimeMills(int tryCount, String retryInterval) {
		// 指定时间
		if (0 == tryCount && null != specifySendTimeMills) {
			return specifySendTimeMills > 0 ? specifySendTimeMills : 0L;
		}
		
		// 间隔重试
		if (StringUtils.isBlank(retryInterval)) {
			return 0L;
		}
		
		// 重试次数超过间隔配置最大次数时，间隔采用轮询方式间隔
		String[] intervalArray = retryInterval.split(",");
		int idx = tryCount < intervalArray.length ? tryCount : tryCount % intervalArray.length;
		// 间隔单位:s,m,h
		IntervalType interval = IntervalType.getInstance(intervalArray[idx].substring(intervalArray[idx].length() - 1));
		if (null == interval) {
			// 未知间隔单位，识为立即执行
			return 0L;
		}
		// 指定间隔首次执行时间=接收时间+间隔时间，后面=上次执行时间+间隔时间
		long actMills = acceptTimeMills;
		if (tryCount == 0) {
			if (null != businessAcceptTimeMills && businessAcceptTimeMills > 0) {
				actMills = businessAcceptTimeMills;
			}
		} else {
			actMills = nextSendTimeMills;
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
		return getToSendTimeMills(0) >= (acceptTimeMills + IntervalType.SECOND.mills() * 5);
	}

	@Override
	public DingdingMsgEntity id(Long id) {
		this.setId(id);
		return this;
	}

	@Override
	public DingdingMsgEntity state(State state) {
		if (null != state) {
			this.setYn(state.code());
		}
		return this;
	}

	@Override
	public DingdingMsgEntity createTime(Date createTime) {
		if (null != createTime) {
			this.setCreateTime(createTime);
		}
		return this;
	}

	@Override
	public DingdingMsgEntity updateTime(Date updateTime) {
		if (null != updateTime) {
			this.setUpdateTime(new Date(updateTime.getTime()));
		}
		return this;
	}

	@Override
	public DingdingMsgVO toVO() {
		return DingdingMsgVO.getInstance(this);
	}

	final public static DingdingMsgEntity getInstance(DingdingMsg dingdingMsg) {
		if (null == dingdingMsg) {
			return null;
		}
		DingdingMsgEntity target = new DingdingMsgEntity();
		BeanUtils.copyProperties(dingdingMsg, target);
		target.setApp(AppEntity.getInstance(dingdingMsg.getApp()));
		return target;
	}
}
