package com.lvyx.author.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 *     shiro Redis缓存配置
 * </p>
 * @author lvyx
 * @since 2021/12/30 15:59
 **/
@Data
@Component
@ConfigurationProperties(prefix = "lvyx.shiro.redis")
public class ShiroRedisProperties implements Serializable {

	/**
	 * redis连接地址，（集群环境多个redis连接使用逗号分割）
	 * @since 2021/12/30 15:59
	 */
	private String nodes =  "redis://127.0.0.1:6379";

	/**
	 * 获取连接超时时间
	 * @since 2021/12/30 15:59
	 */
	private int connectTimeout = 6000;

	/**
	 * 连接池大小
	 * @since 2021/12/30 15:59
	 */
	private int connectPoolSize = 150;

	/**
	 * 初始化连接数
	 * @since 2021/12/30 15:59
	 */
	private int connectionMinimumidleSize = 30;

	/**
	 * 等待数据返回超时时间
	 * @since 2021/12/30 15:59
	 */
	private int timeout = 6000;

	/**
	 *  全局超时时间
	 *  @since 2021/12/30 15:59
	 */
	private long globalSessionTimeout = 360000;

}