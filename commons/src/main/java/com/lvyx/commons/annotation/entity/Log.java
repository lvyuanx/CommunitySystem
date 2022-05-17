package com.lvyx.commons.annotation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Getter
@Setter
@TableName("l_log")
@ApiModel(value = "Log对象", description = "日志表")
public class Log {

    @ApiModelProperty("日志id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("方法注释")
    @TableField("method_doc")
    private String methodDoc;

    @ApiModelProperty("方法名称")
    @TableField("method_name")
    private String methodName;

    @ApiModelProperty("运行时间（ms）")
    @TableField("run_time")
    private Long runTime;

    @ApiModelProperty("请求ip")
    @TableField("ip")
    private String ip;

    @ApiModelProperty("请求地址")
    @TableField("url")
    private String url;

    @ApiModelProperty("请求类型（GET,PUT,POST,DELETE）")
    @TableField("request_type")
    private String requestType;

    @ApiModelProperty("类路径")
    @TableField("class_path")
    private String classPath;

    @ApiModelProperty("方法入参")
    @TableField("method_param")
    private String methodParam;

    @ApiModelProperty("方法返回值")
    @TableField("method_return")
    private String methodReturn;

    @ApiModelProperty("错误信息")
    @TableField("error_message")
    private String errorMessage;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
