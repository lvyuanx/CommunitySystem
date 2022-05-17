package com.lvyx.community.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 查询异常请求参数封装
 * </p>
 *
 * @author lvyx
 * @since 2022-05-02 15:03:50
 */
@Data
public class QueryExceptionBo implements Serializable {

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("异常状态（0：未开始，1：处理中，2：已结束）")
    private Integer status;

    @ApiModelProperty("异常类型（0：体温异常，1：其他异常）")
    private Integer exceptionType;

    @ApiModelProperty("期主键")
    private List<String> periodIds;

}
