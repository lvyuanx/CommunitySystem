package com.lvyx.commons.encrypt_decrypt;

import java.lang.annotation.*;

/**
 * 自动加密解密注解
 * @author lvyx
 * @since 2021/12/9 19:10
 **/
@Documented
@Inherited
@Target({ ElementType.TYPE,ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LEncryptDecrypt {

    /**
     * 加密
     * @return boolean
     * @author lvyx
     * @since 2021/12/9 19:52
     **/
    boolean encrypt() default true;

    /**
     * 解密
     * @return boolean
     * @author lvyx
     * @since 2021/12/9 19:53
     **/
    boolean dencrypt() default true;
}