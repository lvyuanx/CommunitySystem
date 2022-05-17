package com.lvyx.author.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * jwt令牌配置
 * </p>
 *
 * @author lvyx
 * @since 2022-01-14 09:25:25
 */
@Data
@Component
@ConfigurationProperties(prefix = "lvyx.shiro.jwt")
public class JWTProperties {

    /**
     * 签名密码
     * @since 2022/1/14 9:26
     **/
    private String hexEncodedSecretKey = "helloworld-community-system";

    /**
     * 是否仅仅使用jwt令牌一种方式登录
     * @since 2022/1/14 16:55
     **/
    private boolean isOnlyJwt = true;
}
