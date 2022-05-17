package com.lvyx.commons.enums;

/**
 * <p>
 *  用户类型类型值
 * </p>
 *
 * @author lvyx
 * @date 2021-12-28 09:44:23
 */
public enum UserTypeEnum {

    /**
     * 社区用户
     * @since 2021/12/28 9:51
     **/
    COMMUNITY_USER(0,"社区用户"),

    /**
     * 管理员
     * @since 2021/12/28 9:51
     **/
    SYSTEM_USER(1,"管理员"),

    /**
     * 其他用户
     * @since 2021/12/28 9:51
     **/
    OTHER_USER(2,"其他用户"),

    /**
     * 其他用户
     * @since 2021/12/28 9:51
     **/
    SYSTEM_OTHER_USER(3,"管理员和其他用户"),
    ;

    private Integer code;
    private String value;

    UserTypeEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static String isSuccessEnum(Integer code){
        UserTypeEnum[] array = values();
        for(UserTypeEnum arr: array){
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
