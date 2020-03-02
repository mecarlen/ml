package com.ml.mnc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ml.base.BussinessException.BizErrorCode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <pre>
 * RPC返回基类
 * 
 * state String 状态码
 * errorMsg String 错误信息
 * result T 返回结果
 * </pre>
 * 
 * @author mecarlen.wang 2018年3月29日 下午2:02:26
 */
@ApiModel(description="请求返回类")
public class RpcResponse<T> implements Serializable {

	private static final long serialVersionUID = -4397672856377576733L;
	@ApiModelProperty(value="状态码",required=true)
	private String code;
	@ApiModelProperty(value="错误信息",required=false)
	private String errorMsg;
	@ApiModelProperty(value="具体返回结果",required=false)
	private T result;

	public RpcResponse() {
		this.code = RpcResponseCode.SUCCESS.code();
	}

	public String getCode() {
		return code;
	}

	private void setCode(String code) {
		this.code = code;
	}

	public RpcResponse<T> code(RpcResponseCode code) {
		if (null != code) {
			this.setCode(code.code());
		}
		return this;
	}
	
	public RpcResponse<T> code(BizErrorCode bizErrorCode) {
		if (null != bizErrorCode) {
			this.setCode(String.valueOf(bizErrorCode.code()));
		}
		return this;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	private void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public RpcResponse<T> errorMsg(String errorMsg) {
		this.setErrorMsg(errorMsg);
		return this;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public RpcResponse<T> result(T result) {
		this.setResult(result);
		return this;
	}

	final public boolean isSuccess() {
		return RpcResponseCode.SUCCESS.code().equals(code);
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
	public static enum RpcResponseCode {
		/** 200-SUCCESS */
		SUCCESS("200", "success"),
		/** 400-BIZEXCEPTION */
		BIZEXCEPTION("400", "business exception"),
		/** 401-AUTEXCEPTION */
		AUTEXCEPTION("401", "authenication exception"),
		/** 402-PAREXCEPTION */
		PAREXCEPTION("402","parameters exception"),
		/** 500-SYSEXCEPTION */
		SYSEXCEPTION("500", "system exception");

		final private String code;
		final private String alias;
		final private static Map<String, RpcResponseCode> ENUMSTATE = new HashMap<String, RpcResponseCode>();

		static {
			for (RpcResponseCode state : RpcResponseCode.values()) {
			    ENUMSTATE.put(state.code, state);
			}
		}

		private RpcResponseCode(String code, String alias) {
			this.code = code;
			this.alias = alias;
		}

		public String code() {
			return code;
		}

		public String alias() {
			return alias;
		}

		final public static RpcResponseCode getInstance(String code) {
			return ENUMSTATE.get(code);
		}
	}

}
