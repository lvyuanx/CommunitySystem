package com.lvyx.author.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 社区角色
 * </p>
 *
 * @author lvyx
 * @since 2022-05-05 00:36:36
 */
@Data
public class CommunityRoleVo implements Serializable {

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("角色名称")
    private String RoleName;

    @ApiModelProperty("角色描述")
    private String description;

    @ApiModelProperty("资源列表")
    private List<CommunityResourceVo> communityResourceVoList;

}
