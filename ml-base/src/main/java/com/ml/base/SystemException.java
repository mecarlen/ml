package com.ml.base;
/**
 * <pre>
 * 	系统异常
 * 
 * 描述：
 *   1、非业务异常
 *   2、未预见异常
 *   3、底层框架抛出的异常
 * 
 * </pre>
 * 
 * @author mecarlen 2017年11月21日 下午5:39:51
 */
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 5813222280145322262L;

	public SystemException() {
		super();
	}

	public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemException(String message) {
		super(message);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}

}
