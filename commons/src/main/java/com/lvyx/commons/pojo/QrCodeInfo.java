package com.lvyx.commons.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 二维码信息
 * </p>
 *
 * @author lvyx
 * @since 2022-04-24 19:38:54
 */
@Data
@ApiModel( value = "二维码信息对象", description = "二维码信息对象" )
public class QrCodeInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "二维码类型")
    private String codeType;

    @ApiModelProperty(value = "二维码类型名称")
    private String codeTypeName;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "联系方式")
    private String mobile;


}
