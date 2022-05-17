package com.lvyx.commons.enums;

/**
 * <p>
 * 二维码颜色枚举
 * </p>
 *
 * @author lvyx
 * @date 2021-12-31 09:59:24
 */
public enum QrCodeColorEnum {

    /**
     * 黄色二维码
     * @since 2021/12/31 10:08
     **/
    YELLOW("yellow", "黄码"),

    /**
     * 绿色二维码
     * @since 2021/12/31 10:08
     **/
    GREEN("green", "绿码"),

    /**
     * 红色二维码
     * @since 2021/12/31 10:08
     **/
    RED("red", "红码"),

    /**
     * 灰色二维码
     * @since 2021/12/31 10:08
     **/
    GRAY("gray", "灰码");


    ;


    private final String value;
    private final String name;

    QrCodeColorEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    QrCodeColorEnum(String value) {
        this.value = value;
        this.name = null;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}