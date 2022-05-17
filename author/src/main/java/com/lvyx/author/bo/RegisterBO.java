package com.lvyx.author.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 注册请求参数
 * </p>
 *
 * @author lvyx
 * @since 2022-01-23 13:28:35
 */
@Data
public class RegisterBO implements Serializable {

    @ApiModelProperty("登录名称")
    private String loginName;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("性别（0：女，1：男）")
    private Integer sex;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("固定电话")
    private String tel;

    @ApiModelProperty("电话")
    private String mobil;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("确认密码")
    private String confirmPassword;

    @ApiModelProperty("验证码id")
    private String captchaId;

    @ApiModelProperty("验证码用途")
    private String purpose;

    @ApiModelProperty("验证码id")
    private String captcha;

}
