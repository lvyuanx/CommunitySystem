package com.lvyx.author.shiro.core.impl;

import cn.hutool.core.util.ObjectUtil;
import com.lvyx.commons.enums.ShiroTokenEnum;
import io.jsonwebtoken.Claims;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * <p>
 * Jwt会话管理
 * </p>
 *
 * @author lvyx
 * @since 2022-01-14 11:32:02
 */
public class ShiroSessionManager extends DefaultWebSessionManager {


    @Resource
    private JwtTokenManager jwtTokenManager;

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 判断request请求中是否带有jwtToken的key
        String jwtToken = WebUtils.toHttp(request).getHeader(ShiroTokenEnum.AUTHORIZATION.getValue());
        if (ObjectUtil.isEmpty(jwtToken)){
            // 如果没有走默认的cook获得sessionId的方式
            return super.getSessionId(request, response);
        }else {
            // 如果有走jwtToken获得sessionI的的方式
            Claims claims = null;
            String id = null;
            try {
                claims = jwtTokenManager.decodeToken(jwtToken);
                // 创建token的时候SessionId赋给了jti
                id = (String) claims.get("jti");
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                        ShiroTokenEnum.REFERENCED_SESSION_ID_SOURCE.getValue());
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            } catch (Exception e) {
                // 无法获取sessionId
                return null;
            }
            return id;
        }

    }

}
