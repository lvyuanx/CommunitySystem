package com.lvyx.commons.enums;

/**
 * <p>
 * 返回结果枚举类
 * </p>
 *
 * @author lvyx
 * @date 2021-12-28 09:44:23
 */
public enum ResultCodeEnum {

    /**
     * 没有登录
     * @since 2021/12/28 9:51
     **/
    NO_LOGIN(401,"没有登录"),

    /**
     * 没有权限
     * @since 2021/12/28 9:51
     **/
    NO_AUTHORITY(403,"没有权限"),

    /**
     * 资源不存在
     * @since 2021/12/28 9:51
     **/
    NO_RESSOURCE(404,"资源不存在"),

    /**
     * 资源不存在
     * @since 2021/12/28 9:51
     **/
    OTHER(500,"服务器错误"),

    ;

    private Integer code;
    private String value;

    ResultCodeEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static String isSuccessEnum(Integer code){
        ResultCodeEnum[] array = values();
        for(ResultCodeEnum arr: array){
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
