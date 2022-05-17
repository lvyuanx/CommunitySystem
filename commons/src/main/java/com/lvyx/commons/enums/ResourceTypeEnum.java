package com.lvyx.commons.enums;

/**
 * <p>
 * 资源类型枚举
 * </p>
 *
 * @author lvyx
 * @date 2021-12-31 09:59:24
 */
public enum ResourceTypeEnum {

    /**
     * 功能
     * @since 2021/12/31 10:08
     **/
    FUNCTION("function", "功能"),

    /**
     * 社区
     * @since 2021/12/31 10:08
     **/
    COMMUNITY("community", "社区资源"),


    ;


    private final String value;
    private final String name;

    ResourceTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    ResourceTypeEnum(String value) {
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