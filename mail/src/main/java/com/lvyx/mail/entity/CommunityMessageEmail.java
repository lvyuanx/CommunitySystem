package com.lvyx.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 社区邮件信息表
 * </p>
 *
 * @author lvyx
 * @since 2022-05-01
 */
@Getter
@Setter
@TableName("l_community_message_email")
@ApiModel(value = "CommunityMessageEmail对象", description = "社区邮件信息表")
public class CommunityMessageEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("消息主键")
    @TableField("MESSAGE_ID")
    private String messageId;

    @ApiModelProperty("发送邮箱")
    @TableField("FROM_EMAIL")
    private String fromEmail;

    @ApiModelProperty("发送者")
    @TableField("FROM_USER")
    private String fromUser;

    @ApiModelProperty("接收邮箱")
    @TableField("TO_EMAIL")
    private String toEmail;

    @ApiModelProperty("接收者")
    @TableField("TO_USER")
    private String toUser;

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
