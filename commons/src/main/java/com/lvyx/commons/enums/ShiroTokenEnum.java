package com.lvyx.commons.enums;

/**
 * <p>
 * shiro 缓存枚举
 * </p>
 *
 * @author lvyx
 * @date 2021-12-31 09:59:24
 */
public enum ShiroTokenEnum {

    /**
     * 请求头中携带token的名称
     * @since 2021/12/31 10:08
     **/
    AUTHORIZATION("L-TOKEN"),

    /**
     * 自定义注入资源名称
     * @since 2021/12/31 10:08
     **/
    REFERENCED_SESSION_ID_SOURCE("Stateless request"),

    ;


    private final String value;

    ShiroTokenEnum(String value){
        this.value = value;
    }

    public static String isSuccessEnum(String value){
        ShiroTokenEnum[] array = values();
        for(ShiroTokenEnum arr: array){
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