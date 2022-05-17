package com.lvyx.mail.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 邮件信息vo
 * </p>
 *
 * @author lvyx
 * @since 2022-05-01 15:26:45
 */
@Data
public class MessageVo implements Serializable {

    @ApiModelProperty("消息主键")
    private String messageId;

    @ApiModelProperty("邮件主键")
    private String emailId;

    @ApiModelProperty("消息主题")
    private String subject;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("是否发送邮件（0：发送，1：不发送）")
    private Integer isEmail;


    @ApiModelProperty("是否有效（0：无效（未读），1：有效（已读））")
    private Integer isEnable;

    @ApiModelProperty("发送邮箱")
    @TableField("FROM_EMAIL")
    private String fromEmail;

    @ApiModelProperty("发送者")
    private String fromUser;

    @ApiModelProperty("发送者名称")
    private String fromUserName;

    @ApiModelProperty("接收邮箱")
    private String toEmail;

    @ApiModelProperty("接收者")
    private String toUser;

    @ApiModelProperty("接收者名称")
    private String toUserName;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date emailCreateDate;


}
