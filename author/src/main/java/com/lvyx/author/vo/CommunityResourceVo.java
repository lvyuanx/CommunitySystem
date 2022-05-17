package com.lvyx.author.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 社区资源vo
 * </p>
 *
 * @author lvyx
 * @since 2022-05-05 00:49:41
 */
@Data
public class CommunityResourceVo implements Serializable {

    @ApiModelProperty("资源id")
    private String resourceId;

    @ApiModelProperty("社区期id")
    private String parentId;

    @ApiModelProperty("社区期名称")
    private String periodName;
}
