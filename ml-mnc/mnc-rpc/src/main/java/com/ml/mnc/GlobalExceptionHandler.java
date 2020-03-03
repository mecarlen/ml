package com.ml.mnc;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.base.BussinessException;
import com.ml.base.SystemException;
import com.ml.mnc.RpcResponse.RpcResponseCode;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * <b>异常统一处理</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年3月3日 下午3:19:04
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * <pre>
	 * 业务异常
	 * 
	 * </pre>
	 */
	@ExceptionHandler(BussinessException.class)
	public @ResponseBody RpcResponse<Boolean> handleBusinessException(BussinessException ex) {
		log.error("bizErrorCode:" + ex.getErrorCode(), ex);
		RpcResponse<Boolean> response = new RpcResponse<>();
		BussinessException.BizErrorCode errorCode = BussinessException.BizErrorCode.getInstance(ex.getErrorCode());
		if (null != errorCode) {
			response.code(BussinessException.BizErrorCode.getInstance(ex.getErrorCode()));
			response.errorMsg(ex.getMessage());
		} else {
			response.code(RpcResponseCode.BIZEXCEPTION).errorMsg(ex.getErrorCode() + "-" + ex.getMessage());

		}
		return response;
	}

	/**
	 * <pre>
	 * 系统异常
	 * 
	 * </pre>
	 */
	@ExceptionHandler(SystemException.class)
	public @ResponseBody RpcResponse<Boolean> handleSystemException(SystemException ex) {
		log.error("systemException:", ex);
		RpcResponse<Boolean> response = new RpcResponse<>();
		response.code(RpcResponseCode.SYSEXCEPTION).errorMsg(ex.getMessage());
		return response;
	}

	/**
	 * <pre>
	 * 未知异常
	 * 
	 * </pre>
	 */
	@ExceptionHandler(value = Exception.class)
	public @ResponseBody RpcResponse<Boolean> handlerGlobalException(Exception ex) {
		log.error("unKnowException", ex);
		RpcResponse<Boolean> response = new RpcResponse<>();
		return response.code(RpcResponseCode.SYSEXCEPTION).errorMsg("unKnowException");
	}
}
