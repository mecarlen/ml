package com.ml.util.date;

/**
 * <pre>
 * 日期比较结果枚举
 * 
 * BEFORE_SDATE 当前时间在比较时间段之前
 * IN_PERIOD 当前时间在比较时间段内
 * AFTER_EDATE 当前时间在比较时间段之后
 * </pre>
 * 
 * @author metanoia.lang 2012-4-26
 * @author mecarlen 2014-6-6 下午3:20:10
 */
public enum EnumCompareResult {
	/** 当前时间在比较时间段之前 */
	BEFORE_SDATE,
	/** 当前时间在比较时间段内 */
	IN_PERIOD,
	/** 当前时间在比较时间段之后 */
	AFTER_EDATE;
}
