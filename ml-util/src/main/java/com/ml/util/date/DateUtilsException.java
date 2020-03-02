package com.ml.util.date;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 日期工具异常
 * 
 * </pre>
 * 
 * @author metanoia.lang 2012-4-26
 * @author mecarlen 2014-6-6 下午2:37:53
 */
public class DateUtilsException extends RuntimeException {
    
    private static final long serialVersionUID = 5605649783208136644L;
	private final static Logger DATE_UTIL_LOGGER = LoggerFactory.getLogger(DateUtilsException.class);

	public DateUtilsException() {
		super();
	}

	public DateUtilsException(String message) {
		super(message);
		DATE_UTIL_LOGGER.error(message);
	}

	public DateUtilsException(String message, Throwable cause) {
		super(message, cause);
		DATE_UTIL_LOGGER.error(message, cause);
	}
}
