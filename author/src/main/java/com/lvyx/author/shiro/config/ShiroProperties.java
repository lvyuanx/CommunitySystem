package com.lvyx.author.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * shiro配置类
 * </p>
 *
 * @author lvyx
 * @since 2022-01-13 14:22:54
 */
@Data
@Component
@ConfigurationProperties(prefix = "lvyx.shiro")
public class ShiroProperties {

    /**
     * 在线人数
     * @since 2022/1/13 14:24
     **/
    private Integer online = 1;

}
