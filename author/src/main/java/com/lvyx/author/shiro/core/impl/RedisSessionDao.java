package com.lvyx.author.shiro.core.impl;


import com.lvyx.commons.enums.ShiroCacheEnum;
import com.lvyx.commons.utils.LSerializeUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     自定义统一sessiondao实现
 * </p>
 *
 * @author lvyx
 * @since 2022/1/4 17:12
 **/
public class RedisSessionDao extends AbstractSessionDAO {

    @Resource(name = "redissonClientForShiro")
    RedissonClient redissonClient;

    private long globalSessionTimeout;

    /**
     * 创建session
     * @param session 会话
     * @return java.io.Serializable
     * @author lvyx
     * @since 2022/1/4 17:13
     **/
    @Override
    protected Serializable doCreate(Session session) {
        //创建唯一标识的sessionId
        Serializable sessionId = generateSessionId(session);
        //为session会话指定唯一的sessionId
        assignSessionId(session, sessionId);
        //放入缓存中
        String key = ShiroCacheEnum.SESSION_DAO.getValue()+sessionId.toString();
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.trySet(LSerializeUtils.serialize(session), globalSessionTimeout/1000, TimeUnit.SECONDS);
        return sessionId;
    }

    /**
     * 读取Session
     * @param sessionId session唯一标识
     * @return org.apache.shiro.session.Session
     * @author lvyx
     * @since 2022/1/4 17:13
     **/
    @Override
    protected Session doReadSession(Serializable sessionId) {
        String key = ShiroCacheEnum.SESSION_DAO.getValue()+sessionId.toString();
        RBucket<String> bucket = redissonClient.getBucket(key);
        return (Session) LSerializeUtils.deserialize(bucket.get());
    }

    /**
     * 更新session对象
     * @param session session对象
     * @author lvyx
     * @since 2022/1/4 17:14
     **/
    @Override
    public void update(Session session) throws UnknownSessionException {
        String key = ShiroCacheEnum.SESSION_DAO.getValue()+session.getId().toString();
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(LSerializeUtils.serialize(session), globalSessionTimeout/1000, TimeUnit.SECONDS);
    }

    /**
     * 删除session
     * @param session session对象
     * @author lvyx
     * @since 2022/1/4 17:15
     **/
    @Override
    public void delete(Session session) {
        String key = ShiroCacheEnum.SESSION_DAO.getValue()+session.getId().toString();
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }

    /**
     * 统计当前活跃用户数(后续扩展)
     * @return java.util.Collection<org.apache.shiro.session.Session>
     * @author lvyx
     * @since 2022/1/4 17:16
     **/
    @Override
    public Collection<Session> getActiveSessions() {
        return Collections.emptySortedSet();
    }

    public void setGlobalSessionTimeout(long globalSessionTimeout) {
        this.globalSessionTimeout = globalSessionTimeout;
    }
}