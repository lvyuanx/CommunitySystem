package com.lvyx.community.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 户VO
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:09:04
 */
@Data
public class HouseholdVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("层Id")
    private String layerId;

    @ApiModelProperty("户")
    private String householdNumber;

    @ApiModelProperty("排序")
    private Integer sortNo;

    @ApiModelProperty("用户")
    private List<HouseholdUserVo> householdUserVoList;
}
