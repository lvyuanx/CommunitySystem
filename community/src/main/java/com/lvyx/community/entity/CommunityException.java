package com.lvyx.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 社区异常表
 * </p>
 *
 * @author lvyx
 * @since 2022-05-02
 */
@Getter
@Setter
@TableName("l_community_exception")
@ApiModel(value = "CommunityException对象", description = "社区异常表")
public class CommunityException implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("异常类型（0：体温异常，1：其它异常）")
    @TableField("EXCEPTION_TYPE")
    private Integer exceptionType;

    @ApiModelProperty("异常描述")
    @TableField("EXCEPTION_DESCRIPTION")
    private String exceptionDescription;

    @ApiModelProperty("是否处理中（0：未开始,1：处理中，2：已结束）")
    @TableField("IS_DEAL_WITH")
    private Integer isDealWith;

    @ApiModelProperty("处理结果")
    @TableField("RESULT")
    private String result;

    @ApiModelProperty("排序")
    @TableField("SORT_NO")
    private Integer sortNo;

    @ApiModelProperty("是否有效（0：无效，1：有效）")
    @TableField("IS_ENABLE")
    private Integer isEnable;

    @ApiModelProperty("创建者")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty("创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @ApiModelProperty("修改者")
    @TableField("UPDATE_USER")
    private String updateUser;

    @ApiModelProperty("修改时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否删除（0：未删除，1：删除）")
    @TableField("IS_DELETE")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty("删除者")
    @TableField("DELETE_USER")
    private String deleteUser;

    @ApiModelProperty("删除时间")
    @TableField("DELETE_TIME")
    private LocalDateTime deleteTime;


}
