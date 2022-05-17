package com.lvyx.community.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 栋VO
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:09:04
 */
@Data
public class BuildingVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("期Id")
    private String periodId;

    @ApiModelProperty("第几栋")
    private String buildingNumber;

    @ApiModelProperty("排序")
    private Integer sortNo;

    @ApiModelProperty("单元")
    private List<UnitVo> unitVoList;
}
