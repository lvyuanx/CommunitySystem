package com.lvyx.author.shiro.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.lvyx.author.shiro.config.JWTProperties;
import com.lvyx.commons.enums.ResultCodeEnum;
import com.lvyx.commons.enums.ShiroTokenEnum;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.utils.ApplicationContextUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 自定义jwt的资源校验
 * @author lvyx
 * @since 2022/1/14 14:05
 **/
public class JwtPermsFilter extends PermissionsAuthorizationFilter {

    @Resource
    private JWTProperties jWTProperties;

    /**
     * 资源访问被拒绝时
     * @param request   request
     * @param response  response
     * @return boolean
     * @author lvyx
     * @since 2022/1/14 14:07
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader(ShiroTokenEnum.AUTHORIZATION.getValue());
        if (ObjectUtil.isNull(jWTProperties)){
            jWTProperties = ApplicationContextUtils.getBean(JWTProperties.class);
        }
        //如果有：返回json的应答
        if (!ObjectUtil.isEmpty(jwtToken) | jWTProperties.isOnlyJwt()){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONUtil.toJsonStr(new ErrorResult<>(ResultCodeEnum.NO_AUTHORITY.getCode(), "权限不足！")));
            return false;
        }
        //如果没有：走原始方式
        return super.onAccessDenied(request, response);
    }
}