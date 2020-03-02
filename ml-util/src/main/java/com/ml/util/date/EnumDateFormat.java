package com.ml.util.date;

/**
 * <pre>
 * 日期格式枚举
 * 
 * </pre>
 * 
 * @author metanoia.lang 2012-4-25
 * @author mecarlen 2014-5-16 上午10:12:18
 * */
public enum EnumDateFormat {
	/** yyyy eg:1970 */
	YEAR("yyyy", "^[0-9]{4}$"),
	/** yyyy-MM eg:1970-01 */
	MONTH_YM("yyyy-MM", "^[0-9]{4}-[0-9]{1,2}$"),
	/** yyyyMM eg:197001 */
	MONTH_YM_S("yyyyMM", "^[0-9]{4}[0-9]{1,2}$"),
	/** yyyy/MM eg:1970/01 */
	MONTH_YM_T("yyyy/MM","^[0-9]{4}/[0-9]{1,2}$"),
	/** MM-dd-yyyy eg:01-18-1970 */
	DATE_MDY("MM-dd-yyyy", "^[0-9]{1,2}-[0-9]{1,2}-[0-9]{4}$"),
	/** yyyy-MM-dd eg:1970-01-18 */
	DATE_YMD("yyyy-MM-dd", "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$"),
	/** yyyyMMdd eg:19700118 */
	DATE_YMD_S("yyyyMMdd", "^[0-9]{4}[0-9]{1,2}[0-9]{1,2}$"),
	/** yyyy/MM/dd eg:1970/01/18 */
	DATE_YMD_T("yyyy/MM/dd", "^[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}$"),
	/** yyyy-MM-dd HH eg:1970-01-18 10 */
	TIME_YMDH("yyyy-MM-dd HH", "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}$"),
	/** yyyyMMddHH eg:1970011810 */
	TIME_YMDH_S("yyyyMMddHH", "^[0-9]{4}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}$"),
	/** yyyy-MM-dd HH:mm eg:1970-01-18 10:00 */
	TIME_YMDHM("yyyy-MM-dd HH:mm",
			"^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}$"),
	/** yyyyMMddHHmm eg:197001181000 */
	TIME_YMDHM_S("yyyyMMddHHmm",
			"^[0-9]{4}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}$"),
	/** yyyy-MM-dd HH:mm:ss eg:1970-01-18 10:00:58 */
	TIME_YMDHMS("yyyy-MM-dd HH:mm:ss",
			"^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}$"),
	/** yyyyMMddHHmmss eg:19700118100058 */
	TIME_YMDHMS_S("yyyyMMddHHmmss",
			"^[0-9]{4}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}$"),
	/** yyyy-MM-dd HH:mm:ss:SSS eg:1970-01-18 10:00:58:986 */
	TIME_YMDHMSSS("yyyy-MM-dd HH:mm:ss:SSS",
			"^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}:[0-9]{3}$"),
	/** yyyyMMddHHmmssSSS eg:19700118100058986 */
	TIME_YMDHMSSS_S("yyyyMMddHHmmssSSS",
			"^[0-9]{4}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}[0-9]{3}$");

	private final String format;
	private final String pattern;

	private EnumDateFormat(String format, String pattern) {
		this.format = format;
		this.pattern = pattern;
	}

	public String value() {
		return format;
	}

	public String pattern() {
		return pattern;
	}

}
