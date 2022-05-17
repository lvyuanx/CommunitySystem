package com.lvyx.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 小区-体温登记表
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
@Getter
@Setter
@TableName("l_community_temperature_registration")
@ApiModel(value = "CommunityTemperatureRegistration对象", description = "小区-体温登记表")
public class CommunityTemperatureRegistration implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("体温")
    @TableField("TEMPERATURE")
    private Float temperature;

    @ApiModelProperty("登记地点")
    @TableField("REGISTRATION_PLACE")
    private String registrationPlace;

    @ApiModelProperty("14天内是否去过高风险地区")
    @TableField("IS_TO_HIGH_RISK")
    private Integer isToHighRisk;

    @ApiModelProperty("高风险地区名称")
    @TableField("TO_HIGH_RISK_ADDRESS")
    private String toHighRiskAddress;

    @ApiModelProperty("连续打卡多少天")
    @TableField("CONTINUOUS")
    private Integer continuous;

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
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
