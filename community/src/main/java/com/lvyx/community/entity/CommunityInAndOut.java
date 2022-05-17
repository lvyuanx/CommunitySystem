package com.lvyx.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 小区-进出登记表
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
@Getter
@Setter
@TableName("l_community_in_and_out")
@ApiModel(value = "CommunityInAndOut对象", description = "小区-进出登记表")
public class CommunityInAndOut implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("进入或离开（0：进入，1：离开）")
    @TableField("IN_OR_OUT")
    private Integer inOrOut;

    @ApiModelProperty("是否健康（0：不健康，1：健康）")
    @TableField("IS_HEALTH")
    private Integer isHealth;

    @ApiModelProperty("异常体温")
    @TableField("ABNORMAL_BODY_TEMPERATURE")
    private BigDecimal abnormalBodyTemperature;

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
