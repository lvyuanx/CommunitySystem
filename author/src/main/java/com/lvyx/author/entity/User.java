package com.lvyx.author.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lvyx.commons.encrypt_decrypt.LEncryptDecrypt;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Getter
@Setter
@TableName("l_user")
@ApiModel(value = "User对象", description = "用户表")
public class User implements Serializable {

    @ApiModelProperty("主键")
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("登录名称")
    @TableField("LOGIN_NAME")
    private String loginName;

    @ApiModelProperty("真实姓名")
    @TableField("REAL_NAME")
    private String realName;

    @ApiModelProperty("昵称")
    @TableField("NICK_NAME")
    private String nickName;

    @ApiModelProperty("密码")
    @TableField("PASSWORD")
    @LEncryptDecrypt
    private String password;

    @ApiModelProperty("性别（0：女，1：男）")
    @TableField("SEX")
    private Integer sex;

    @ApiModelProperty("邮箱")
    @TableField("ZIPCODE")
    private String zipcode;

    @ApiModelProperty("地址")
    @TableField("ADDRESS")
    private String address;

    @ApiModelProperty("固定电话")
    @TableField("TEL")
    private String tel;

    @ApiModelProperty("电话")
    @TableField("MOBIL")
    private String mobil;

    @ApiModelProperty("邮箱")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty("职务")
    @TableField("DUTIES")
    private String duties;

    @ApiModelProperty("头像")
    @TableField("AVATAR")
    private String avatar;

    @ApiModelProperty("期id")
    @TableField("PERIOD_ID")
    private String periodId;

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
