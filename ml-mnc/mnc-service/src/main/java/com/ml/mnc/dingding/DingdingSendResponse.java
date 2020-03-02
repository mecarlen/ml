package com.ml.mnc.dingding;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 钉钉推送接口返回对象
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月9日 下午6:34:17
 */
@Setter
@Getter
public class DingdingSendResponse {
	/** 钉钉接口返回成功编码-0 */
	final public static transient long SUCCESS = 0L;
	private Long errcode;
	private String errmsg;

	public boolean isSuccess() {
		return SUCCESS == errcode;
	}
}
