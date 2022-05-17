package com.lvyx.commons.enums;

/**
 * <p>
 * 返回结果枚举类
 * </p>
 *
 * @author lvyx
 * @date 2021-12-28 09:44:23
 */
public enum ResultEnum {

    /**
     * 请求成功
     * @since 2021/12/28 9:50
     **/
    SUCCESS(200,"请求成功"),

    /**
     * 请求异常
     * @since 2021/12/28 9:51
     **/
    ERROR(500,"请求异常"),

    ;

    private Integer code;
    private String value;

    ResultEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static String isSuccessEnum(Integer code){
        ResultEnum[] array = values();
        for(ResultEnum arr: array){
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
