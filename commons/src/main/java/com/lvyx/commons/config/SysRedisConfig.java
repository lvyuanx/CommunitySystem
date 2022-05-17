package com.lvyx.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <p>
 * 系统缓存客户端配置类
 * </p>
 *
 * @author lvyx
 * @since 2022-01-17 09:43:11
 */
@Slf4j
@Configuration
public class SysRedisConfig {

    @Resource
    private SysRedisProperties sysRedisProperties;

    /**
     * 创建Redis客户供系统使用
     * @return org.redisson.api.RedissonClient
     * @author lvyx
     * @since 2021/12/31 8:50
     **/
    @Bean("redissonClientSystem")
    public RedissonClient redissonClient() {
        log.info("=====初始化redissonClientForSystem开始======");
        String[] nodeList = sysRedisProperties.getNodes().split(",");
        Config config = new Config();
        if (nodeList.length == 1) {
            // 单一节点
            config.useSingleServer().setAddress(nodeList[0])
                    .setConnectTimeout(sysRedisProperties.getConnectTimeout())
                    .setConnectionMinimumIdleSize(sysRedisProperties.getConnectionMinimumidleSize())
                    .setConnectionPoolSize(sysRedisProperties.getConnectPoolSize()).setTimeout(sysRedisProperties.getTimeout());
        } else {
            // 集群环境
            config.useClusterServers().addNodeAddress(nodeList)
                    .setConnectTimeout(sysRedisProperties.getConnectTimeout())
                    .setMasterConnectionMinimumIdleSize(sysRedisProperties.getConnectionMinimumidleSize())
                    .setMasterConnectionPoolSize(sysRedisProperties.getConnectPoolSize()).setTimeout(sysRedisProperties.getTimeout());
        }
        RedissonClient redissonClient =  Redisson.create(config);
        log.info("=====初始化redissonClientForSystem完成======");
        return redissonClient;
    }
}
