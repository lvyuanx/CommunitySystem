package com.lvyx.author.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *   缓存实现类, 实现序列化接口方便对象存储于第三方容器(Map存放键值对)
 *   官方的实现类未实现序列化接口，无法使用redis缓存。
 * </p>
 *
 * @author lvyx
 * @since 2021-12-31 09:01:42
 */
public class SimpleMapCache implements Serializable, Cache<Object, Object> {

    /**
     * 缓存对象
     * @since 2021/12/31 9:10
     **/
    private final Map<Object, Object> attributes;

    /**
     * 缓存的名字
     * @since 2021/12/31 9:11
     **/
    private final String name;

    public SimpleMapCache(String name, Map<Object, Object> backingMap){
        if (name == null){
            throw new IllegalArgumentException("缓存名字不能为空！");
        }
        if (backingMap == null){
            throw new IllegalArgumentException("缓存对象不能为空！");
        }
        this.name = name;
        this.attributes = backingMap;
    }

    /**
     * 根据键得到值
     * @param key 键
     * @return java.lang.Object
     * @author lvyx
     * @since 2021/12/31 9:23
     **/
    @Override
    public Object get(Object key) throws CacheException {
        return attributes.get(key);
    }

    /**
     * 添加键值对
     * @param key 键
     * @param value 值
     * @return java.lang.Object
     * @author lvyx
     * @since 2021/12/31 9:23
     **/
    @Override
    public Object put(Object key, Object value) throws CacheException {
        return attributes.put(key, value);
    }

    /**
     * 根据将删除缓存
     * @param key 键
     * @return java.lang.Object
     * @author lvyx
     * @since 2021/12/31 9:24
     **/
    @Override
    public Object remove(Object key) throws CacheException {
        return attributes.remove(key);
    }

    /**
     * 清空缓存
     * @author lvyx
     * @since 2021/12/31 9:24
     **/
    @Override
    public void clear() throws CacheException {
        attributes.clear();
    }

    /**
     * 缓存的大小
     * @return int 缓存大小
     * @author lvyx
     * @since 2021/12/31 9:24
     **/
    @Override
    public int size() {
        return attributes.size();
    }

    /**
     * 得到缓存的键的集合
     * @return java.util.Set<java.lang.Object>
     * @author lvyx
     * @since 2021/12/31 9:25
     **/
    @Override
    public Set<Object> keys() {
        Set<Object> keyset = attributes.keySet();
        if (keyset.isEmpty()){
            return Collections.emptySet();
        }else {
            return Collections.unmodifiableSet(keyset);
        }
    }

    /**
     * 得到缓存值的集合
     * @return java.util.Collection<java.lang.Object>
     * @author lvyx
     * @since 2021/12/31 9:25
     **/
    @Override
    public Collection<Object> values() {
        Collection<Object> values = attributes.values();
        if (CollectionUtils.isEmpty(values)){
            return Collections.emptySet();
        }else {
            return Collections.unmodifiableCollection(values);
        }
    }

    @Override
    public String toString() {
        return "SimpleMapCache [attributes=" + attributes + ", name=" + name
                + ", keys()=" + keys() + ", size()=" + size() + ", values()="
                + values() + "]";
    }
}
