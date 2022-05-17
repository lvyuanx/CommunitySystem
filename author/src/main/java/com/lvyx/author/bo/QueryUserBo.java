package com.lvyx.author.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 查询用户房间信息实体
 * </p>
 *
 * @author lvyx
 * @since 2022-04-25 21:44:43
 */
@Data
public class QueryUserBo implements Serializable {

    @ApiModelProperty("用户主键")
    private String userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户电话号码")
    private String userMobile;

    @ApiModelProperty("是否启用（未启用表示待审核状态，启用表示审核通过状态）")
    private String isEnable;

    @ApiModelProperty("期主键")
    private String periodId;

    @ApiModelProperty("栋主键")
    private String buildingId;

    @ApiModelProperty("单元主键")
    private String unitId;

    @ApiModelProperty("层主键")
    private String layerId;

    @ApiModelProperty("房间主键")
    private String householdId;

}
