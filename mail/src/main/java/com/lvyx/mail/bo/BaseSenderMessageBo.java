package com.lvyx.mail.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 邮件发送，请求参数
 * </p>
 *
 * @author lvyx
 * @since 2022-05-01 15:13:21
 */
@Data
@ApiModel(value = "BaseSenderMailBo对象", description = "邮件发送，请求参数")
public class BaseSenderMessageBo implements Serializable {

    @ApiModelProperty("消息发送者")
    private String from;

    @ApiModelProperty("是否邮件发送")
    private Integer isSenderEmail;

    @ApiModelProperty("消息接收者")
    private List<String> to;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("消息主题")
    private String subject;



}
