package com.lvyx.community.controller;


import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.community.CommunityUrls;
import com.lvyx.community.entity.CommunityTemperatureRegistration;
import com.lvyx.community.service.CommunityTemperatureRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 小区-体温登记表 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
@Slf4j
@Api(tags = "社区-体温登记")
@ApiSupport(order = 7)
@RestController
@RequestMapping(CommunityUrls.TemperatureRegistrationCtrls.BASE_URL)
public class CommunityTemperatureRegistrationController {

    @Resource
    private CommunityTemperatureRegistrationService communityTemperatureRegistrationService;

    @LLogger(description = "添加体温信息", params = {"社区-体温登记信息"})
    @ApiOperation("添加体温信息")
    @ApiOperationSupport(order = 1)
    @PostMapping(CommunityUrls.TemperatureRegistrationCtrls.ADD)
    public Result<String> add(@RequestBody CommunityTemperatureRegistration communityTemperatureRegistration){
        try {
            communityTemperatureRegistrationService.add(communityTemperatureRegistration);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("添加体温信息异常");
        }
        return new SuccessResult<>("添加体温信息成功");
    }

    @LLogger(description = "查询当前登陆人连续打卡天数")
    @ApiOperation("查询当前登陆人连续打卡天数")
    @ApiOperationSupport(order = 2)
    @GetMapping(CommunityUrls.TemperatureRegistrationCtrls.CONTINUOUS_DAY)
    public Result<String> continuousDay(){
        Integer continuousDay;
        try {
            continuousDay = communityTemperatureRegistrationService.continuousDay();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询当前登陆人连续打卡天数异常");
        }
        return new SuccessResult<>("查询当前登陆人连续打卡天数成功", continuousDay.toString());
    }

    @LLogger(description = "查询打卡记录", params = {"起始页", "页面大小", "提交状态"})
    @ApiOperation("查询打卡记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "起始页",  defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小",  defaultValue = "10"),
            @ApiImplicitParam(name = "isEnable", value = "提交状态"),
            @ApiImplicitParam(name = "isHealth", value = "是否健康"),
    })
    @ApiOperationSupport(order = 3)
    @GetMapping(CommunityUrls.TemperatureRegistrationCtrls.FIND_LIST)
    public Result<PageInfo<CommunityTemperatureRegistration>> findList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                                                                       @RequestParam(value = "isEnable", required = false) Integer isEnable,
                                                                       @RequestParam(value = "isHealth", required = false) Integer isHealth){
        PageInfo<CommunityTemperatureRegistration> communityTemperatureRegistrationPageInfo = new PageInfo<>();
        try {
            communityTemperatureRegistrationPageInfo = communityTemperatureRegistrationService.findList(pageNum, pageSize, isEnable, isHealth);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询打卡记录异常");
        }
        return new SuccessResult<>("查询打卡记录成功", communityTemperatureRegistrationPageInfo);
    }



}
