package com.ml.mnc.admin;

import java.util.HashMap;
import java.util.Map;

import com.ml.base.domain.Entity;

/**
 * <pre>
 * 消息队列信息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月4日 下午5:11:16
 */
public interface MessageQueue extends Entity<Long> {
	/** 队列缓存key格式 mnc:adm:{appCode}:queue */
	final public static String QUEUE_CACHE_KEY_FORMAT = "mnc:adm:%s:queue";
	/** 队列缓存field格式 {queueName}-{routeKey} */
	final public static String QUEUE_CACHE_FIELD_FORMAT = "%s:%s";

	/** 队列所属应用 */
	App getApp();

	/** 队列名称 */
	String getQueueName();

	/** 队列路由 */
	String getRouteKey();

	/** 最大重试次数 */
	Integer getMaxRetryCount();

	/** 重试时间间隔数据，单位：秒 */
	String getRetryIntervals();

	/** 是否结果回传 */
	boolean isNeedCallback();

	/** 备注 */
	String getRemark();

	/**
	 * <pre>
	 * 队列类型
	 * 
	 * transient 瞬时 内存中有，但配置与数据库都不存在
	 * detached 离线 内存中和配置有，但数据库不存
	 * persistent 持久 内存、配置和数据库都有
	 * </pre>
	 */
	public static enum QueueType {
		/** 瞬时 */
		TRANSIENT(0, "瞬时"),
		/** 离线 */
		DETACHED(1, "离线"),
		/** 持久 */
		PERSISTENT(2, "持久");
		private int code;
		private String alias;

		private static Map<Integer, QueueType> QUEUE_TYPE = new HashMap<>(QueueType.values().length);
		static {
			for (QueueType type : QueueType.values()) {
				QUEUE_TYPE.put(type.code, type);
			}
		}

		private QueueType(int code, String alias) {
			this.code = code;
			this.alias = alias;
		}

		public int code() {
			return code;
		}

		public String alias() {
			return alias;
		}

		final public static QueueType getInstance(int code) {
			return QUEUE_TYPE.get(code);
		}

	}
}
