package com.ml.base.query;
/**
 * <pre>
 * 分页参数
 * 
 * </pre>
 * 
 * @author mecarlen 2018年9月30日 下午4:54:16
 */
public class PageThreadLocal {
	private static ThreadLocal<PageParams<?>> PAGE_PARAMS = new ThreadLocal<PageParams<?>>();
	
	public static <T> void setParams(PageParams<T> value) {
		PAGE_PARAMS.set(value);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> PageParams<T> getParams(){
		return (PageParams<T>)PAGE_PARAMS.get();
	}
	
	public static void clean() {
		PAGE_PARAMS.remove();
	}
}
