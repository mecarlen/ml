package com.ml.mnc;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 公共消息
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月4日 下午6:32:46
 */
@Setter
@Getter
public abstract class AbstractMessage {
	private String queueName;
	private String routeKey;
	private String businessId;
	private String businessType;
	private Long businessAcceptTimeMills;
	private String title;
	private String content;
	private Long specifyTimeMills;
	private String delayInterval;
}
