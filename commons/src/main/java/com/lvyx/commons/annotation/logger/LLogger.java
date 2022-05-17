package com.lvyx.commons.annotation.logger;

import java.lang.annotation.*;

/**
 * <p>
 * 自定日志注解
 * 使用说明：
 *      1. 该注解用于Controller层，进行日志的记录与存储
 *      2. 请自行创建T_L_LOG数据表用户存储日志内容
 *      3. 如果捕获了异常，日志注解将无法记录错误信息
 * </p>
 *
 * @author lvyx
 * @since 2021-12-02 13:47:53
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LLogger {
    /**
     * 方法注释
     * @return java.lang.String
     * @author lvyx
     * @since 2021/12/3 15:34
     **/
    String description();
    
    /**
     * 形参注释
     *      注意：形参注释的顺序与个数必须与方法的形参一一对应
     * @return java.lang.String[]
     * @author lvyx
     * @since 2021/12/3 15:34
     **/
    String[] params() default {};
}
