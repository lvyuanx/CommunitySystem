package com.lvyx.controller;

import com.lvyx.commons.result.Result;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 返回异常控制器
 * </p>
 *
 * @author lvyx
 * @since 2021-12-28 16:16:08
 */
@RestController
public class ResultErrorController {

    @RequestMapping("/shiroError")
    public void error(){
        throw new AuthorizationException();
    }
}
