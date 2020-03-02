package com.ml.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ml.base.query.PageParams;
import com.ml.base.query.PageThreadLocal;

/**
 * <pre>
 * 通用分页栏截器
 * 
 * </pre>
 * 
 * @author mecarlen 2019年2月28日 下午4:45:39
 */
public class PageInterceptor extends HandlerInterceptorAdapter {
	
	protected static Logger PAGE_INTERCEPTOR_LOG = LoggerFactory.getLogger(PageInterceptor.class);
	/** 分页URI匹配 */
	final public static String PAGE_PATH_INDEXOF="/page/";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (-1 != request.getRequestURI().indexOf(PageInterceptor.PAGE_PATH_INDEXOF)) {
			String uri = request.getRequestURI();
			String[] uriSegments = uri.split("/");
			int pageSize = Integer.valueOf(uriSegments[uriSegments.length - 1]);
			int pageNo = Integer.valueOf(uriSegments[uriSegments.length - 2]);
			PageParams<?> pp = new PageParams<>();
			pp.setPageNo(pageNo);
			pp.setPageSize(pageSize);
			PageThreadLocal.setParams(pp);
		}
		
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 将分页信息传至页面
//		modelAndView.addObject("pageParams", PageThreadLocal.getParams());
		PageThreadLocal.clean();
		super.postHandle(request, response, handler, modelAndView);
	}

}
