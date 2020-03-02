package com.ml.mnc.http;

import java.util.HashMap;
import java.util.Map;

import com.ml.mnc.Message;

/**
 * <pre>
 * Http消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月16日 下午4:03:39
 */
public interface HttpMsg extends Message {
	/** 字段层次分隔符 "." */
	final public static String FIELD_INTERVAL_CHAR = "\\.";
	/** 成功字段及其值分隔符 "=" */
	final public static String SUCCESS_FIELD_CODE_INTERVAL_CHAR = "=";
	/** 成功与否判断式分隔符 ":" */
	final public static String SUCCES_CODE_BELONG_METHOD_INTERVAL_CHAR=":";
	/** 多字段与 */
	final public static String SUCCESS_FIELDS_CODES_AND="&";
	/** 多字段或 */
	final public static String SUCCESS_FIELDS_CODES_OR="|";
	/** 通过http响应体判断推送是否成功 */
	final public static String SUCCESS_CODE_IN_BODY="body";
	/** 通过http状态码判断推送是否成功 */
	final public static String SUCCESS_CODE_IS_STATUS="status";
	/** http认证方式-0-head认证 */
	final public static int HEAD_HTTP_AUTH_METHOD_CODE = 0;

	/** http认证方式-1-body认证 */
	final public static int BODY_HTTP_AUTH_METHOD_CODE = 1;
	/** http认证方式-2-param认证 */
	final public static int PARAM_HTTP_AUTH_METHOD_CODE = 2;
	
	/** http消息临时缓存key */
	final public static String HTTP_MSG_CACHE_KEY="mnc:http:message";
	/** http消息推送中 标记正在推送到目标URL */
	final public static String HTTP_MSG_SENDING_CACHE_KEY="mnc:http:sending";
	/** SLA消息临时缓存key */
	final public static String HTTP_SLAMSG_CACHE_KEY="mnc:http:slamsg";
	
	/** 推送URL */
	String getSendUrl();

	/** 成功字段与编码,key=value格式,key支持多层,字段名以“.”分隔,如：status=200或data.status=200 */
	String getSuccessFieldCode();

	/** 认证参数 仅header认证方式 */
	String getAuthJson();

	/** 认证参数过期毫秒数 */
	Long getAuthExpireTimeMills();

	/** 自动重试次数 */
	Integer getAutoRetryCount();
	/**
	 * <pre>
	 * 认证方式枚举
	 * 
	 * HEAD head认证
	 * BODY body认证
	 * PARAM param认证
	 * </pre>
	 */
	public static enum AuthMethod {
		/** head认证 */
		HEAD(HEAD_HTTP_AUTH_METHOD_CODE, "head认证"),
		/** body认证 */
		BODY(BODY_HTTP_AUTH_METHOD_CODE, "body认证"),
		/** param认证 */
		PARAM(PARAM_HTTP_AUTH_METHOD_CODE, "param认证");
		private int code;
		private String alias;

		private static Map<Integer, AuthMethod> AUTH_METHOD = new HashMap<>(AuthMethod.values().length);
		static {
			for (AuthMethod method : AuthMethod.values()) {
				AUTH_METHOD.put(method.code, method);
			}
		}

		private AuthMethod(final int code, final String alias) {
			this.code = code;
			this.alias = alias;
		}

		public int code() {
			return code;
		}

		public String alias() {
			return alias;
		}

		final public static AuthMethod getInstance(int code) {
			return AUTH_METHOD.get(code);
		}

	}

}
