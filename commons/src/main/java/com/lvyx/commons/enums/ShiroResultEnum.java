package com.lvyx.commons.enums;

/**
 * <p>
 * 返回结果枚举类
 * </p>
 *
 * @author lvyx
 * @date 2021-12-28 09:44:23
 */
public enum ShiroResultEnum {

    /**
     * 没有登录
     * @since 2021/12/28 9:51
     **/
    NO_LOGIN(401,"没有登录"),

    /**
     * 没有权限
     * @since 2021/12/28 9:51
     **/
    NO_AUTHORITY(402,"没有权限"),

    /**
     * 资源不存在
     * @since 2021/12/28 9:51
     **/
    NO_RESSOURCE(403,"资源不存在"),

    ;

    private Integer code;
    private String value;

    ShiroResultEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static String isSuccessEnum(Integer code){
        ShiroResultEnum[] array = values();
        for(ShiroResultEnum arr: array){
            if(arr.code.equals(code)){
                return arr.value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
