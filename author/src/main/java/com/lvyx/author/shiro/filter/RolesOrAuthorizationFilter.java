package com.lvyx.author.shiro.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.lvyx.author.shiro.cache.service.SimpleMapCacheService;
import com.lvyx.author.shiro.config.JWTProperties;
import com.lvyx.commons.enums.ResultCodeEnum;
import com.lvyx.commons.enums.ShiroTokenEnum;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.utils.ApplicationContextUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 角色或关系
 *
 * @author lvyx
 * @since 2022/2/8 15:42
 **/
public class RolesOrAuthorizationFilter extends RolesAuthorizationFilter {

    @Resource
    private JWTProperties jWTProperties;

    @Resource
    private SimpleMapCacheService simpleMapCacheService;


    @Override
    @SuppressWarnings({"unchecked"})
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            //不需要角色信息
            return true;
        }
        Set<String> roles = CollectionUtils.asSet(rolesArray);
        //循环roles判断只要有角色则返回true
        for (String role : roles) {
            if(subject.hasRole(role)){
                return true;
            }
        }
        return false;
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
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        //判断当前请求头中是否带有jwtToken的字符串
        String jwtToken = WebUtils.toHttp(request).getHeader(ShiroTokenEnum.AUTHORIZATION.getValue());
        if (ObjectUtil.isNull(jWTProperties)){
            jWTProperties = ApplicationContextUtils.getBean(JWTProperties.class);
        }
        //如果有：返回json的应答
        if (!ObjectUtil.isEmpty(jwtToken) || jWTProperties.isOnlyJwt()){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSONUtil.toJsonStr(new ErrorResult<>(ResultCodeEnum.NO_AUTHORITY.getCode(), "权限不足")));
            return false;
        }
        //如果没有：走原始方式
        return super.onAccessDenied(request, response);
    }

}
