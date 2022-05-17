package com.lvyx.community.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 单元VO
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:09:04
 */
@Data
public class UnitVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("栋Id")
    private String buildingId;

    @ApiModelProperty("单元")
    private String unitNumber;

    @ApiModelProperty("排序")
    private Integer sortNo;

    @ApiModelProperty("层")
    private List<LayerVo> layerVoList;
}
