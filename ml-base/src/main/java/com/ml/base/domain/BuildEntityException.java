package com.ml.base.domain;

import com.ml.base.BussinessException;

/**
 * <pre>
 * 实体构建异常
 * 
 * </pre>
 * 
 * @author mecarlen 2019年3月1日 上午10:14:07
 */
public class BuildEntityException extends BussinessException {
	
	private static final long serialVersionUID = -4281164045022994011L;

	private BuildEntityException() {
		super();
	}

	private BuildEntityException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	private BuildEntityException(String message, Throwable cause) {
		super(message, cause);
	}

	private BuildEntityException(String message) {
		super(message);
	}

	private BuildEntityException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * <pre>
	 * 必填属性为空异常
	 * 
	 * </pre>
	 * */
	public static class RequiredFieldsIsEmptyException extends BuildEntityException{

		private static final long serialVersionUID = -7824786367346338361L;
		
	}
	
}
