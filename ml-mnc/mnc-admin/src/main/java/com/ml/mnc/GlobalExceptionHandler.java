package com.ml.mnc;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.base.BussinessException;
import com.ml.base.SystemException;
import com.ml.base.controller.WebResponse;
import com.ml.base.controller.WebResponse.WebResponseCode;

import lombok.extern.slf4j.Slf4j;

/**
 * com.ml.mnc
 *
 * @author yansongda <me@yansongda.cn>
 * @version 2019/11/4 下午1:39
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
	public @ResponseBody WebResponse<Boolean> handleBusinessException(BussinessException ex) {
		log.error("bizErrorCode:" + ex.getErrorCode(), ex);
		WebResponse<Boolean> response = new WebResponse<>();
		BussinessException.BizErrorCode errorCode = BussinessException.BizErrorCode.getInstance(ex.getErrorCode());
		if (null != errorCode) {
			response.code(BussinessException.BizErrorCode.getInstance(ex.getErrorCode()));
			response.setErrorMsg(ex.getMessage());
		} else {
			response.code(WebResponseCode.BIZEXCEPTION).errorMsg(ex.getErrorCode() + "-" + ex.getMessage());

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
	public WebResponse<Boolean> handleSystemException(SystemException ex) {
		log.error("systemException:", ex);
		WebResponse<Boolean> response = new WebResponse<>();
		response.code(WebResponseCode.SYSEXCEPTION).errorMsg(ex.getMessage());
		return response;
	}
	
	/**
	 * <pre>
	 * 未知异常
	 * 
	 * </pre>
	 * */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public WebResponse<Boolean> handlerGlobalException(Exception ex) {
		log.error("unKnowException", ex);
		WebResponse<Boolean> response = new WebResponse<>();
		return response.code(WebResponseCode.SYSEXCEPTION).errorMsg("unKnowException");
	}
}
