package com.lvyx.community.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  用户住址
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:09:04
 */
@Data
public class AddressVo implements Serializable {

    @ApiModelProperty("期主键")
    private String periodId;

    @ApiModelProperty("第几期")
    private String periodNumber;

    @ApiModelProperty("栋主键")
    private String buildingId;

    @ApiModelProperty("第几栋")
    private String buildingNumber;

    @ApiModelProperty("单元主键")
    private String unitId;

    @ApiModelProperty("第几单元")
    private String unitNumber;

    @ApiModelProperty("层主键")
    private String layerId;

    @ApiModelProperty("第几层")
    private String layerNumber;

    @ApiModelProperty("房间主键")
    private String householdId;

    @ApiModelProperty("房价好")
    private String householdNumber;

    @ApiModelProperty("用户Id")
    private String userId;

}
