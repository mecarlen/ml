package com.ml.util.phone;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

/**
 * 号码工具类.
 *
 * @author yansongda
 * @version 2019/8/13 下午1:27
 */
public class PhoneUtils {
    /**
     * 国际长途前缀.
     */
    public static final String PREFIX_IDD = "00";

    /**
     * 外地号加 0.
     */
    public static final String PREFIX_OUTLAND = "0";

    /**
     * 中国编码.
     */
    public static final String PREFIX_CHINA = "86";

    /**
     * +.
     */
    public static final String PREFIX_PLUS = "+";

    /**
     * 号段位数.
     */
    public static final int NUMBER_SEGMENT = 7;

    private static final Map<String, String> VENDOR;

    /**
     * <pre>
     * 重点城市区号前两位
     * 1、首都 01
     * 2、直辖市 02
     * </pre>
     */
    public static final List<String> IMPCITY_AREACODE_PREFIXS;

    /**
     * <pre>
     * 其它城市区号前两位
     * 03~09
     * </pre>
     */
    public static final List<String> OTHERCITY_AREACODE_PREFIXS;

    static {
        VENDOR = new HashMap<>();

        VENDOR.put("130", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("131", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("132", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("133", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("134", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("1349", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("135", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("136", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("137", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("138", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("139", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("145", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("146", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("147", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("148", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("149", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("150", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("151", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("152", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("153", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("155", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("156", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("157", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("158", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("159", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("162", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("165", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("166", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("167", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("1700", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("1701", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("1702", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("1703", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("1704", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("1705", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("1706", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("1707", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("1708", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("1709", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("171", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("172", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("173", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("1740", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("175", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("176", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("177", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("178", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("180", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("181", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("182", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("183", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("184", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("185", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("186", EnumMobileVendor.UNICOM.getValue());
        VENDOR.put("187", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("188", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("189", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("191", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("193", EnumMobileVendor.TELECOM.getValue());
        VENDOR.put("195", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("198", EnumMobileVendor.MOBILE.getValue());
        VENDOR.put("199", EnumMobileVendor.TELECOM.getValue());
        // 首都、直辖市区号前缀
        IMPCITY_AREACODE_PREFIXS = Arrays.asList(new String[] {"01", "02"});
        // 其它城市区号前缀
        OTHERCITY_AREACODE_PREFIXS = Arrays.asList(new String[] {"03", "04", "05", "06", "07", "08", "09"});
    }

    /**
     * 是否为号码.
     *
     * @param phone
     *            号码
     *
     * @return boolean
     */
    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile(EnumPhoneFormat.PHONE.getPattern(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);

        return matcher.find();
    }

    /**
     * 是否为手机号.
     *
     * @param mobile
     *            手机号码
     *
     * @return boolean
     */
    public static boolean isMobile(String mobile) {
        // 正常手机号判断
        Pattern pattern = Pattern.compile(EnumPhoneFormat.MOBILE.getPattern(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mobile);
//        if(matcher.find()) {
//            return true;
//        } else if (mobile.length() >= 14) {
//            String areaCodePrefix = mobile.substring(0, 2);
//            String phone = mobile;
//            if (PhoneUtils.IMPCITY_AREACODE_PREFIXS.contains(areaCodePrefix)) {
//                // 首都和直辖市为3位区号
//                phone = mobile.substring(3);
//            } else if (PhoneUtils.OTHERCITY_AREACODE_PREFIXS.contains(areaCodePrefix)) {
//                // 其它城市为4位区号
//                phone = mobile.substring(4);
//            }
//            Matcher phoneMatcher = pattern.matcher(phone);
//            return phoneMatcher.find();
//        }
        return matcher.find();
    }

    /**
     * 是否为服务号码.
     *
     * @param phone
     *            号码
     *
     * @return boolean
     */
    public static boolean isService(String phone) {
        Pattern pattern = Pattern.compile(EnumPhoneFormat.SERVICE.getPattern(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);

        return matcher.find();
    }

    /**
     * <pre>
     * 是否为国际长途.
     * 
     * 描述
     *  1、00开头
     *  2、以+开头，但非+86的
     * </pre>
     * 
     * @param phone
     *            号码
     *
     * @return boolean
     */
    public static boolean isIdd(String phone) {
        if (phone.startsWith("00")) {
            // 00开头为国际长途
            return true;
        }
        if (phone.startsWith("+") && !phone.startsWith("+86")) {
            // 非+86开头的为国际长途
            return true;
        }
        return false;
    }

    /**
     * 获取号码的运营商.
     *
     * @param mobile
     *            手机号码
     *
     * @return 运营商
     */
    public static String getMobileVendor(String mobile) {
        String prefix4 = mobile.substring(0, 4);
        String prefix3 = mobile.substring(0, 3);

        return VENDOR.getOrDefault(prefix4, VENDOR.getOrDefault(prefix3, ""));
    }

    /**
     * 优化手机号码格式，将多余的前缀去掉.
     *
     * @param mobile
     *            待优化的手机号码
     *
     * @return string
     */
    public static String prettyMobileNumber(String mobile) {
        
        if (PREFIX_PLUS.equals(mobile.substring(0, 1))) {
            mobile = mobile.substring(1);
        }

        if (PREFIX_CHINA.equals(mobile.substring(0, 2))) {
            mobile = mobile.substring(2);
        }

        if (PREFIX_OUTLAND.equals(mobile.substring(0, 1))) {
            mobile = mobile.substring(1);
        }
        return mobile;
    }
    
    public static String prettyStartwithAreaCodeMobileNumber(String mobile) {
        if(StringUtils.isBlank(mobile)||mobile.length()<14) {
            return mobile;
        }
        String areaCodePrefix = mobile.substring(0, 2);
        String number = mobile;
        if (PhoneUtils.IMPCITY_AREACODE_PREFIXS.contains(areaCodePrefix)) {
            // 首都和直辖市为3位区号
            number = mobile.substring(3);
        } else if (PhoneUtils.OTHERCITY_AREACODE_PREFIXS.contains(areaCodePrefix)) {
            // 其它城市为4位区号
            number = mobile.substring(4);
        }
        return number;
    }

    /**
     * 获取固定电话区号.
     *
     * @param phone
     *            电话号码
     *
     * @return code
     */
    @Nullable
    public static String getTelCode(String phone) {
        String code = "";

        if (phone.length() < 2) {
            return null;
        }

        if ("0".equals(phone.substring(0, 1))) {
            code = phone.substring(0, Math.min(phone.length(), 4));
        }

        if ("01".equals(phone.substring(0, 2)) || "02".equals(phone.substring(0, 2))) {
            code = phone.substring(0, Math.min(phone.length(), 3));
        }

        return code;
    }
}
