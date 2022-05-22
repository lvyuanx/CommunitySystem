package com.lvyx.commons.enums;

/**
 * <p>
 *  住址信息状态
 * </p>
 *
 * @author lvyx
 * @date 2021-12-28 09:44:23
 */
public enum AddressStatusEnum {

    /**
     * 没有登录
     * @since 2021/12/28 9:51
     **/
    INACTIVE(0,"未激活"),

    /**
     * 没有权限
     * @since 2021/12/28 9:51
     **/
    ACTIVE(1,"已激活"),

    /**
     * 没有权限
     * @since 2021/12/28 9:51
     **/
    NO_CREATE(2,"未创建"),

    ;

    private Integer code;
    private String value;

    AddressStatusEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static String isSuccessEnum(Integer code){
        AddressStatusEnum[] array = values();
        for(AddressStatusEnum arr: array){
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
