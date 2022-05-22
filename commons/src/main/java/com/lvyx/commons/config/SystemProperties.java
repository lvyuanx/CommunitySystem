package com.lvyx.commons.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 系统配置
 * </p>
 *
 * @author lvyx
 * @since 2022-01-31 16:59:21
 */
@Component
@ConfigurationProperties(
        prefix = "lvyx.system"
)
@Data
public class SystemProperties {

    /**
     * 文件服务器地址
     */
    private String filePath = "D:/CommunitySystem";

    /**
     * 上传文件跟目录
     */
    private String uploadModule = "UPLOAD";

    /**
     * 静态资源根目录
     */
    private String staticPath = "STATIC";

    /**
     * 社区logo路径
     **/
    private String logoPath = "D:/CommunitySystem/STATIC/logo/logo.png";

    /**
     * 系统开发环境
     **/
    private String active = "test";

    /**
     * 系统测试环境验证码
     **/
    private String testCaptchaCode = "0715";

    /**
     * 系统重置默认密码
     **/
    private String systemPassword = "Lv@12345";

    /**
     * 是否开启验证码登录
     **/
    private Boolean isCaptcha = true;
}
