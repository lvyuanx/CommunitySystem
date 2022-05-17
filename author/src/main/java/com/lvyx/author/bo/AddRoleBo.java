package com.lvyx.author.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *  添加管理角色Bo
 * </p>
 *
 * @author lvyx
 * @since 2022-05-04 23:29:33
 */
@Data
public class AddRoleBo {

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色描述")
    private String description;

    @ApiModelProperty("期id")
    private List<String> periodIds;
}
