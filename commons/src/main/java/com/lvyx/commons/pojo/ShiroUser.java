package com.lvyx.commons.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 * @author lvyx
 */
@Data
@NoArgsConstructor
@ApiModel( value = "ShiroUser对象", description = "用户权限性相关信息")
public class ShiroUser implements Serializable {


	@ApiModelProperty(value = "主键")
	private String id;

	@ApiModelProperty(value = "登录名称")
	private String loginName;

	@ApiModelProperty(value = "真实姓名")
	private String realName;

	@ApiModelProperty(value = "昵称")
	private String nickName;

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "性别")
	private Integer sex;

	@ApiModelProperty(value = "邮箱")
	private String zipcode;

	@ApiModelProperty(value = "地址")
	private String address;

	@ApiModelProperty(value = "固定电话")
	private String tel;

	@ApiModelProperty(value = "电话")
	private String mobil;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "职务")
	private String duties;

	@ApiModelProperty("头像")
	private String avatar;

	@ApiModelProperty(value = "排序")
	private Integer sortNo;

	@ApiModelProperty(value = "是否有效")
	private Integer isEnable;

	@ApiModelProperty(value = "资源ids")
	private List<String> resourceIds;

	@ApiModelProperty(value = "角色名称")
	private List<String> roleName;

	@ApiModelProperty(value = "角色信息")
	private List<RoleVo> roleVoList;




	public ShiroUser(String id, String loginName) {
		super();
		this.id = id;
		this.loginName = loginName;
	}
	
}
