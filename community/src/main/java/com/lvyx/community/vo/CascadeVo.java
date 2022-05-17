package com.lvyx.community.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *  级联选择结构
 * </p>
 *
 * @author lvyx
 * @since 2022-02-08 06:00:13
 */
@Data
@ApiModel(value = "CascadeVo对象", description = "级联选择对象")
public class CascadeVo {

    @ApiModelProperty("显示内容")
    private String text;

    @ApiModelProperty("内容ID")
    private String value;

    @ApiModelProperty("排序")
    private String sortNo;

    @ApiModelProperty("下级结构")
    private List<CascadeVo> children;
}
