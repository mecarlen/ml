package com.ml.base.rpc;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 消息公共字段
 * 
 * </pre>
 * @author mecarlen.Wang 2019年9月9日 上午10:31:10
 */
@Setter
@Getter
public abstract class AbstractMQ {
	private String exchange;
	private String queueName;
	private String routeKey;
	private Date messageTimeStamp;
}
