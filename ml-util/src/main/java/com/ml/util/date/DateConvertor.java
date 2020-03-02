package com.ml.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 日期格转换工具类
 * 
 * 描述:实现Date,String,long之间转换
 * </p>
 * 
 * @author metanoia.lang 2012-4-25
 * @author mecarlen 2014-5-16 上午10:31:19
 */
public class DateConvertor {
    
    /**
     * <p>
     * String转换成Date
     * 
     * 描述:方法自动匹配格式,格式不匹配抛异常
     * </p>
     * 
     * @param dateStr
     *            String 格式化后的日期字符串
     * @return Date dateStr==null || StringUtils.isBlank(dateStr)?null:Date
     */
    final public static Date str2Date(final String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        for (EnumDateFormat format : EnumDateFormat.values()) {
            Pattern pattern = Pattern.compile(format.pattern(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(dateStr);
            if (matcher.find()) {
                return str2Date(dateStr, format);
            }
        }
        return null;
    }

    /**
     * <p>
     * String转换成Date
     * 
     * 描述:提定格式转换,格式不匹配抛异常
     * </p>
     * 
     * @param dateStr
     *            String 格式化后的日期字符串
     * @param format
     *            EnumDateFormat 日期格式
     * @return Date dateStr==null || StringUtils.isBlank(dateStr)? null:Date
     */
    final public static Date str2Date(String dateStr, final EnumDateFormat format) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        // 正则未通过时，返回null;
        Pattern pattern = Pattern.compile(format.pattern(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(dateStr);
        if (!matcher.find()) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.value());
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * <p>
     * String转换成long(毫秒时间)
     * 
     * 描述:方法自动匹配格式,格式不匹配抛异常
     * </p>
     * 
     * @param dateStr
     *            String 格式化的日期字符串
     * @return long null==dateStr || StringUtils.isBlank(dateStr)?-1l:long
     */
    final public static long str2Long(final String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return -1L;
        }
        Date date = str2Date(dateStr);
        if (null != date) {
            return date.getTime();
        } else {
            return -1L;
        }
    }

    /**
     * <p>
     * String转换成long(毫秒时间)
     * 
     * 描述:指定格式转换,格式不匹配抛异常
     * </p>
     * 
     * @param dateStr
     *            String 格式化的日期字符串
     * @param format
     *            EnumDateFormat 日期格式枚举
     * @return long null==dateStr || StringUtils.isBlank(dateStr)?-1l:long
     */
    final public static long str2Long(final String dateStr, final EnumDateFormat format) {
        if (StringUtils.isBlank(dateStr)) {
            return -1L;
        }
        Date date = str2Date(dateStr, format);
        if (null != date) {
            return str2Date(dateStr, format).getTime();
        } else {
            return -1L;
        }
    }

    /**
     * <p>
     * Date转换成String
     * 
     * </p>
     * 
     * @param date
     *            Date 日期
     * @param format
     *            EnumDateFormat 日期格式枚举
     * @param String
     *            null==date?"":格式化的日期字符串
     */
    final public static String date2Str(final Date date, final EnumDateFormat format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.value());
        return simpleDateFormat.format(date);
    }

    /**
     * <p>
     * Date转换成long(毫秒时间)
     * 
     * </p>
     * 
     * @param date
     *            Date 日期
     * @return long null==date?-1:long
     */
    final public static long date2long(final Date date) {
        return null == date ? -1L : date.getTime();
    }

    /**
     * <p>
     * long转换成Date
     * 
     * </p>
     * 
     * @param milliseconds
     *            long 毫秒时间
     * @return Date
     */
    final public static Date long2Date(final long milliseconds) {
        return new Date(milliseconds);
    }

    /**
     * <p>
     * long转换成Str
     * 
     * </p>
     * 
     * @param milliseconds
     *            long 毫秒时间
     * @param format
     *            EnumDateFormat 日期格式枚举
     * @return String 格式化的日期字符串
     */
    final public static String long2Str(final long milliseconds, final EnumDateFormat format) {
        return date2Str(long2Date(milliseconds), format);
    }
}
