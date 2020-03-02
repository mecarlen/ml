package com.ml.mnc.dingding;

import com.ml.base.domain.Entity;
import com.ml.mnc.Message;

/**
 * <pre>
 * 钉钉消息
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月4日 下午6:0:18
 */
public interface DingdingMsg extends Message, Entity<Long> {
	/** 消息主题 */
	String getMsgTitle();
	
	/** 钉钉URL */
	String getDingdingUrl();

}
