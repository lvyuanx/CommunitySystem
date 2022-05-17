package com.lvyx.commons.cache.service;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

/**
 * <p>
 * 自定义简单缓存管理接口
 * </p>
 *
 * @author lvyx
 * @date 2021-12-31 09:38:27
 */
public interface SysSimpleMapCacheService {

    /**
     * <b>功能说明：</b>：新增缓存堆到管理器<br>
     * @param cacheName 缓存名称
     * @param cache 缓存对象
     * @exception CacheException 缓存异常
     * @author lvyx
     * @since 2021/12/31 9:40
     **/
    void createCache(String cacheName, Cache<Object, Object> cache) throws CacheException;

    /**
     * <b>功能说明：</b>：新增缓存堆到管理器<br>
     * @param cacheName 缓存名称
     * @param cache 缓存对象
     * @param timeoutTime 超时时间
     * @exception CacheException 缓存异常
     * @author lvyx
     * @since 2021/12/31 9:40
     **/
    void createCache(String cacheName, Cache<Object, Object> cache,Long timeoutTime) throws CacheException;

    /**
     * <b>方法名：</b>：getCache<br>
     * <b>功能说明：</b>：获取缓存堆<br>
     * @param cacheName  缓存名称
     * @exception CacheException 缓存异常
     * @return org.apache.shiro.cache.Cache<java.lang.Object,java.lang.Object>
     * @author lvyx
     * @since 2021/12/31 9:44
     **/
    Cache<Object, Object> getCache(String cacheName) throws CacheException;

    /**
     * <b>方法名：</b>：removeCache<br>
     * <b>功能说明：</b>：移除缓存堆<br>
     * @param cacheName 缓存名称
     * @exception CacheException 缓存异常
     * @author lvyx
     * @since 2021/12/31 9:46
     **/
    void removeCache(String cacheName) throws CacheException;

    /**
     * <b>方法名：</b>：updateCahce<br>
     * <b>功能说明：</b>：更新缓存堆<br>
     * @param cacheName 缓存名称
     * @param cache 缓存对象
     * @exception CacheException 缓存异常
     * @author lvyx
     * @since 2021/12/31 9:47
     **/
    void updateCahce(String cacheName, Cache<Object, Object> cache) throws CacheException;

    /**
     * <b>方法名：</b>：updateCahce<br>
     * <b>功能说明：</b>：更新缓存堆<br>
     * @param cacheName 缓存名称
     * @param cache 缓存对象
     * @param timeoutTime 超时时间
     * @exception CacheException 缓存异常
     * @author lvyx
     * @since 2021/12/31 9:47
     **/
    void updateCahce(String cacheName, Cache<Object, Object> cache, Long timeoutTime) throws CacheException;
}
