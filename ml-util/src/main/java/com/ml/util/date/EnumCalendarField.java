package com.ml.util.date;

import java.util.Calendar;

/**
 * <pre>
 * Calendar常用日期属性枚举
 * 
 * DAY_OF_YEAR 一年的第几天 
 * DAY_OF_MONTH 一个月的第几天
 * DAY_OF_WEEK 一个周的第几天
 * WEEK_OF_YEAR 一年的第几周
 * WEEK_OF_MONTH 一个月的第几周
 * YEAR 年
 * MONTH 月
 * MONTHSHORT 简写月
 * DATE 日期
 * HOUR_OF_DAY 一天的第几小时
 * MINUTE 分钟
 * SECOND 秒
 * MILLISECOND 微秒
 * </pre>
 * 
 * @author mecarlen 2014-6-6 下午2:56:02
 */
public enum EnumCalendarField {
	/** 一年的第几天 */
	DAY_OF_YEAR(Calendar.DAY_OF_YEAR), 
	/** 一个月的第几天 */
	DAY_OF_MONTH(Calendar.DAY_OF_MONTH), 
	/** 一个周的第几天 */
	DAY_OF_WEEK(Calendar.DAY_OF_WEEK), 
	/** 一年的第几周 */
	WEEK_OF_YEAR(Calendar.WEEK_OF_YEAR), 
	/** 一个月的第几周 */
	WEEK_OF_MONTH(Calendar.WEEK_OF_MONTH), 
	/** 年 */
	YEAR(Calendar.YEAR), 
	/** 月 */
	MONTH(Calendar.MONTH), 
	/** 简写月 */
	MONTHSHORT(Calendar.SHORT), 
	/** 日期 */
	DATE(Calendar.DATE), 
	/** 一天的第几小时 */
	HOUR_OF_DAY(Calendar.HOUR_OF_DAY), 
	/** 分钟 */
	MINUTE(Calendar.MINUTE), 
	/** 秒 */
	SECOND(Calendar.SECOND), 
	/** 微秒 */
	MILLISECOND(Calendar.MILLISECOND);

	private final int value;

	private EnumCalendarField(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
