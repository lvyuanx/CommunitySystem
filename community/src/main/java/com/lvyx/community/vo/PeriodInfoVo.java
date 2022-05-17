package com.lvyx.community.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 社区详情
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:22:53
 */
@ApiModel(value = "期详情", description = "期详情")
@Data
public class PeriodInfoVo implements Serializable {

    @ApiModelProperty("栋Id")
    private String buildingId;

    @ApiModelProperty("栋")
    private String buildingNumber;

    @ApiModelProperty("排序")
    private Integer sortNo;

    @ApiModelProperty("单元详情")
    private List<InfoVo> infoVoList;
}

