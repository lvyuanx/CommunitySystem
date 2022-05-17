package com.lvyx.author.vo;


import com.lvyx.commons.pojo.ShiroUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 登录信息返回结果封装
 * </p>
 *
 * @author lvyx
 * @since 2022-01-15 17:40:07
 */
@ApiModel(value="LogInfoVO对象", description="登录用户数据")
@Data
public class LoginInfoVO implements Serializable {


    @ApiModelProperty("登录token的id")
    private String token;


    @ApiModelProperty("登录的用户信息")
    private ShiroUser shiroUser;


}
