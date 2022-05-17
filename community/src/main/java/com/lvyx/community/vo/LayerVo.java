package com.lvyx.community.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 层VO
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:09:04
 */
@Data
public class LayerVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("单元Id")
    private String unitId;

    @ApiModelProperty("层")
    private String layerNumber;

    @ApiModelProperty("排序")
    private Integer sortNo;

    @ApiModelProperty("户")
    private List<HouseholdVo> householdVoList;
}
