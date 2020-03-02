package com.ml.mnc;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 全局的controller层日志输出
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年7月1日 上午10:29:25
 */
@Slf4j
@Component
@Aspect
@Order(1)
public class GlobalControllerLogAspect {

	@Around("execution(public com.ml.base.controller.WebResponse com.ml.mnc..*Controller.*(..))")
	public Object logPrint(ProceedingJoinPoint pjp) throws Throwable {
		long startTimeMillis = System.currentTimeMillis();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        String url = request.getRequestURL().toString();
        String headerParams = getHeaderParams(request, request.getHeaderNames());
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String params = getParams((MethodSignature)pjp.getSignature(), pjp.getArgs());
        log.info("requestStart, allParameters, url: {},header: {}, method: {}, uri: {}, params: {}", url, headerParams,
            method, uri, params);
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        log.info("requestEnd costTimeMillis={},requestParams:{},backValue={}",
            System.currentTimeMillis() - startTimeMillis, params, JSON.toJSONString(result));
		return result;
	}

	private String getHeaderParams(HttpServletRequest request, Enumeration<String> headerFieldName) {
		StringBuilder params = new StringBuilder();
		while (headerFieldName.hasMoreElements()) {
			String fieldName = headerFieldName.nextElement();
			String value = String.valueOf(request.getAttribute(fieldName));
			if (StringUtils.isNotBlank(value)&&!"null".equals(value)) {
				if (StringUtils.isNotEmpty(params.toString())) {
					params.append(",");
				}
				params.append("'").append(fieldName).append("':").append(value);
			}
		}
		return params.toString();
	}
	
	private String getParams(MethodSignature methodSignature,Object[] args) {
		String[] argNames = methodSignature.getParameterNames();
		StringBuilder params = new StringBuilder();
		for(int i=0;i<argNames.length;i++) {
			if (StringUtils.isNotEmpty(params.toString())) {
				params.append(",");
			}
			params.append("\"").append(argNames[i]).append("\":");
			if(args[i] instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)args[i];
				params.append(JSONArray.toJSONString(bindingResult.getAllErrors()));
			} else {
				params.append(JSON.toJSONString(args[i]));
			}
		}
		return params.toString();
	}

}
