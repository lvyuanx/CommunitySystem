package com.lvyx.author.controller;


import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.author.AuthorUrls;
import com.lvyx.author.bo.AddRoleBo;
import com.lvyx.author.entity.Resource;
import com.lvyx.author.service.RoleService;
import com.lvyx.author.vo.CommunityRoleVo;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.SuccessResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户角色表 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Api(tags = "角色管理")
@ApiSupport(order = 2)
@RestController
@RequestMapping(AuthorUrls.RoleCtrl.BASE_URL)
@Slf4j
public class RoleController {

    @javax.annotation.Resource
    private RoleService roleService;

    @LLogger(description = "查询用户功能")
    @ApiOperation(value = "查询用户功能")
    @ApiOperationSupport(order = 1)
    @GetMapping(AuthorUrls.RoleCtrl.FIND_USER_FUNCTION)
    public Result<List<Resource>> findUserFunction() {
        List<Resource> currentUserFunction = new ArrayList<>();
        try {
            currentUserFunction = roleService.findCurrentUserFunction();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("查询用户功能失败");
        }
        return new SuccessResult<>("查询用户功能成功", currentUserFunction);
    }

    @LLogger(description = "添加社区角色")
    @ApiOperation(value = "添加社区角色")
    @ApiOperationSupport(order = 2)
    @PostMapping(AuthorUrls.RoleCtrl.ADD_COMMUNITY_ROLE)
    public Result<String> addCommunityRole(@RequestBody AddRoleBo addRoleBo) {
        try {
            roleService.editCommunityRole(addRoleBo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("添加社区角色失败");
        }
        return new SuccessResult<>("操作成功");
    }

    @LLogger(description = "查询社区角色")
    @ApiOperation(value = "查询社区角色")
    @ApiOperationSupport(order = 2)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "起始页",  defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小",  defaultValue = "10"),
            @ApiImplicitParam(name = "name", value = "角色名称")
    })
    @GetMapping(AuthorUrls.RoleCtrl.FIND_COMMUNITY_ROLE)
    public Result<PageInfo<CommunityRoleVo>> findCommunityRole(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                               @RequestParam(value = "pageSize",required = false,  defaultValue = "10") Integer pageSize,
                                                               @RequestParam(value = "name", required = false, defaultValue = "") String name) {
        PageInfo<CommunityRoleVo> communityRoleVoPageInfo = new PageInfo<>();
        try {
            communityRoleVoPageInfo = roleService.findCommunityRole(pageNum, pageSize, name);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("查询社区角色失败");
        }
        return new SuccessResult<>("查询社区角色成功", communityRoleVoPageInfo);
    }

    @LLogger(description = "删除角色")
    @ApiOperation(value = "删除角色")
    @ApiOperationSupport(order = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id")
    })
    @GetMapping(AuthorUrls.RoleCtrl.DELETE_ROLE)
    public Result<String> deleteRole(@RequestParam String roleId) {
        try {
            roleService.deleteRole(roleId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("删除角色失败");
        }
        return new SuccessResult<>("删除角色成功");
    }

    @LLogger(description = "编辑用户角色")
    @ApiOperation(value = "编辑用户角色")
    @ApiOperationSupport(order = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "userId"),
            @ApiImplicitParam(name = "roleIds", value= "角色ids")
    })
    @GetMapping(AuthorUrls.RoleCtrl.EDIT_USER_ROLE)
    public Result<String> editUserRole(@RequestParam String userId,
                                     @RequestParam String roleIdStr) {
        try {
            String[] roleIds = roleIdStr.split(",");
            roleService.editUserRole(userId, Arrays.asList(roleIds));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("编辑用户角色失败");
        }
        return new SuccessResult<>("编辑用户角色成功");
    }

}
