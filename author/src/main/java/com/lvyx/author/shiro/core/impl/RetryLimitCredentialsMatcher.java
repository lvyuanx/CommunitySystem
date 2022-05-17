package com.lvyx.author.shiro.core.impl;

import com.lvyx.author.shiro.utils.ShiroUserUtils;
import com.lvyx.commons.enums.ShiroCacheEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 自定义shiro密码比较器，实现密码重试次数限制
 * </p>
 *
 * @author lvyx
 * @since 2022-01-10 14:47:50
 */
@Slf4j
public class RetryLimitCredentialsMatcher extends SimpleCredentialsMatcher {

    private RedissonClient redissonClient;

    /**
     * 最大重试次数
     * @since 2022/1/10 14:54
     **/
    private static final Long RETRY_LIMIT_NUM = 4L;

    /**
     * 构造函数
     * @param redissonClient redis客户端
     * @author lvyx
     * @since 2022/1/10 14:51
     **/
    public RetryLimitCredentialsMatcher(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }


    /**
     * 重写比较方法
     * @param token 身份凭证
     * @param info  身份信息
     * @return boolean
     * @author lvyx
     * @since 2022/1/10 14:52
     **/
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获得登录名
        String loginName = (String) token.getPrincipal();
        // 获得重试次数key
        String key = ShiroCacheEnum.PWD_RETRY_COUNT.getValue() + loginName;
        // 获得缓存
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        log.info("{}登录重试次数：{}", loginName, atomicLong);
        long retryFlag = atomicLong.get();
        // 判断次数
        if (retryFlag>RETRY_LIMIT_NUM){
            // 超过次数设计10分钟后重试
            atomicLong.expire(300, TimeUnit.SECONDS);
            throw new ExcessiveAttemptsException("密码错误超过5次，请5分钟以后再试");
        }
        // 累加次数
        atomicLong.incrementAndGet();
        // 调用父类的密码比较
        boolean flag =  super.doCredentialsMatch(token, info);
        if (flag){
            // 校验成功删除限制
            atomicLong.delete();
            // 使用Redis客户得到
            RDeque<Object> deque = redissonClient.getDeque(ShiroCacheEnum.USER_QUEUE.getValue() + loginName);
            // 判断当前用户的SessionId是否已经在队列中
            boolean isOnline = deque.contains(ShiroUserUtils.getSessionId());
            if (!isOnline){
                // 不存在就加入到队尾
                deque.addLast(ShiroUserUtils.getSessionId());
            }
        }
        return flag;
    }


}
