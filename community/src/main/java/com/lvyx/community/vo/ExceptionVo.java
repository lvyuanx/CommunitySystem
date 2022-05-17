package com.lvyx.community.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 异常信息返回实体
 * </p>
 *
 * @author lvyx
 * @since 2022-05-02 15:13:16
 */
@Data
public class ExceptionVo implements Serializable {

    @ApiModelProperty("用户主键")
    private String userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("用户联系电话")
    private String userMobile;

    @ApiModelProperty("处理人")
    private String updateUserName;

    @ApiModelProperty("处理人联系电话")
    private String updateUserMobile;

    @ApiModelProperty("主键")
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("异常类型（0：体温异常，1：其它异常）")
    private Integer exceptionType;

    @ApiModelProperty("异常描述")
    private String exceptionDescription;

    @ApiModelProperty("是否处理中（0：未开始,1：处理中，2：已结束）")
    private Integer isDealWith;

    @ApiModelProperty("处理结果")
    private String result;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
