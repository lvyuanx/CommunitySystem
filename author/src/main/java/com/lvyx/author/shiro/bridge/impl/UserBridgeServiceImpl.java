package com.lvyx.author.shiro.bridge.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lvyx.author.entity.Role;
import com.lvyx.author.entity.User;
import com.lvyx.author.service.ResourceService;
import com.lvyx.author.service.RoleService;
import com.lvyx.author.service.UserService;
import com.lvyx.author.shiro.bridge.UserBridgeService;
import com.lvyx.author.shiro.cache.SimpleMapCache;
import com.lvyx.author.shiro.cache.service.SimpleMapCacheService;
import com.lvyx.commons.enums.ShiroCacheEnum;
import com.lvyx.commons.pojo.ShiroUser;
import com.lvyx.commons.utils.ShiroUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息桥接实现类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-24 09:22:30
 */
@Component
public class UserBridgeServiceImpl implements UserBridgeService {

    @Resource
    private UserService userService;

    @Resource
    private ResourceService resourceService;

    @Resource
    private RoleService roleService;

    @Resource
    private SimpleMapCacheService simpleMapCacheService;

    /**
     * 根据用户登录名查找用户
     * @param loginName 登录名称
     * @return com.lvyx.author.entity.User
     * @author lvyx
     * @since 2021/12/24 9:21
     **/
    @Override
    public User findUserByLoginName(String loginName) {
        // 查询缓存中是否有该用户的登录信息
        String key = ShiroCacheEnum.FIND_USER_BY_LOGINNAME.getValue() + loginName;
        Cache<Object, Object> cache = simpleMapCacheService.getCache(key);
        if (Objects.nonNull(cache)){
            // 缓存不为空，直接返回用户信息
            return (User) cache.get(key);
        }
        // 缓存不存在，去数据库中查找
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getLoginName, loginName);
        User user = userService.getOne(wrapper);
        if (Objects.nonNull(user)){
            // 创建缓存
            Map<Object, Object>  map = new HashMap<>();
            map.put(key, user);
            simpleMapCacheService.createCache(key, new SimpleMapCache(key, map));
        }
        return user;
    }

    /**
     * 根据用户id得到用户拥有的资源id
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/24 9:30
     **/
    @Override
    public List<String> findResourceIds(String userId) {
        // 得到sessionId
        String sessionId = ShiroUtils.getSessionId();
        String key = ShiroCacheEnum.RESOURCES_KEY_IDS.getValue() + sessionId;
        Cache<Object, Object> cache = simpleMapCacheService.getCache(key);
        if (Objects.nonNull(cache)){
            // 缓存存在
            List<com.lvyx.author.entity.Resource> resources = (List< com.lvyx.author.entity.Resource>) cache.get(key);
            return  resources.stream().map(com.lvyx.author.entity.Resource::getId).collect(Collectors.toList());
        }
        // 缓存不存在
        List<com.lvyx.author.entity.Resource> resourceList = resourceService.getResourceByUserId(userId);
        if (! CollectionUtils.isEmpty(resourceList)) {
            // 创建缓存
            Map<Object, Object> map = new HashMap<>();
            map.put(key, resourceList);
            simpleMapCacheService.createCache(key, new SimpleMapCache(key, map));
            return resourceList.stream().map(com.lvyx.author.entity.Resource::getId).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 根据用户id的到用户角色信息
     *
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/31 14:00
     **/
    @Override
    public List<String> findRoleIds(String userId) {
        // 得到SessionId
        String sessionId = ShiroUtils.getSessionId();
        String key = ShiroCacheEnum.ROLE_KEY.getValue() + sessionId;
        Cache<Object, Object> cache = simpleMapCacheService.getCache(key);
        if (Objects.nonNull(cache)){
            // 缓存存在
            List<Role> roleList = (List<Role>) cache.get(key);
            return roleList.stream().map(Role::getId).collect(Collectors.toList());
        }
        // 缓存不存
        List<Role> roleList = roleService.findRoleByUserId(userId);
        if (! CollectionUtils.isEmpty(roleList)){
            Map<Object, Object> map = new HashMap<>();
            map.put(key, roleList);
            simpleMapCacheService.createCache(key, new SimpleMapCache(key, map));
            return roleList.stream().map(Role::getId).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 得到鉴权信息
     * @param shiroUser shiro用户对象
     * @return org.apache.shiro.authz.AuthorizationInfo 鉴权信息
     * @author lvyx
     * @since 2021/12/24 10:01
     **/
    @Override
    public AuthorizationInfo getAuthorizationInfo(ShiroUser shiroUser) {
        String sessionId = ShiroUtils.getSessionId();
        String roleKey = ShiroCacheEnum.ROLE_KEY+sessionId;
        String resourcesKey = ShiroCacheEnum.RESOURCES_KEY+sessionId;
        // 得到用户的角色
        List<String> roleIds = this.findRoleList(roleKey, ShiroUtils.getShiroUser().getId());
        //  得到用户的资源
        List<String> resourceIds = this.findResourcesList(resourcesKey, ShiroUtils.getShiroUser().getId());
        //构建鉴权信息对象
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            simpleAuthorizationInfo.addRoles(roleIds);
        }
        if (CollectionUtils.isNotEmpty(resourceIds)) {
            simpleAuthorizationInfo.addStringPermissions(resourceIds);
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 登录成后加载缓存信息
     *
     * @param shiroUser 登录信息
     * @author lvyx
     * @since 2022/1/14 15:39
     **/
    @Override
    public void loadUserAuthorityToCache(ShiroUser shiroUser) {
        String sessionId = ShiroUtils.getSessionId();
        String roleKey = ShiroCacheEnum.ROLE_KEY+sessionId;
        String resourcesKey = ShiroCacheEnum.RESOURCES_KEY+sessionId;
        //查询用户对应的角色标识
        List<String> roleList = this.findRoleList(roleKey,shiroUser.getId());
        //查询用户对于的资源标识
        List<String> resourcesList = this.findResourcesList(resourcesKey,shiroUser.getId());
    }


    @Override
    public List<String> findRoleList(String key,String userId){
        List<Role> roles = new ArrayList<>();
        //获得缓存
        Cache<Object, Object> cache = simpleMapCacheService.getCache(key);
        //缓存存在
        if (!ObjectUtil.isEmpty(cache)){
            roles = (List<Role>) cache.get(key);
        }else {
            //缓存不存在
            roles = this.roleService.findRoleByUserId(userId);
            if (!ObjectUtil.isEmpty(roles)){
                Map<Object,Object> map = new HashMap<>();
                map.put(key, roles);
                SimpleMapCache simpleMapCache = new SimpleMapCache(key, map);
                simpleMapCacheService.createCache(key,simpleMapCache );
            }
        }
        List<String> roleLabel = new ArrayList<>();
        for (Role role : roles) {
            roleLabel.add(role.getLabel());
        }
        return roleLabel;
    }

    @Override
    public List<String> findResourcesList(String key,String userId){
        List<com.lvyx.author.entity.Resource> resources = new ArrayList<>();
        //获得缓存
        Cache<Object, Object> cache = simpleMapCacheService.getCache(key);
        //缓存存在
        if (!ObjectUtil.isEmpty(cache)){
            resources = (List<com.lvyx.author.entity.Resource>) cache.get(key);
        }else {
            //缓存不存在
            resources = resourceService.getResourceByUserId(userId);
            if (!ObjectUtil.isEmpty(resources)){
                Map<Object,Object> map = new HashMap<>();
                map.put(key, resources);
                SimpleMapCache simpleMapCache = new SimpleMapCache(key, map);
                simpleMapCacheService.createCache(key,simpleMapCache );
            }
        }
        List<String> resourceLabel = new ArrayList<>();
        for (com.lvyx.author.entity.Resource resource : resources) {
            resourceLabel.add(resource.getLabel());
        }
        return resourceLabel;
    }
}
