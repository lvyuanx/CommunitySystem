package com.lvyx.commons.enums;

/**
 * <p>
 * 角色类型枚举
 * </p>
 *
 * @author lvyx
 * @date 2021-12-31 09:59:24
 */
public enum RoleTypeEnum {

    /**
     * 社区居民
     * @since 2021/12/31 10:08
     **/
    USER("user", "社区居民"),


    /**
     * 系统管理员
     * @since 2021/12/31 10:08
     **/
    ADMIN("admin", "系统管理员"),

    /**
     * 系统管理员
     * @since 2021/12/31 10:08
     **/
    COMMUNITY_ADMIN("communityAdmin", "社区管理员"),

    /**
     * 用户功能
     * @since 2021/12/31 10:08
     **/
    USER_FUNCTION("userFunction", "用户功能"),

    /**
     * 社区管理员功能
     * @since 2021/12/31 10:08
     **/
    COMMUNITY_FUNCTION("communityFunction", "社区管理员功能"),

    /**
     * 系统管理员功能
     * @since 2021/12/31 10:08
     **/
    ADMIN_FUNCTION("adminFunction", "系统管理员功能"),

    /**
     * 社区管理角色
     * @since 2021/12/31 10:08
     **/
    COMMUNITY("community", "社区管理角色"),

    ;


    private final String value;
    private final String name;

    RoleTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    RoleTypeEnum(String value) {
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