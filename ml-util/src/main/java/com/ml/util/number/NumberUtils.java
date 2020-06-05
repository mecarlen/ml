package com.ml.util.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * <pre>
 * 数字常用操作工具类
 * 描述:0.20 #0.00 0.20 0.20 ##.## .2(不显示前面和后面的0)
 * </pre>
 * 
 * @author metanoia.lang
 * @author mecarlen 2014-9-26 下午3:03:37
 */
public class NumberUtils {
	/** 默认除法运算精度 */
	private static final int DEF_DIV_SCALE = 10;

	/** 23233.02 */
	public static final String NUMBER_FORMAT_TWODECIMAL = "#0.00";

	/** 23.43% */
	public static final String NUMBER_FORMAT_DECIMALPERCENT = "#0.00%";

	/** 23,233.02 */
	public static final String NUMBER_FORMAT_DECIMALTHREESEPARATED = "#,##0.00";

	/** 23% */
	public static final String NUMBER_FORMAT_PERCENT = "#%";

	/** 23，233 */
	public static final String NUMBER_FORMAT_THREESEPARATED = "#,###";
	/**
     * <p>
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     * </p>
     * 
     * @param v1
     *            double 被除数
     * @param v2
     *            double 除数
     * @return double 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * <p>
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * </p>
     * 
     * @param v1
     *            double 被除数
     * @param v2
     *            double 除数
     * @param scale
     *            int 表示表示需要精确到小数点以后几位。
     * @return double 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * <p>
     * 将Double转换为指定格式的Double 比如12322.233 转换为12,322.23
     * </p>
     * 
     * @param doubleNum
     *            Double 要转换的数字
     * @param pattern
     *            String 转换的格式（默认是#0.00）
     * @return Double 如果doubleStr is null或者发生异常 返回null
     */
    public static Double formatDoubleToDouble(Double doubleNum, String pattern) {
        return Double.parseDouble(formatNumberToStr(doubleNum, pattern, null));
    }

    /**
     * <p>
     * 数字的格式
     * </p>
     * 
     * @param number
     *            Number 转换的数字
     * @param pattern
     *            String 转换的格式
     * @return String 格式化后的string；如果number==null return null
     */
    public static String formatNumberToStr(Number number, String pattern) {
        return formatNumberToStr(number, pattern, null);
    }

    /**
     * <p>
     * 转换数字的格式
     * </p>
     * 
     * @param number
     *            Number 转换的数字
     * @param pattern
     *            String 转换的格式默认四舍五入保留两位小数
     * @param locale
     *            Locale 指定语言环境
     * @return String 格式化后的string；如果number==null return null
     */
    public static String formatNumberToStr(Number number, String pattern,
            Locale locale) {
        if (number == null) {
            return null;
        }
        DecimalFormat decimalformat = (DecimalFormat) NumberFormat
                .getInstance();
        if (locale != null) {
            decimalformat = (DecimalFormat) NumberFormat.getInstance(locale);
        }
        if (pattern == null) {
            pattern = NUMBER_FORMAT_TWODECIMAL;
        }
        decimalformat.applyPattern(pattern);
        return decimalformat.format(number);
    }

    /**
     * <p>
     * 将String转换成指定格式的Double
     * </p>
     * 
     * @param doubleStr
     *            String
     * @param pattern
     *            String 转换的格式（默认是#0.00）
     * @return Double 如果doubleStr==null或者转换时出现异常 返回null
     */
    public static Double formatStrToDouble(String doubleStr, String pattern) {
        if (doubleStr == null) {
            return null;
        }
        return formatDoubleToDouble(Double.parseDouble(doubleStr), pattern);
    }

    /**
     * <p>
     * 返回数字的货币形式
     * </p>
     * 
     * @param number
     *            Number 转换的数字
     * @param locale
     *            Locale 指定语言环境
     * @return String 保留两位小数，并且四舍五入。如果number==null return null
     */
    public static String getMoneyStr(Number number, Locale locale) {
        if (number == null) {
            return null;
        }
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        if (locale != null) {
            numberFormat = NumberFormat.getCurrencyInstance(locale);
        }
        return numberFormat.format(number);
    }

    /**
     * <p>
     * 判断一个字符串是否是数字
     * </p>
     * 
     * @param numStr
     *            String 给出的字符串
     * @return boolean true-是数字; false-不是数字
     */
    public static boolean isNum(String numStr) {
        String regx = "(-?\\d+)(\\.\\d+)*";
        return (numStr == null || numStr.length() == 0 || numStr.matches(regx)) ? true
                : false;
    }

    /**
     * <p>
     * 提供精确的乘法运算。
     * </p>
     * 
     * @param v1
     *            double 被乘数
     * @param v2
     *            double 乘数
     * @return double 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * <p>
     * 返回double类型数据的负数
     * </p>
     * 
     * @param doubleNum
     *            Double 指定的Double
     * @return double
     */
    public static double negDouble(double doubleNum) {
        return sub(0.0, doubleNum);
    }

    /**
     * <p>
     * 提供精确的减法运算。
     * </p>
     * 
     * @param v1
     *            double 被减数
     * @param v2
     *            double 减数
     * @return double 两个参数的差
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    private NumberUtils() {
    }
}
