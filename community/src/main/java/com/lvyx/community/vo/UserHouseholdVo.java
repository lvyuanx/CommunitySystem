package com.lvyx.community.vo;

import com.lvyx.commons.pojo.ShiroUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户房间信息
 * </p>
 *
 * @author lvyx
 * @since 2022-04-25 21:38:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserHouseholdVo extends AddressVo implements Serializable {

    @ApiModelProperty("用户信息")
    private ShiroUser user;

    @ApiModelProperty("人员类型")
    private String roleTypeName;

}
