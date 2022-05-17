package com.lvyx.author.shiro.bridge;

import com.lvyx.author.entity.User;
import com.lvyx.commons.pojo.ShiroUser;
import org.apache.shiro.authz.AuthorizationInfo;

import java.util.List;

/**
 * <p>
 *  用户信息桥接
 * </p>
 *
 * @author lvyx
 * @date 2021-12-24 09:19:54
 */
public interface UserBridgeService {

    /**
     * 根据用户登录名查找用户
     * @param loginName 登录名称
     * @return com.lvyx.author.entity.User
     * @author lvyx
     * @since 2021/12/24 9:21
     **/
    User findUserByLoginName(String loginName);

    /**
     * 根据用户id得到用户拥有的资源id
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/24 9:30
     **/
    List<String> findResourceIds(String userId);


    /**
     * 根据用户id的到用户角色信息
     * @param userId  用户id
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/31 14:00
     **/
    List<String> findRoleIds(String userId);


    /**
     * 得到鉴权信息
     * @param shiroUser shiro用户对象
     * @return org.apache.shiro.authz.AuthorizationInfo 鉴权信息
     * @author lvyx
     * @since 2021/12/24 10:01
     **/
    AuthorizationInfo getAuthorizationInfo(ShiroUser shiroUser);

    /**
     * 登录成后加载缓存信息
     * @param shiroUser 登录信息
     * @author lvyx
     * @since 2022/1/14 15:39
     **/
    void loadUserAuthorityToCache(ShiroUser shiroUser);

    /**
     * 查询用户对应角色标识list
     * @param userId 用户id
     * @return 角色标识集合
     */
    List<String> findRoleList(String key,String userId);

    /**
     * 查询用户对应资源标识list
     * @param userId 用户id
     * @return 资源标识集合
     */
    List<String> findResourcesList(String key,String userId);

}
