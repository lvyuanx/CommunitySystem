package com.lvyx.author.shiro.config;

import com.lvyx.author.shiro.core.ShiroDbRealm;
import com.lvyx.author.shiro.core.impl.JwtTokenManager;
import com.lvyx.author.shiro.core.impl.RedisSessionDao;
import com.lvyx.author.shiro.core.impl.ShiroDbRealmImpl;
import com.lvyx.author.shiro.core.impl.ShiroSessionManager;
import com.lvyx.author.shiro.filter.*;
import com.lvyx.author.shiro.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * shiro 配置类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-27 14:30:06
 */
@Configuration
@Slf4j
public class ShiroConfig {

    @Resource
    private ShiroRedisProperties shiroRedisProperties;

    @Resource
    private JwtTokenManager jwtTokenManager;


    /**
     * 创建Redis客户端仅供Shiro使用
     * @return org.redisson.api.RedissonClient
     * @author lvyx
     * @since 2021/12/31 8:50
     **/
    @Bean("redissonClientForShiro")
    public RedissonClient redissonClient() {
        log.info("=====初始化redissonClientForShiro开始======");
        String[] nodeList = shiroRedisProperties.getNodes().split(",");
        Config config = new Config();
        if (nodeList.length == 1) {
            // 单一节点
            config.useSingleServer().setAddress(nodeList[0])
                    .setConnectTimeout(shiroRedisProperties.getConnectTimeout())
                    .setConnectionMinimumIdleSize(shiroRedisProperties.getConnectionMinimumidleSize())
                    .setConnectionPoolSize(shiroRedisProperties.getConnectPoolSize()).setTimeout(shiroRedisProperties.getTimeout());
        } else {
            // 集群环境
            config.useClusterServers().addNodeAddress(nodeList)
                    .setConnectTimeout(shiroRedisProperties.getConnectTimeout())
                    .setMasterConnectionMinimumIdleSize(shiroRedisProperties.getConnectionMinimumidleSize())
                    .setMasterConnectionPoolSize(shiroRedisProperties.getConnectPoolSize()).setTimeout(shiroRedisProperties.getTimeout());
        }
        RedissonClient redissonClient =  Redisson.create(config);
        log.info("=====初始化redissonClientForShiro完成======");
        return redissonClient;
    }

    /**
     * 创建cookie对象
     * @return org.apache.shiro.web.servlet.SimpleCookie
     * @author lvyx
     * @since 2021/12/27 14:31
     **/
    @Bean(name="sessionIdCookie")
    public SimpleCookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("LCookieSession");
        return simpleCookie;
    }

    /**
     * 自定义session会话存储的实现类 ，使用Redis来存储共享session，达到分布式部署目的
     * @return org.apache.shiro.session.mgt.eis.SessionDAO
     * @author lvyx
     * @since 2022/1/4 17:21
     **/
    @Bean("redisSessionDao")
    public SessionDAO redisSessionDao(){
        RedisSessionDao redisSessionDao = new RedisSessionDao();
        redisSessionDao.setGlobalSessionTimeout(shiroRedisProperties.getGlobalSessionTimeout());
        return redisSessionDao;
    }

    /**
     * 权限管理器
     * @return org.apache.shiro.web.mgt.DefaultWebSecurityManager
     * @author lvyx
     * @since 2021/12/27 14:31
     **/
    @Bean(name="securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroDbRealm());
        securityManager.setSessionManager(shiroSessionManager());
        return securityManager;
    }

    /**
     * 自定义realm
     * @return com.lvyx.author.shiro.core.ShiroDbRealm
     * @author lvyx
     * @since 2021/12/27 14:32
     **/
    @Bean
    public ShiroDbRealm shiroDbRealm(){
        return new ShiroDbRealmImpl();
    }

    /**
     * 会话管理器
     * @return DefaultWebSessionManager
     * @author lvyx
     * @since 2021/12/27 14:32
     **/
    @Bean
    public DefaultWebSessionManager shiroSessionManager(){
        DefaultWebSessionManager sessionManager = new ShiroSessionManager();
        // 使用redisSessionDao
        sessionManager.setSessionDAO(redisSessionDao());
        // 关闭会话更新
        sessionManager.setSessionValidationSchedulerEnabled(false);
        // 开启cookie
        sessionManager.setSessionIdCookieEnabled(true);
        // 指定cookie生成策略
        sessionManager.setSessionIdCookie(simpleCookie());
        // 会话过期时间
        sessionManager.setGlobalSessionTimeout(3600000);
        return sessionManager;
    }


    /**
     * Shiro过滤器
     * @return org.apache.shiro.spring.web.ShiroFilterFactoryBean
     * @author lvyx
     * @since 2021/12/27 14:35
     **/
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(defaultWebSecurityManager());
        // 自定义过滤器
        shiroFilter.setFilters(filters());
        // 过滤器链
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinition());
        return shiroFilter;
    }

    /**
     * 自定义拦截器
     * @return java.util.Map<java.lang.String,javax.servlet.Filter>
     * @author lvyx
     * @since 2022/1/13 15:00
     **/
    private Map<String, Filter> filters() {
        Map<String, Filter> map = new HashMap<String, Filter>();
        map.put("online", new KickedOutAuthorizationFilter(redissonClient(), redisSessionDao(), shiroSessionManager()));
        map.put("jwt-authc", new JwtAuthcFilter(jwtTokenManager));
        map.put("jwt-perms", new JwtPermsFilter());
        map.put("jwt-roles", new JwtRolesFilter());
        map.put("jwt-role-or", new RolesOrAuthorizationFilter());
        return map;
    }

    /**
     * 过滤器链，读取authentication.properties文件
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author lvyx
     * @since 2021/12/27 14:34
     **/
    private Map<String, String> filterChainDefinition(){
        List<Object> list  = PropertiesUtil.propertiesShiro.getKeyList();
        Map<String, String> map = new LinkedHashMap<>();
        for (Object object : list) {
            String key = object.toString();
            String value = PropertiesUtil.getShiroValue(key);
            log.info("读取防止盗链控制：---key:{},---value:{}",key,value);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 保证实现了Shiro内部lifecycle函数的bean执行
     * @return org.apache.shiro.spring.LifecycleBeanPostProcessor
     * @author lvyx
     * @since 2021/12/27 14:33
     **/
    @Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * AOP式方法级权限检查
     * @return org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
     * @author lvyx
     * @since 2021/12/27 14:33
     **/
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 配合DefaultAdvisorAutoProxyCreator事项注解权限校验
     * @return org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
     * @author lvyx
     * @since 2021/12/27 14:34
     **/
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(defaultWebSecurityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }

}
