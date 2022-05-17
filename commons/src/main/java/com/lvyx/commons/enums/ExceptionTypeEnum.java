package com.lvyx.commons.enums;

/**
 * <p>
 *  社区异常类型
 * </p>
 *
 * @author lvyx
 * @date 2021-12-28 09:44:23
 */
public enum ExceptionTypeEnum {

    /**
     * 其他异常
     * @since 2021/12/28 9:51
     **/
    YES(1,"其他异常"),

    /**
     * 体温异常
     * @since 2021/12/28 9:51
     **/
    NO(0,"体温异常"),

    ;

    private Integer code;
    private String value;

    ExceptionTypeEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static String isSuccessEnum(Integer code){
        ExceptionTypeEnum[] array = values();
        for(ExceptionTypeEnum arr: array){
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
