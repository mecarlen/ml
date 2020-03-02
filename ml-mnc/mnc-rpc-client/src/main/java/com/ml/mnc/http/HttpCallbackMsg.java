package com.ml.mnc.http;

import com.ml.mnc.CallbackMsg;

/**
 * <pre>
 * http回调消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月23日 下午2:29:54
 */
public interface HttpCallbackMsg extends CallbackMsg {
	/** 回调消息缓存key */
	final public static String CALLBACK_MSG_CACHEKEY="mnc:http:callback:msg";
	/** 回调消息队列锁key */
	final public static String CALLBACK_MSG_QUEUE_LOCK="mnc:http:callback:job-lock"; 
	/** 推送URL */
	String getSendUrl();

}
