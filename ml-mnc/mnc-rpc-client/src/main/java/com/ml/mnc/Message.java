package com.ml.mnc;

import java.util.HashMap;
import java.util.Map;

import com.ml.mnc.admin.App;

/**
 * <pre>
 * 抽象消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月16日 下午4:38:05
 */
public interface Message {
	/** 最大消息重试次数-30 */
	final public static int MAX_MSG_RETRY_COUNT = 30;
	/** 默认消息重试次数-3 */
	final public static int DEFAULT_MSG_RETRY_COUNT = 3;
	/** 默认消息重试时间间隔 15s,35s,1m  */
	final public static String DEFAULT_MSG_DELAY_INTERVAL="15s,35s,1m";
	/** 推送状态-失败 -1 */
	final public static int FAILURE_SEND_STATUS = -1;
	/** 推送状态-初始 0 */
	final public static int INITIAL_SEND_STATUS = 0;
	/** 推送状态-推送中 1 */
	final public static int SENDING_SEND_STATUS = 1;
	/** 推送状态-成功 2 */
	final public static int SUCCESS_SEND_STATUS = 2;

	/** 延迟间隔毫秒-秒单位 */
	final public static long SECOND_INTERVAL_MILLS = 1000L;
	/** 延迟间隔毫秒-分钟单位 */
	final public static long MINUTE_INTERVAL_MILLS = 60 * 1000L;
	/** 延迟间隔毫秒-小时单位 */
	final public static long HOUR_INTERVAL_MILLS = 3600 * 1000L;

	/** 所属应用 */
	App getApp();

	/** 所属产品线编码 */
	String getPlCode();

	/** 所属应用编码 */
	String getAppCode();

	/** 队列名称 */
	String getQueueName();

	/** 队列路由 */
	String getRouteKey();

	/** 消息所属企业Id */
	Long getVccId();

	/** 业务Id */
	String getBusinessId();

	/** 业务类型 */
	String getBusinessType();

	/** 业务接收时间 */
	Long getBusinessAcceptTimeMills();

	/** 消息正文 */
	String getMsgContent();

	/** 指定发送时间 */
	Long getSpecifySendTimeMills();

	/** 重试时间间隔，以接收时间（优先用业务接收时间）开始计算，单位支持，s-秒，m-分,h-小时，如3s,5m,1h */
	String getRetryInterval();

	/** 消息接收时间毫秒数,接收时系统当前时间 */
	Long getAcceptTimeMills();

	/** 首次推送时间毫秒数 */
	Long getFirstSendTimeMills();

	/** 下次推送时间毫秒数 */
	Long getNextSendTimeMills();

	/** 首次成功推送时间毫秒数 */
	Long getFirstSendSuccessTimeMills();

	/** 重试次数 */
	Integer getRetryCount();

	/** 推送状态 */
	Integer getSendStatus();

	/**
	 * <pre>
	 * 推送状态枚举
	 * 
	 * 
	 * INITIAL 初始
	 * SENDING 推送中
	 * SUCCESS 成功
	 * FAILURE 失败
	 * </pre>
	 */
	public static enum SendStatus {
		/** 初始 */
		INITIAL(Message.INITIAL_SEND_STATUS, "初始"),
		/** 推送中 */
		SENDING(Message.SENDING_SEND_STATUS, "推送中"),
		/** 失败 */
		FAILURE(Message.FAILURE_SEND_STATUS, "失败"),
		/** 成功 */
		SUCCESS(Message.SUCCESS_SEND_STATUS, "成功");

		private int code;
		private String alias;

		private static Map<Integer, SendStatus> SEND_STATUS = new HashMap<>(SendStatus.values().length);
		static {
			for (SendStatus status : SendStatus.values()) {
				SEND_STATUS.put(status.code, status);
			}
		}

		private SendStatus(int code, String alias) {
			this.code = code;
			this.alias = alias;
		}

		public int code() {
			return code;
		}

		public String alias() {
			return alias;
		}

		final public static SendStatus getInstance(int code) {
			return SEND_STATUS.get(code);
		}

	}

	/**
	 * <pre>
	 * 时间间隔枚举
	 * 
	 * SECOND 秒
	 * MINUTE 分
	 * HOUR 时
	 * </pre>
	 * 
	 * SECOND 秒 MINUTE 分 HOUR 时
	 */
	public static enum IntervalType {
		/** s-秒 */
		SECOND("s", "秒", SECOND_INTERVAL_MILLS),
		/** m-分钟 */
		MINUTE("m", "分钟", MINUTE_INTERVAL_MILLS),
		/** h-小时 */
		HOUR("h", "小时", HOUR_INTERVAL_MILLS);

		private String code;
		private String alias;
		private long mills;

		private static Map<String, IntervalType> INTERVAL_TYPE_MAP = new HashMap<>(IntervalType.values().length);
		static {
			for (IntervalType type : IntervalType.values()) {
				INTERVAL_TYPE_MAP.put(type.code, type);
			}
		}

		private IntervalType(String code, String alias, long mills) {
			this.code = code;
			this.alias = alias;
			this.mills = mills;
		}

		public String code() {
			return code;
		}

		public String alias() {
			return alias;
		}

		public long mills() {
			return mills;
		}

		final public static IntervalType getInstance(String code) {
			return INTERVAL_TYPE_MAP.get(code);
		}
	}

}
