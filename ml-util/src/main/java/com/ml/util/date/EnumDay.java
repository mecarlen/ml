package com.ml.util.date;

import java.util.Calendar;

/**
 * <pre>
 * 星期枚举
 * 
 * MONDAY 星期一
 * TUESDAY 星期二
 * WEDNESDAY 星期三
 * THURSDAY 星期四
 * FRIDAY 星期五
 * SATURDAY 星期六
 * SUNDAY 星期日
 * </pre>
 * 
 * @author metanoia.lang 2012-4-26
 * @author mecarlen 2014-6-6 下午2:42:54
 */
public enum EnumDay {
	/** 星期一 */
	MONDAY(Calendar.MONDAY),
	/** 星期二 */
	TUESDAY(Calendar.TUESDAY),
	/** 星期三 */
	WEDNESDAY(Calendar.WEDNESDAY),
	/** 星期四 */
	THURSDAY(Calendar.THURSDAY),
	/** 星期五 */
	FRIDAY(Calendar.FRIDAY),
	/** 星期六 */
	SATURDAY(Calendar.SATURDAY),
	/** 星期日 */
	SUNDAY(Calendar.SUNDAY);

	private final int value;

	private EnumDay(int value) {
		this.value = value;
	}

	/**
	 * <pre>
	 * 取星期中文名称
	 * 
	 * MONDAY 星期一
	 * TUESDAY 星期二
	 * WEDNESDAY 星期三
	 * THURSDAY 星期四
	 * FRIDAY 星期五
	 * SATURDAY 星期六
	 * SUNDAY 星期日
	 * </pre>
	 * */
	public String toname() {
		if (Calendar.MONDAY == value) {
			return "星期一";
		} else if (Calendar.TUESDAY == value) {
			return "星期二";
		} else if (Calendar.WEDNESDAY == value) {
			return "星期三";
		} else if (Calendar.THURSDAY == value) {
			return "星期四";
		} else if (Calendar.FRIDAY == value) {
			return "星期五";
		} else if (Calendar.SATURDAY == value) {
			return "星期六";
		} else if (Calendar.SUNDAY == value) {
			return "星期日";
		} else {
			return value + "";
		}
	}

	/**
	 * <pre>
	 * 取星期对应的Calendar值
	 * </pre>
	 * */
	public int value() {
		return value;
	}
}
