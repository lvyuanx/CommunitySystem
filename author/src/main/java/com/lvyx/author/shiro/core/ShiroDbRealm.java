package com.lvyx.author.shiro.core;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.PostConstruct;


/**
 * <p>
 * 自定义shiro认证抽象类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
public abstract class ShiroDbRealm extends AuthorizingRealm {
	
	/**
	 * 认证
	 * @param authcToken token对象
	 * @return org.apache.shiro.authc.AuthenticationInfo 认证信息
	 * @author lvyx
	 * @since 2021/12/23 19:30
	 **/
	@Override
	public abstract AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) ;

	/**
	 * 鉴权
	 * @param principals 令牌
	 * @return org.apache.shiro.authz.AuthorizationInfo 认证信息
	 * @author lvyx
	 * @since 2021/12/23 19:26
	 **/
	@Override
	public abstract AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals);

	/**
	 * 密码匹配器
	 * @author lvyx
	 * @since 2022/1/10 15:02
	 **/
	@PostConstruct
	public abstract void initCredentialsMatcher();

}