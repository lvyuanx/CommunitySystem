package com.lvyx.community.controller;


import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.community.CommunityUrls;
import com.lvyx.community.bo.QueryUserHouseholdBo;
import com.lvyx.community.service.CommunityHouseholdUserService;
import com.lvyx.community.vo.UserHouseholdVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户与户关联表 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Slf4j
@Api(tags = "社区-户关联用户")
@ApiSupport(order = 6)
@RestController
@RequestMapping(CommunityUrls.HouseholdUserCtrls.BASE_URL)
public class CommunityHouseholdUserController {

    @Resource
    private CommunityHouseholdUserService communityHouseholdUserService;

    @LLogger(description = "查询用户关联住址信息", params = {"查询限制条件", "起始页", "页面大小"})
    @ApiOperation("查询用户关联住址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "起始页",  defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小",  defaultValue = "10"),
    })
    @ApiOperationSupport(order = 1)
    @PostMapping(CommunityUrls.HouseholdUserCtrls.FIND_USER_HOUSEHOLD)
    public Result<PageInfo<UserHouseholdVo>> findUserHousehold(@RequestBody QueryUserHouseholdBo queryUserHouseholdBo,
                                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize){
        PageInfo<UserHouseholdVo> userHousehold = new PageInfo<>();
        try {
            userHousehold = communityHouseholdUserService.findUserHousehold(queryUserHouseholdBo, pageNum, pageSize);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询用户关联住址信息");
        }
        return new SuccessResult<>("查询用户关联住址信息", userHousehold);
    }

    @LLogger(description = "修改用户住址信息", params = {"用户id", "新的户id"})
    @ApiOperation("修改用户住址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id"),
            @ApiImplicitParam(name = "householdId", value = "新的户id"),
            @ApiImplicitParam(name = "isEnable", value = "是否启用"),
    })
    @ApiOperationSupport(order = 4)
    @GetMapping(CommunityUrls.HouseholdUserCtrls.UPDATE_HOUSEHOLD_USER)
    public Result<String> updateHouseholdUser(@RequestParam(value = "userId") String userId,
                                               @RequestParam(value = "householdId") String householdId,
                                              @RequestParam(value = "isEnable", required = false) Integer isEnable){
        try {
            communityHouseholdUserService.updateUserHousehold(userId, householdId, isEnable);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("修改用户住址信息异常");
        }
        return new SuccessResult<>("修改用户住址信息成功");
    }

}
