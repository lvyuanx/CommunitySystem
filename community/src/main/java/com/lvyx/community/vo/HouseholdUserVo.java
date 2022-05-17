package com.lvyx.community.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 居民VO
 * </p>
 *
 * @author lvyx
 * @since 2022-02-06 16:09:04
 */
@Data
public class HouseholdUserVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("户Id")
    private String householdId;

    @ApiModelProperty("用户")
    private String userId;

    @ApiModelProperty("排序")
    private Integer sortNo;
}
