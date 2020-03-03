package com.ml.util.phone;

/**
 * 号码类型枚举.
 *
 * @author yansongda
 * @version 2019/8/13 下午1:27
 */
public enum EnumPhoneType {
    /* 手机 */
    MOBILE("MOBILE"),

    /* 固定电话 */
    TEL("TEL"),

    /* 国际长途 */
    IDD("IDD"),

    /* 服务号码 */
    SERVICE("SERVICE");

    private final String name;

    EnumPhoneType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return name;
    }
}
