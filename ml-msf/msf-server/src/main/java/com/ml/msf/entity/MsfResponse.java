package com.ml.msf.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * <b>RPC返回</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月25日 下午3:35:01
 */
@Setter
@Getter
public class MsfResponse {
	private String requestId;
    private int code;
    private String error_msg;
    private Object data;
}
