package com.lvyx.author.shiro.core.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.lvyx.author.entity.User;
import com.lvyx.author.shiro.bridge.UserBridgeService;
import com.lvyx.author.shiro.cache.service.SimpleMapCacheService;
import com.lvyx.author.shiro.core.ShiroDbRealm;
import com.lvyx.author.shiro.pojo.SimpleToken;
import com.lvyx.commons.enums.ShiroCacheEnum;
import com.lvyx.commons.pojo.ShiroUser;
import com.lvyx.commons.utils.ShiroUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 自定定义shiro认证实现类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23 19:29:44
 */
public class ShiroDbRealmImpl extends ShiroDbRealm {

    @Resource
    private UserBridgeService userBridgeService;

    @Resource
    private SimpleMapCacheService simpleMapCacheService;

    @Resource(name = "redissonClientForShiro")
    private RedissonClient redissonClient;

    /**
     * 认证
     * @param authcToken token对象
     * @return org.apache.shiro.authc.AuthenticationInfo 认证信息
     * @author lvyx
     * @since 2021/12/23 19:30
     **/
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        // 转换为自定义token
        SimpleToken token = (SimpleToken)authcToken;
        // 根据用户名查询登录用户
        User user  = userBridgeService.findUserByLoginName(token.getUsername());
        if(Objects.isNull(user)){
            // 用户不存在
            throw new UnknownAccountException("账号不存在");
        }
        ShiroUser shiroUser = new ShiroUser();
        // 将参数复制到拓展的shiroUser上
        BeanUtils.copyProperties(user, shiroUser);
        List<String> resourceIds = userBridgeService.findResourceIds(user.getId());
        if (CollectionUtil.isNotEmpty(resourceIds)){
            shiroUser.setResourceIds(userBridgeService.findResourceIds(user.getId()));
        }
        String sessionId = ShiroUtils.getSessionId();
        String roleKey = ShiroCacheEnum.ROLE_KEY+sessionId;
        List<String> roleList = userBridgeService.findRoleList(roleKey, ShiroUtils.getUserId());
        if (CollectionUtil.isNotEmpty(roleList)){
            shiroUser.setRoleName(roleList);
        }
        return new SimpleAuthenticationInfo(shiroUser, shiroUser.getPassword(), this.getName());

    }

    /**
     * 鉴权
     * @param principals 令牌
     * @return org.apache.shiro.authz.AuthorizationInfo 认证信息
     * @author lvyx
     * @since 2021/12/23 19:26
     **/
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        return userBridgeService.getAuthorizationInfo(shiroUser);
    }

    /**
     * 密码匹配器
     *
     * @author lvyx
     * @since 2022/1/10 15:02
     **/
    @Override
    public void initCredentialsMatcher() {
        // 自定义密码比较器
        RetryLimitCredentialsMatcher matcher = new RetryLimitCredentialsMatcher(redissonClient);
        // 生效密码比较器
        setCredentialsMatcher(matcher);
    }

    /**
     * 缓存清理方法
     * @param principals 令牌
     * @author lvyx
     * @since 2021/12/31 14:10
     **/
    @Override
    protected void doClearCache(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        String sessionId = ShiroUtils.getSessionId();
        // 角色key
        String roleKey = ShiroCacheEnum.ROLE_KEY.getValue() + sessionId;
        // 资源key
        String resourcesKey = ShiroCacheEnum.RESOURCES_KEY.getValue() + sessionId;
        // 登录用户名key
        String loginNamekey = ShiroCacheEnum.FIND_USER_BY_LOGINNAME.getValue() + shiroUser.getLoginName();
        // 资源idskey
        String resourcesIdKey = ShiroCacheEnum.RESOURCES_KEY_IDS.getValue() + sessionId;
        // 在线用户队列
        String onlineKey = ShiroCacheEnum.USER_QUEUE.getValue() + shiroUser.getLoginName();
        // 用户jwt
        String jwtKey  = ShiroCacheEnum.JWT_TOKEN.getValue() + sessionId;
        simpleMapCacheService.removeCache(roleKey);
        simpleMapCacheService.removeCache(resourcesKey);
        simpleMapCacheService.removeCache(loginNamekey);
        simpleMapCacheService.removeCache(resourcesIdKey);
        simpleMapCacheService.removeCache(jwtKey);
        RDeque<Object> deque = redissonClient.getDeque(onlineKey);
        deque.remove(sessionId);
        super.doClearCache(principals);
    }
}
