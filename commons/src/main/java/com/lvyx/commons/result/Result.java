package com.lvyx.commons.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 统一返回结果抽象类
 * </p>
 *
 * @author lvyx
 * @date 2021-12-28 09:31:50
 */
@ApiModel(
        value = "数据返回封装",
        description = "数据返回封装"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Result<T> implements Serializable {
    @ApiModelProperty("状态编码")
    private Integer code;

    @ApiModelProperty("提示信息")
    private String message;

    @ApiModelProperty("返回结果")
    private T data;

    public Result(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}