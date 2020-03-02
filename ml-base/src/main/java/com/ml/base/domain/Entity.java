package com.ml.base.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 实体
 * 
 * id Long id
 * state int 状态 1-在用,0-停用,-1弃用
 * createTime Date 创建时间
 * updateTime Date 更新时间
 * </pre>
 * 
 * @author mecarlen 2016年8月9日 下午5:49:04
 */
public interface Entity<ID extends Serializable> {
	/** 实体-状态-在用 1 */
	final public static int STATE_NORMAL = 1;
	/** 实体-状态-停用 0 */
	final public static int STATE_DISABLE = 0;
	/** 实体-状态-弃用 -1 */
	final public static int STATE_DEPRECATED = -1;

	/**
	 * <pre>
	 * 获取Entity唯一标示
	 * 
	 * </pre>
	 * 
	 * @return ID
	 */
	ID getId();

	/**
	 * <pre>
	 * 获取Entity状态码
	 * 
	 * </pre>
	 * 
	 * @return ID
	 */
	Integer getYn();

	/**
	 * <pre>
	 * 取Entity创建时间,在CommonServiceImpl用到,,此方法BaseEntity/BaseVO实现
	 * 
	 * <pre>
	 * 
	 * @return Date
	 */
	Date getCreateTime();

	/**
	 * <pre>
	 * 取Entity更新时间,在CommonServiceImpl用到,此方法BaseEntity/BaseVO实现
	 * 
	 * <pre>
	 * 
	 * @return Date
	 */
	Date getUpdateTime();

	/**
	 * <pre>
	 * 实体状态
	 * 
	 * NORMAL 在用
	 * DISABLE 停用
	 * DEPRECATED 弃用
	 * </pre>
	 * 
	 * @author mecarlen 2016年8月11日 上午11:48:53
	 */
	public enum State {
		/** 在用 1 */
		NORMAL(Entity.STATE_NORMAL, "在用"),
		/** 停用 0 */
		DISABLE(Entity.STATE_DISABLE, "停用"),
		/** 弃用 1 */
		DEPRECATED(Entity.STATE_DEPRECATED, "弃用");
		private int code;
		private String alias;
		final private static Map<Integer, State> STATES = new HashMap<Integer, State>();
		static {
			for (State state : State.values()) {
			    STATES.put(state.code, state);
			}
		}

		private State(final int code, final String alias) {
			this.code = code;
			this.alias = alias;
		}

		public int code() {
			return code;
		}

		public String alias() {
			return alias;
		}

		public static State getInstance(final int code) {
			return STATES.get(code);
		}
	}
}
