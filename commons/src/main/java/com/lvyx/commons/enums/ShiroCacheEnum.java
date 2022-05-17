package com.lvyx.commons.enums;

/**
 * <p>
 * shiro 缓存枚举
 * </p>
 *
 * @author lvyx
 * @date 2021-12-31 09:59:24
 */
public enum ShiroCacheEnum {

    /**
     * shiro缓存外层组名称
     * @since 2021/12/31 10:08
     **/
    GROUP_CAS(SystemCacheEnum.COMMUNITY_SYSTEM_CACHE.getValue() + "group_shiro:"),

    /**
     * 角色的键
     * @since 2021/12/31 10:11
     **/
    ROLE_KEY("role_key:"),

    /**
     * 资源的键
     * @since 2021/12/31 10:12
     **/
    RESOURCES_KEY("resources_key:"),

    /**
     * 角色ids的键
     * @since 2021/12/31 10:12
     **/
    RESOURCES_KEY_IDS("resources_key_ids:"),

    /**
     * 用户登录名
     * @since 2021/12/31 10:12
     **/
    FIND_USER_BY_LOGINNAME("findUserByLoginName:"),

    /**
     * token
     * @since 2022/1/14 18:02
     **/
    JWT_TOKEN("jwtToken:"),

    /**
     * 密码重试次数
     * @since 2022/1/10 16:31
     **/
    PWD_RETRY_COUNT(GROUP_CAS.getValue() + "pwdRetryCount:"),

    /**
     * 会话id
     * @since 2022/1/12 15:50
     **/
    SESSION_DAO(GROUP_CAS.getValue() + "sessionDao:"),

    /**
     * 用户队列
     * @since 2022/1/12 15:50
     **/
    USER_QUEUE(GROUP_CAS.getValue() + "userQueue:"),


    ;


    private final String value;

    ShiroCacheEnum(String value){
        this.value = value;
    }

    public static String isSuccessEnum(String value){
        ShiroCacheEnum[] array = values();
        for(ShiroCacheEnum arr: array){
            if(arr.value.equals(value)){
                return arr.value;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
