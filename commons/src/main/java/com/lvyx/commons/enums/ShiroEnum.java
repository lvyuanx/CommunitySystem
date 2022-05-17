package com.lvyx.commons.enums;

/**
 * <p>
 * 返回结果枚举类
 * </p>
 *
 * @author lvyx
 * @date 2021-12-28 09:44:23
 */
public enum ShiroEnum {

    /**
     * 系统后台
     * @since 2021/12/28 9:50
     **/
    PLATFORM_MGT(1,"platform_mgt"),

    /**
     * 系统前台
     * @since 2021/12/28 10:43
     **/
    OPEN_API(2, "open_api"),

    /**
     * 密码登录
     * @since 2021/12/28 10:43
     **/
    LOGIN_TYPE_PASSWORD(3, "login_type_password"),

    /**
     * 快速登录
     * @since 2021/12/28 10:44
     **/
    LOGIN_TYPE_QUICK(4, "login_type_quick"),

    /**
     * pc密码登录
     * @since 2021/12/28 10:45
     **/
    LOGIN_TYPE_PASSWORD_PC(5, "login_type_password_pc")

    ;

    private Integer code;
    private String value;

    ShiroEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static String isSuccessEnum(Integer code){
        ShiroEnum[] array = values();
        for(ShiroEnum arr: array){
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
