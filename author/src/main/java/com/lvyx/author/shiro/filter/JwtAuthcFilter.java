package com.lvyx.author.shiro.filter;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.lvyx.author.shiro.config.JWTProperties;
import com.lvyx.author.shiro.core.impl.JwtTokenManager;
import com.lvyx.commons.enums.ResultCodeEnum;
import com.lvyx.commons.enums.ShiroTokenEnum;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.utils.ApplicationContextUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义登录验证过滤器
 * @author lvyx
 * @since 2022/1/14 13:42
 **/
public class JwtAuthcFilter extends FormAuthenticationFilter {

    private JwtTokenManager jwtTokenManager;

    @Resource
    private JWTProperties jWTProperties;

    public JwtAuthcFilter(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    /**
     * 是否允许访问
     * @param request     request
     * @param response    response
     * @param mappedValue 请求信息
     * @return boolean
     * @author lvyx
     * @since 2022/1/14 13:42
     **/
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader(ShiroTokenEnum.AUTHORIZATION.getValue());
        //如果有：走jwt校验
        if (!ObjectUtil.isEmpty(jwtToken)){
            // 校验token
            boolean verifyToken = jwtTokenManager.isVerifyToken(jwtToken);
            if (verifyToken){
                // 校验成功
                return super.isAccessAllowed(request, response, mappedValue);
            }else {
                return false;
            }
        }
        if (ObjectUtil.isNull(jWTProperties)){
            jWTProperties = ApplicationContextUtils.getBean(JWTProperties.class);
        }
        // 是否仅仅允许使用jwt方式登录
        if (jWTProperties.isOnlyJwt()){
            return false;
        }
        //没有没有：走原始校验
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 访问拒绝时调用
     * @param request  reuest
     * @param response response
     * @return boolean
     * @author lvyx
     * @since 2022/1/14 13:54
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader(ShiroTokenEnum.AUTHORIZATION.getValue());
        if (ObjectUtil.isNull(jWTProperties)){
            jWTProperties = ApplicationContextUtils.getBean(JWTProperties.class);
        }
        //如果有：返回json的应答
        if (!ObjectUtil.isEmpty(jwtToken) || jWTProperties.isOnlyJwt()){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONUtil.toJsonStr(new ErrorResult<>(ResultCodeEnum.NO_LOGIN.getCode(), "请重新登录")));
            return false;
        }
        //如果没有：走原始方式
        return super.onAccessDenied(request, response);
    }
}