package com.lvyx.commons.enums;

/**
 * <p>
 * shiro 缓存枚举
 * </p>
 *
 * @author lvyx
 * @date 2021-12-31 09:59:24
 */
public enum SystemCacheEnum {

    /**
     * 缓存最外层组名称
     * @since 2021/12/31 10:08
     **/
    COMMUNITY_SYSTEM_CACHE("COMMUNITY_SYSTEM_CACHE:"),


    /**
     * 系统外层层组名
     * @since 2021/12/31 10:08
     **/
    SYSTEM_GROUP(SystemCacheEnum.COMMUNITY_SYSTEM_CACHE.getValue() + "system_group:"),

    /**
     * 验证码
     * @since 2021/12/31 10:08
     **/
    CAPTCHA(SystemCacheEnum.SYSTEM_GROUP.getValue() + "captcha: "),

    ;


    private final String value;

    SystemCacheEnum(String value){
        this.value = value;
    }

    public static String isSuccessEnum(String value){
        SystemCacheEnum[] array = values();
        for(SystemCacheEnum arr: array){
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
