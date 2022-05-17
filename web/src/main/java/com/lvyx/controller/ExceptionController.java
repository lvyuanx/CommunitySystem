package com.lvyx.controller;


import com.lvyx.commons.result.Result;
import com.lvyx.factory.MyExceptionFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 全局异常处理器
 * </p>
 *
 * @author lvyx
 * @since 2021-12-09 11:00:53
 */
@RestControllerAdvice
public class ExceptionController {


    /**
     * 全局异常处理
     * @param request request上下文
     * @param e 异常信息
     * @return com.lvyx.shiro_boot02.vo.Result
     * @author lvyx
     * @since 2021/12/9 11:03
     **/
    @ExceptionHandler(Exception.class)
    public Result<Object> defaultExceptionHandler(HttpServletRequest request, Exception e){
        // 异常方法
        String requestMethod = request.getMethod();
        return MyExceptionFactory.getResult(e, requestMethod);
    }
}