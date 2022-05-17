package com.lvyx.community.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 社区详情
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:22:53
 */
@ApiModel(value = "详情", description = "详情")
@Data
public class InfoVo implements Serializable {

    @ApiModelProperty("单元id")
    private String unitId;

    @ApiModelProperty("单元名称")
    private String unitNumber;

    @ApiModelProperty("总户数")
    private int allHousehold;

    @ApiModelProperty("总人口")
    private int allUser;

    @ApiModelProperty("危险等级")
    private int dangerGrade;

    @ApiModelProperty("绿码人数")
    private int greenCode;

    @ApiModelProperty("红码人数")
    private int redCode;

    @ApiModelProperty("灰码人数")
    private int greyCode;

    @ApiModelProperty("黄码人数")
    private int yellowCode;
}
