package com.lvyx.commons.enums;

/**
 * <p>
 *  数据库boolean类型值
 * </p>
 *
 * @author lvyx
 * @date 2021-12-28 09:44:23
 */
public enum BooleanTypeEnum {

    /**
     * 没有登录
     * @since 2021/12/28 9:51
     **/
    YES(1,"是"),

    /**
     * 没有权限
     * @since 2021/12/28 9:51
     **/
    NO(0,"否"),

    ;

    private Integer code;
    private String value;

    BooleanTypeEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static String isSuccessEnum(Integer code){
        BooleanTypeEnum[] array = values();
        for(BooleanTypeEnum arr: array){
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
