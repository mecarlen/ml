package com.ml.util.phone;

/**
 * 号码规则枚举.
 *
 * @author yansongda
 * @version 2019/8/13 下午1:27
 */

public enum EnumPhoneFormat {
    /* 号码规则 */
    PHONE("^[\\d+\\-]+$"),

    /* 手机号规则 */
    MOBILE("^(\\+)?(86)?(0)?1[3-9]\\d{9}$"),

    /* 服务号码 */
    SERVICE("^(400|800|95|1010)"),

    /* 国际长途 */
    IDD("^00");

    private final String pattern;

    EnumPhoneFormat(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public String getValue() {
        return pattern;
    }
}
