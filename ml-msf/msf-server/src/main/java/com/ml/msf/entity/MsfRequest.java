package com.ml.msf.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * <b>RPC请求</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月25日 下午3:32:02
 */
@Setter
@Getter
public class MsfRequest {
	private String id;
    private String className;// 类名
    private String methodName;// 函数名称
    private Class<?>[] parameterTypes;// 参数类型
    private Object[] parameters;// 参数列表
}
