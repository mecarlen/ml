package com.ml.util.phone;

/**
 * 运营商枚举.
 *
 * @author yansongda
 * @version 2019/8/13 下午1:27
 */
public enum EnumMobileVendor {
    /* 移动 */
    MOBILE("mobile"),

    /* 移动 */
    UNICOM("unicom"),

    /* 移动 */
    TELECOM("telecom");

    private final String name;

    EnumMobileVendor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return name;
    }
}
