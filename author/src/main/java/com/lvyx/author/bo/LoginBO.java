package com.lvyx.author.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户登录参数
 * </p>
 *
 * @author lvyx
 * @since 2021-12-28 10:25:23
 */
@Data
@ApiModel("用户登录参数")
public class LoginBO {

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "验证码", required = true)
    private String captcha;

    @ApiModelProperty(value = "是否记住我", required = true)
    private Boolean rememberFlag;

    @ApiModelProperty(value = "验证码Id", required = true)
    private String captchaId;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

}
