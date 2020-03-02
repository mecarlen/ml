package com.ml.util.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * <pre>
 * 日期运算工具
 * 
 * </pre>
 * 
 * @author metanoia.lang 2012-4-25
 * @author mecarlen 2014-6-6 下午2:35:52
 */
public class DateUtils {
	/**
	 * <p>
	 * 根据日历的规则，为基准时间添加指定日历字段的时间量
	 * </p>
	 * 
	 * @param fiducialDate
	 *            Date
	 * @param calendarField
	 *            EnumCalendarField 偏移量维度 如 年 季 月
	 * @param offset
	 *            int calendarField偏移量
	 * @return Date fiducialDate==null return null
	 */
	public static Date add(Date fiducialDate, EnumCalendarField calendarField,
			int offset) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fiducialDate);
		cal.add(calendarField.value(), offset);
		return cal.getTime();
	}

	/**
	 * <p>
	 * 根据日历的规则，为基准时间添加指定日历字段的时间量
	 * </p>
	 * 
	 * @param fiducialDate
	 *            String
	 * @param format
	 *            EnumDateFormat
	 * @param calendarField
	 *            EnumCalendarField 偏移量维度 如 年 季 月
	 * @param offset
	 *            int calendarField偏移量
	 * @return String fiducialDate=null renturn null
	 */
	public static String add(String fiducialDate, EnumDateFormat format,
			EnumCalendarField calendarField, int offset) {
		if (null == fiducialDate) {
			return null;
		}

		return DateConvertor.date2Str(DateUtils.add(
				DateConvertor.str2Date(fiducialDate, format), calendarField,
				offset), format);
	}

	/**
	 * <p>
	 * 计算相差天数 eDate-sDate
	 * </p>
	 * 
	 * NOTE:计算时是按毫秒差比较 如相差20h，返回0天
	 * 
	 * @param sDate
	 *            Date
	 * @param eDate
	 *            Date
	 * @return int 相差天数
	 * @throws DateUtilsException
	 */
	public static int compareToDay(Date sDate, Date eDate)
			throws DateUtilsException {
		if (null == eDate || null == sDate) {
			throw new DateUtilsException(
					"DateUtil.compareToDay() fDate is null or sDate is null");
		}

		Long comp = (eDate.getTime() - sDate.getTime()) / 86400000;
		return comp.intValue();
	}

	/**
	 * <p>
	 * 计算相差天数 eDate-sDate
	 * </p>
	 * 
	 * NOTE:计算时是按毫秒差比较 如相差20h，返回0天
	 * 
	 * @param sDate
	 *            String 类似"yyyy-MM-dd"
	 * @param eDate
	 *            String 类似"yyyy-MM-dd"
	 * @return int 相差天数
	 * @throws DateUtilsException
	 */
	public static int compareToDay(String sDate, String eDate)
			throws DateUtilsException {
		if (null == sDate || null == eDate || sDate.isEmpty()
				|| eDate.isEmpty()) {
			throw new DateUtilsException(
					"DateUtil.compareToDay() fDate is null or sDate is null");
		}

		return DateUtils.compareToDay(DateConvertor.str2Date(sDate),
				DateConvertor.str2Date(eDate));
	}

	/**
	 * <p>
	 * 计算相差天数 eDate-sDate
	 * </p>
	 * 
	 * NOTE:计算时是按毫秒差比较 如相差20h，返回0天
	 * 
	 * @param sDate
	 *            String
	 * @param eDate
	 *            String
	 * @param format
	 *            EnumDateFormat
	 * @return int 相差天数
	 * @throws DateUtilsException
	 */
	public static int compareToDay(String sDate, String eDate,
			EnumDateFormat format) throws DateUtilsException {
		if (null == sDate || null == eDate || sDate.isEmpty()
				|| eDate.isEmpty()) {
			throw new DateUtilsException(
					"DateUtil.compareToDay() fDate is null or sDate is null");
		}

		return DateUtils.compareToDay(DateConvertor.str2Date(sDate, format),
				DateConvertor.str2Date(eDate, format));
	}

	/**
	 * <p>
	 * 计算毫秒差 eDate-sDate
	 * </p>
	 * 
	 * @param sDate
	 *            Date
	 * @param eDate
	 *            Date
	 * @return long milliseconds
	 * @throws DateUtilsException
	 */
	public static long compareToMilliseconds(Date sDate, Date eDate)
			throws DateUtilsException {
		if (null == sDate || null == eDate) {
			throw new DateUtilsException(
					"DateUtil.compareToMilliseconds() fDate is null or sDate is null");
		}

		return eDate.getTime() - sDate.getTime();
	}
	
	/**
	 * <p>
	 * 计算秒钟差 eDate-sDate
	 * </p>
	 * @param sDate Date
	 * @param eDate Date
	 * @return long seconds
	 * @throws DateUtilsException
	 * */
	public static long compareToSeconds(Date sDate,Date eDate) throws DateUtilsException{
		if (null == sDate || null == eDate) {
			throw new DateUtilsException(
					"DateUtil.compareToSeconds() fDate is null or sDate is null");
		}
		long milliseconds=compareToMilliseconds(sDate,eDate);
		return milliseconds/1000;
	}
	
	/**
	 * <p>
	 * 计算分种差 eDate-sDate
	 * </p>
	 * @param sDate Date
	 * @param eDate Date
	 * @return long minutes
	 * @throws DateUtilsException
	 * */
	public static long compareToMinutes(Date sDate,Date eDate) throws DateUtilsException{
		if (null == sDate || null == eDate) {
			throw new DateUtilsException(
					"DateUtil.compareToMinutes() fDate is null or sDate is null");
		}
		long seconds=compareToSeconds(sDate,eDate);
		
		return seconds/60;
	}
	
	/**
	 * <p>
	 * 当前时间与时间段比较,判断当前时间与时间段的关系
	 * </p>
	 * 
	 * NOTE:计算时精确到毫秒
	 * 
	 * @param curDate
	 *            Date 当前时间
	 * @param startDate
	 *            Date 时间段开始时间
	 * @param endDate
	 *            Date 时间段结束时间
	 * @return DateCompare curDate==null || startDate==null || endDate==null
	 *         renturn null
	 * @throws DateUtilsException
	 */
	public static EnumCompareResult compareToPeriod(Date curDate,
			Date startDate, Date endDate) throws DateUtilsException {
		if (curDate == null && startDate == null && endDate == null) {
			return null;
		}

		if (DateUtils.compareToMilliseconds(startDate, curDate) < 0) {
			return EnumCompareResult.BEFORE_SDATE;
		} else if (DateUtils.compareToMilliseconds(startDate, curDate) >= 0
				&& DateUtils.compareToMilliseconds(endDate, curDate) <= 0) {
			return EnumCompareResult.IN_PERIOD;
		} else {
			return EnumCompareResult.AFTER_EDATE;
		}
	}

	/**
	 * <p>
	 * 指定时间段内有几个星期几
	 * </p>
	 * 
	 * <p>
	 * NOTE:计算时是按毫秒差比较 如相差20h，视为相差0天
	 * </p>
	 * 
	 * @param sDate
	 *            Date 时间段启点
	 * @param eDate
	 *            Date 时间段止点
	 * @param day
	 *            Day 要计算的星期几
	 * @return int 包含的个数
	 * @throws DateUtilsException
	 */
	public static int countDayOfWeek(Date sDate, Date eDate, EnumDay day)
			throws DateUtilsException {
		if (null == sDate || null == eDate) {
			throw new DateUtilsException(
					"DateUtil.countDayOfWeek() sDate is null or eDate is null");
		}

		int interval = DateUtils.compareToDay(sDate, eDate);
		int count = interval / 7;
		int mo = interval % 7;

		if (mo > 0) {
			Date lastDay = DateUtils.lastDayOfWeek(sDate, 0, day);
			int min = DateUtils.compareToDay(sDate, lastDay) + 1;

			if (min < mo) {
				count++;
			}
		}

		return count;
	}

	/**
	 * <p>
	 * 指定时间段内有几个dayOfWork
	 * </p>
	 * 
	 * <p>
	 * NOTE:计算时是按毫秒差比较 如相差20h，视为相差0天
	 * </p>
	 * 
	 * @param sDate
	 *            String 时间段启点
	 * @param eDate
	 *            String 时间段止点
	 * @param day
	 *            Day 星期几
	 * @param format
	 *            EnumDateFormat
	 * @return int dayOfWork个数
	 * @throws DateUtilsException
	 */
	public static int countDayOfWeek(String sDate, String eDate, EnumDay day,
			EnumDateFormat format) throws DateUtilsException {
		if (null == sDate || null == eDate || sDate.isEmpty()
				|| eDate.isEmpty()) {
			throw new DateUtilsException(
					"DateUtil.countDayOfWeek() sDate is null or eDate is null");
		}

		return DateUtils.countDayOfWeek(DateConvertor.str2Date(sDate, format),
				DateConvertor.str2Date(eDate, format), day);
	}

	/**
	 * <p>
	 * 计算时间段内的天数 eDate-sDate，刨除 指定日期和星期几
	 * </p>
	 * 
	 * <p>
	 * NOTE:计算时是按毫秒差比较 如相差20h，视为相差0天
	 * </p>
	 * 
	 * @param sDate
	 *            Date
	 * @param eDate
	 *            Date
	 * @param woDaySet
	 *            Set<String> 需要刨除的日期
	 * @param woDayOfWeekSet
	 *            Set<Integer> 需要刨除的星期几 例 Calendar.Monday
	 * @return int 天数
	 * @throws DateUtilsException
	 */
	public static int countDayWithoutDay(Date sDate, Date eDate,
			Set<String> woDaySet, Set<EnumDay> woDayOfWeekSet)
			throws DateUtilsException {
		if (null == sDate || null == eDate) {
			throw new DateUtilsException(
					"DateUtil.countDayWithoutDay() sDate is null or eDate is null");
		}

		int countWithout = 0;
		if (null != woDayOfWeekSet) {
			for (EnumDay woDayOfWeek : woDayOfWeekSet) {
				countWithout += DateUtils.countDayOfWeek(sDate, eDate, woDayOfWeek);
			}
		}

		if (null != woDaySet) {
			for (String woDay : woDaySet) {
				Date woDate = DateConvertor.str2Date(woDay);
				if (EnumCompareResult.IN_PERIOD == DateUtils.compareToPeriod(
						woDate, sDate, eDate)
						&& !woDayOfWeekSet.contains(DateUtils.dayOfWeek(woDate))) {
					countWithout++;
				}
			}
		}

		return DateUtils.compareToDay(sDate, eDate) - countWithout + 1;
	}

	/**
	 * <p>
	 * 计算时间段内的天数 eDate-sDate，刨除星期几 whithoutDay
	 * </p>
	 * 
	 * <p>
	 * NOTE:计算时是按毫秒差比较 如相差20h，视为相差0天
	 * </p>
	 * 
	 * @param sDate
	 *            Date
	 * @param eDate
	 *            Date
	 * @param woDayOfWeek
	 *            Day... 星期几
	 * @return int 天数
	 * @throws DateUtilsException
	 */
	public static int countDayWithoutDayOfWeek(Date sDate, Date eDate,
			EnumDay... woDayOfWeek) throws DateUtilsException {
		if (null == sDate || null == eDate) {
			throw new DateUtilsException(
					"DateUtil.countDayWithoutDayOfWeek() sDate is null or eDate is null");
		}

		int countWithout = 0;

		for (EnumDay wod : woDayOfWeek) {
			countWithout += DateUtils.countDayOfWeek(sDate, eDate, wod);
		}

		return DateUtils.compareToDay(sDate, eDate) - countWithout + 1;
	}

	/**
	 * <p>
	 * 计算时间段内的天数 eDate-sDate，刨除周六日
	 * </p>
	 * 
	 * <p>
	 * NOTE:计算时是按毫秒差比较 如相差20h，视为相差0天
	 * </p>
	 * 
	 * @param sDate
	 *            Date
	 * @param eDate
	 *            Date
	 * @return int 天数
	 * @throws DateUtilsException
	 */
	public static int countDayWithoutWeekend(Date sDate, Date eDate)
			throws DateUtilsException {
		if (null == sDate || null == eDate) {
			throw new DateUtilsException(
					"DateUtil.countDayWithoutWeekend() sDate is null or eDate is null");
		}

		return DateUtils.countDayWithoutDayOfWeek(sDate, eDate,
				EnumDay.SATURDAY, EnumDay.SUNDAY);
	}

	/**
	 * <p>
	 * 获取指定格式系统时间
	 * </p>
	 * 
	 * @param format
	 *            EnumDateFormat
	 * @return String
	 */
	public static String curDate(EnumDateFormat format) {
		return DateConvertor.date2Str(new Date(), format);
	}

	/**
	 * <p>
	 * 获取系统试驾日 格式："yyyy-MM-dd"
	 * </p>
	 * 
	 * @return String "yyyy-MM-dd"
	 */
	public static String curDay() {
		return DateConvertor.date2Str(new Date(), EnumDateFormat.DATE_YMD);
	}

	/**
	 * <p>
	 * 获取系统时间秒 格式:"yyyy-MM-dd HH:mm:ss"
	 * </p>
	 * 
	 * @return String "yyyy-MM-dd HH:mm:ss"
	 */
	public static String curSeconds() {
		return DateConvertor.date2Str(new Date(), EnumDateFormat.TIME_YMDHMS);
	}

	/**
	 * <p>
	 * 获取系统时间是第几周，按周第一天是MONDAY计算
	 * </p>
	 * 
	 * @return int WeekOfYear 当年的第几周
	 * @throws DateUtilsException
	 */
	public static int curWeekOfYear() throws DateUtilsException {
		return weekOfYear(new Date(), EnumDay.MONDAY);
	}

	/**
	 * <p>
	 * 获取日期是星期几
	 * </p>
	 * 
	 * @param date
	 *            Date
	 * @return EnumDay
	 */
	public static EnumDay dayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		switch (cal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			return EnumDay.MONDAY;
		case Calendar.TUESDAY:
			return EnumDay.TUESDAY;
		case Calendar.WEDNESDAY:
			return EnumDay.WEDNESDAY;
		case Calendar.THURSDAY:
			return EnumDay.THURSDAY;
		case Calendar.FRIDAY:
			return EnumDay.FRIDAY;
		case Calendar.SATURDAY:
			return EnumDay.SATURDAY;
		default:
			return EnumDay.SUNDAY;
		}
	}
	
	/**
     * <p>
     * 获取当前是星期几
     * 
     * </p>
     * 
     * @return EnumDay
     */
	public static EnumDay currDayOfWeek() {
	    return dayOfWeek(new Date());
	}

	/**
	 * <P>
	 * 获取指定时间范围的第一天，如果是周范围，按周第一天是MONDAY计算
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param scope
	 *            EnumDateScope
	 * @param offset
	 *            int 对应scope的偏移量 比如 季，1-增加一季
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date firstDate(Date fiducialDate, EnumDateScope scope,
			int offset) {
		if (fiducialDate == null) {
			return null;
		}

		switch (scope) {
		case YEAR:
			return firstDayOfYear(fiducialDate, offset);
		case QUARTER:
			return firstDayOfQuarter(fiducialDate, offset);
		case MONTH:
			return firstDayOfMonth(fiducialDate, offset);
		case WEEK:
			return firstDayOfWeek(fiducialDate, offset);
		case DAY:
			return add(fiducialDate, EnumCalendarField.DAY_OF_YEAR, offset);
		default:
			return fiducialDate;
		}
	}

	/**
	 * <P>
	 * 获取指定时间范围的第一天，如果是周范围，按周第一天是MONDAY计算。格式:"yyyy-MM-dd"
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date
	 * @param scope
	 *            EnumDateScope
	 * @param offset
	 *            int field的偏移量
	 * @param format
	 *            EnumDateFormat
	 * @return String fiducialDate==null renturn null
	 */
	public static String firstDateStr(Date fiducialDate, EnumDateScope scope,
			int offset, EnumDateFormat format) {
		return DateConvertor.date2Str(firstDate(fiducialDate, scope, offset),
				format);
	}

	/**
	 * <P>
	 * 获取月的第一天
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 月的偏移量
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date firstDayOfMonth(Date fiducialDate, int offset) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fiducialDate);
		cal.add(Calendar.MONTH, offset);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * <p>
	 * 获取季度的第一天
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 季度的偏移量
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date firstDayOfQuarter(Date fiducialDate, int offset) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fiducialDate);
		cal.add(Calendar.MONTH, offset * 3);
		int mon = cal.get(Calendar.MONTH);
		if (mon >= Calendar.JANUARY && mon <= Calendar.MARCH) {
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		if (mon >= Calendar.APRIL && mon <= Calendar.JUNE) {
			cal.set(Calendar.MONTH, Calendar.APRIL);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		if (mon >= Calendar.JULY && mon <= Calendar.SEPTEMBER) {
			cal.set(Calendar.MONTH, Calendar.JULY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		if (mon >= Calendar.OCTOBER && mon <= Calendar.DECEMBER) {
			cal.set(Calendar.MONTH, Calendar.OCTOBER);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		return cal.getTime();
	}

	/**
	 * <P>
	 * 获取周的第一天(MONDAY)
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 周 的偏移量
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date firstDayOfWeek(Date fiducialDate, int offset) {
		return firstDayOfWeek(fiducialDate, offset, EnumDay.MONDAY);
	}

	/**
	 * <P>
	 * 获取周的第一天，可以指定周的第一天是星期几，根据指定返回周第一天
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 周 的偏移量
	 * @param firstDayOfWeek
	 *            Day 指定周的第一天是星期几
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date firstDayOfWeek(Date fiducialDate, int offset,
			EnumDay firstDayOfWeek) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(firstDayOfWeek.value());
		cal.setTime(fiducialDate);
		cal.add(Calendar.DAY_OF_MONTH, offset * 7);
		cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek.value());
		return cal.getTime();
	}

	/**
	 * <p>
	 * 获取年的第一天
	 * </p>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 年的偏移量
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date firstDayOfYear(Date fiducialDate, int offset) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fiducialDate);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + offset);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * <P>
	 * 获取指定时间范围的最后一天，如果是周范围，按周第一天是MONDAY计算
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date
	 * @param scope
	 *            EnumDateScope
	 * @param offset
	 *            int field的偏移量
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date lastDate(Date fiducialDate, EnumDateScope scope,
			int offset) {
		if (fiducialDate == null) {
			return null;
		}

		switch (scope) {
		case YEAR:
			return lastDayOfYear(fiducialDate, offset);
		case QUARTER:
			return lastDayOfQuarter(fiducialDate, offset);
		case MONTH:
			return lastDayOfMonth(fiducialDate, offset);
		case WEEK:
			return lastDayOfWeek(fiducialDate, offset);
		case DAY:
			return add(fiducialDate, EnumCalendarField.DAY_OF_YEAR, offset);
		default:
			return fiducialDate;
		}
	}

	/**
	 * <p>
	 * 获取指定时间范围的最后一天，如果是周范围，按周第一天是MONDAY计算。格式:"yyyy-MM-dd"
	 * </p>
	 * 
	 * @param fiducialDate
	 *            Date
	 * @param scope
	 *            EnumDateScope
	 * @param offset
	 *            int 偏移量
	 * @param format
	 *            EnumDateFormat
	 * @return String fiducialDate==null renturn null
	 */
	public static String lastDateStr(Date fiducialDate, EnumDateScope scope,
			int offset, EnumDateFormat format) {
		return DateConvertor.date2Str(lastDate(fiducialDate, scope, offset),
				format);
	}

	/**
	 * <P>
	 * 获取月的最后一天
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 月的偏移量
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date lastDayOfMonth(Date fiducialDate, int offset) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fiducialDate);
		cal.add(Calendar.MONTH, offset + 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	/**
	 * <P>
	 * 获取季度的最后一天
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 季度的偏移量
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date lastDayOfQuarter(Date fiducialDate, int offset) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fiducialDate);
		cal.add(Calendar.MONTH, offset * 3);
		int mon = cal.get(Calendar.MONTH);
		if (mon >= Calendar.JANUARY && mon <= Calendar.MARCH) {
			cal.set(Calendar.MONTH, Calendar.MARCH);
			cal.set(Calendar.DAY_OF_MONTH, 31);
		}
		if (mon >= Calendar.APRIL && mon <= Calendar.JUNE) {
			cal.set(Calendar.MONTH, Calendar.JUNE);
			cal.set(Calendar.DAY_OF_MONTH, 30);
		}
		if (mon >= Calendar.JULY && mon <= Calendar.SEPTEMBER) {
			cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
			cal.set(Calendar.DAY_OF_MONTH, 30);
		}
		if (mon >= Calendar.OCTOBER && mon <= Calendar.DECEMBER) {
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			cal.set(Calendar.DAY_OF_MONTH, 31);
		}
		return cal.getTime();
	}

	/**
	 * <P>
	 * 获取周的最后一天(SUNDAY)
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 周 的偏移量
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date lastDayOfWeek(Date fiducialDate, int offset) {
		return lastDayOfWeek(fiducialDate, offset, EnumDay.MONDAY);
	}

	/**
	 * <P>
	 * 获取周的最后一天，可以指定周的第一天是星期几，根据指定返回周最后一天
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 周 的偏移量
	 * @param firstDayOfWeek
	 *            Day 指定周的第一天是星期几
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date lastDayOfWeek(Date fiducialDate, int offset,
			EnumDay firstDayOfWeek) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(firstDayOfWeek.value());
		cal.setTime(fiducialDate);
		cal.add(Calendar.DAY_OF_MONTH, offset * 7);
		cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek.value());
		cal.add(Calendar.DAY_OF_MONTH, 6);
		return cal.getTime();
	}

	/**
	 * <P>
	 * 获取年的最后一天
	 * </P>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 年的偏移量
	 * @return Date fiducialDate==null renturn null
	 */
	public static Date lastDayOfYear(Date fiducialDate, int offset) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fiducialDate);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + offset);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		return cal.getTime();
	}

	/**
	 * <p>
	 * 根据日历的规则，为基准时间添加指定日历字段的单个时间单元 <br>
	 * 例:如果 DAY_OF_MONTH 字段为 31，则在 February的范围内滚动会将它设置为 28。
	 * </p>
	 * 
	 * @param fiducialDate
	 *            Date
	 * @param calendarField
	 *            EnumCalendarField 偏移量维度 如 年 季 月
	 * @param offset
	 *            int 偏移量
	 * @return Date fiducialDate==null return null
	 */
	public static Date roll(Date fiducialDate, EnumCalendarField calendarField,
			int offset) {
		if (fiducialDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fiducialDate);
		cal.roll(calendarField.value(), offset);
		return cal.getTime();
	}

	/**
	 * <p>
	 * 获取指定时间是第几周，按指定周第一天是星期几计算
	 * </p>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param firstDayOfWeek
	 *            Day 指定周的第一天是星期几
	 * @return int WeekOfYear
	 * @throws DateUtilsException
	 */
	public static int weekOfYear(Date fiducialDate, EnumDay firstDayOfWeek)
			throws DateUtilsException {
		return DateUtils.weekOfYear(fiducialDate, 0, firstDayOfWeek);
	}

	/**
	 * <p>
	 * 获取指定时间是第几周，按指定周第一天是星期几计算
	 * </p>
	 * 
	 * @param fiducialDate
	 *            Date 基准时间
	 * @param offset
	 *            int 天的偏移量
	 * @param firstDayOfWeek
	 *            Day 指定周的第一天是星期几
	 * @return int WeekOfYear
	 * @throws DateUtilsException
	 */
	public static int weekOfYear(Date fiducialDate, int offset,
			EnumDay firstDayOfWeek) throws DateUtilsException {
		if (fiducialDate == null) {
			throw new DateUtilsException(
					"DateUtil.weekOfYear(fiducialDate,offset,firstDayOfWeek) fiducialDate is null");
		}

		Date date = DateUtils.add(fiducialDate, EnumCalendarField.DAY_OF_MONTH,
				offset);

		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(firstDayOfWeek.value());

		Date firstDay = DateUtils.firstDayOfYear(date, 0);
		Date lastDay = DateUtils.lastDayOfWeek(firstDay, 0, firstDayOfWeek);
		cal.setMinimalDaysInFirstWeek(DateUtils.compareToDay(firstDay, lastDay) + 1);

		cal.setTime(date);

		int week = cal.get(Calendar.WEEK_OF_YEAR);
		return week == 1 && Calendar.DECEMBER == cal.get(Calendar.MONTH) ? week
				+ cal.getActualMaximum(Calendar.WEEK_OF_YEAR) : week;
	}
	
	/**
	 * 判断日期格式是否正确
	 * @param date
	 * @param format
	 * @return true:正确;false:错误;
	 */
	public static boolean checkDate(String date,String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date d = null;
        try{
            d = df.parse(date);
        }catch(Exception e){
            //如果不能转换,肯定是错误格式
            return false;
        }
        String s1 = df.format(d);
        // 转换后的日期再转换回String,如果不等,逻辑错误.如format为"yyyy-MM-dd",date为
        // "2006-02-31",转换为日期后再转换回字符串为"2006-03-03",说明格式虽然对,但日期
        // 逻辑上不对.
        return date.equals(s1);
    }

	private DateUtils() {
	}
}
