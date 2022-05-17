package com.lvyx.author.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 查询用户房间信息实体
 * </p>
 *
 * @author lvyx
 * @since 2022-04-25 21:44:43
 */
@Data
public class QueryUserListBo implements Serializable {

    @ApiModelProperty("用户主键")
    private String userId;

    @ApiModelProperty("用户名")
    private String searchName;

    @ApiModelProperty("页面状态")
    private Integer status;

    @ApiModelProperty("是否启用用户信息")
    private Integer isEnableUser;

    @ApiModelProperty("是否启用房间信息")
    private Integer isEnableHouseHold;

    @ApiModelProperty("期主键")
    private List<String> periodIds;


}
