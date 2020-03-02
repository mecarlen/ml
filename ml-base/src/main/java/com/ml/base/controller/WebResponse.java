package com.ml.base.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import com.ml.base.BussinessException;

/**
 * com.ml.base
 *
 * @author yansongda <me@yansongda.cn>
 * @version 2019/11/4 下午1:09
 */
@Data
public class WebResponse<T> {
	private Integer code;

	private String errorMsg;

	private T result;

	public WebResponse() {
		this.code = BussinessException.BizErrorCode.SUCCESS.code();
		this.errorMsg = BussinessException.BizErrorCode.SUCCESS.alias();
	}

	public WebResponse<T> code(BussinessException.BizErrorCode bizErrorCode) {
		this.code = bizErrorCode.code();
		this.errorMsg = bizErrorCode.alias();

		return this;
	}

	public WebResponse<T> result(T result) {
		this.result = result;

		return this;
	}

	public WebResponse<T> errorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}
	
	public WebResponse<T> code(WebResponseCode code) {
		if (null != code) {
			this.setCode(code.code());
		}
		return this;
	}

	/**
	 * <pre>
	 * API返回状态
	 * 
	 * SUCCESS 200 success
	 * BIZEXCEPTION 400 business exception
	 * AUTEXCEPTION 401 authenication exception
	 * PAREXCEPTION 402 parameters exception
	 * SYSEXCEPTION 500 system exception
	 * </pre>
	 * 
	 * @author mecarlen.wang 2018年3月29日 下午5:26:47
	 */
	public static enum WebResponseCode {
		/** 200-SUCCESS */
		SUCCESS(200, "success"),
		/** 400-BIZEXCEPTION */
		BIZEXCEPTION(400, "business exception"),
		/** 401-AUTEXCEPTION */
		AUTEXCEPTION(401, "authenication exception"),
		/** 402-PAREXCEPTION */
		PAREXCEPTION(402, "parameters exception"),
		/** 500-SYSEXCEPTION */
		SYSEXCEPTION(500, "system exception");

		final private int code;
		final private String alias;
		final private static Map<Integer, WebResponseCode> ENUMSTATE = new HashMap<Integer, WebResponseCode>();

		static {
			for (WebResponseCode state : WebResponseCode.values()) {
				ENUMSTATE.put(state.code, state);
			}
		}

		private WebResponseCode(int code, String alias) {
			this.code = code;
			this.alias = alias;
		}

		public Integer code() {
			return code;
		}

		public String alias() {
			return alias;
		}

		final public static WebResponseCode getInstance(int code) {
			return ENUMSTATE.get(code);
		}
	}
}
