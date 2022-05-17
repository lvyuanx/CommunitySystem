package com.lvyx.commons.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  角色vo
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:09:04
 */
@Data
public class RoleVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色标识")
    private String label;

    @ApiModelProperty("角色描述")
    private String description;

}
