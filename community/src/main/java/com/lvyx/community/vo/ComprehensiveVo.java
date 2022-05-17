package com.lvyx.community.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 综合信息VO
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:09:04
 */
@Data
public class ComprehensiveVo implements Serializable {


    @ApiModelProperty("栋id")
    private String buildingId;

    @ApiModelProperty("栋名称")
    private String buildingNumber;

    @ApiModelProperty("栋名称")
    private Integer sortNo;

    @ApiModelProperty("户信息")
    private List<UnitVo> unitVoList;


}
