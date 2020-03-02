package com.ml.mnc;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 推送记录
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月26日 上午11:12:43
 */
public interface MsgSendRecord {
	/** UID格式：{acceptTimeMills}-{businessId}-{queueName}-{appCode}  */
	final public static String RECORD_UID="%d-%s-%s-%s";
	/** 推送类型-自动-0 */
	final public static int AUTO_SEND_TYPE = 0;
	/** 推送类型-手动-1 */
	final public static int MANU_SEND_TYPE = 1;

	/** 业务UID */
	String getBusinessUid();

	/** 推送开始时间 */
	Long getBeginTimeMills();

	/** 推送结束时间 */
	Long getEndTimeMills();

	/** 推送状态 */
	Integer getSendStatus();

	/** 推送类型 */
	Integer getSendType();

	/** 手动推送人ERP */
	String getSenderErp();

	/** 手动推送人姓名 */
	String getSenderName();

	/** 备注 */
	String getRemark();
	
	
	/**
	 * <pre>
	 * 推送类型
	 * AUTO 自动
	 * MANU 手动
	 * </pre>
	 * */
	public static enum SendType{
		/** 自动 */
		AUTO(AUTO_SEND_TYPE,"自动"),
		/** 手动 */
		MANU(MANU_SEND_TYPE,"手动");
		private int code;
		private String alias;
		
		private static Map<Integer,SendType> SEND_TYPE = new HashMap<>(SendType.values().length);
		static {
			for(SendType type:SendType.values()) {
				SEND_TYPE.put(type.code, type);
			}
		}
		
		private SendType(int code,String alias) {
			this.code = code;
			this.alias = alias;
		}
		
		public int code() {
			return code;
		}
		
		public String alias() {
			return alias;
		}
		
		final public static SendType getInstance(int code) {
			return SEND_TYPE.get(code);
		}
	}
}
