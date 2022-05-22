package com.lvyx.community.controller;


import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.community.CommunityUrls;
import com.lvyx.community.entity.CommunityPeriod;
import com.lvyx.community.service.CommunityPeriodService;
import com.lvyx.community.vo.AddressVo;
import com.lvyx.community.vo.CascadeVo;
import com.lvyx.community.vo.PeriodInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 小区-期 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Slf4j
@Api(tags = "社区-期")
@ApiSupport(order = 1)
@RestController
@RequestMapping(CommunityUrls.PeriodCtrls.BASE_URL)
public class CommunityPeriodController {

    @Resource
    private CommunityPeriodService communityPeriodService;

    @LLogger(description = "添加期", params = {"社区-期信息"})
    @ApiOperation("添加期")
    @ApiOperationSupport(order = 1)
    @PostMapping(CommunityUrls.PeriodCtrls.ADD)
    public Result<CommunityPeriod> add(@RequestBody CommunityPeriod period){
        CommunityPeriod res = null;
        try {
            res = communityPeriodService.add(period);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("添加期异常");
        }
        return new SuccessResult<>("添加期成功", res);
    }


    @LLogger(description = "查询期")
    @ApiOperation("查询期")
    @ApiOperationSupport(order = 2)
    @GetMapping(CommunityUrls.PeriodCtrls.FIND)
    public Result<List<CommunityPeriod>> find(){
        List<CommunityPeriod> communityPeriods = null;
        try {
            communityPeriods = communityPeriodService.find();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询期异常");
        }
        return new SuccessResult<>("查询期成功", communityPeriods);
    }

    @LLogger(description = "查询所有期")
    @ApiOperation("查询所有期")
    @ApiOperationSupport(order = 3)
    @GetMapping(CommunityUrls.PeriodCtrls.FIND_ALL)
    public Result<List<CommunityPeriod>> findAll(){
        List<CommunityPeriod> communityPeriods = null;
        try {
            communityPeriods = communityPeriodService.findAll();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询所有期异常");
        }
        return new SuccessResult<>("查询所有期成功", communityPeriods);
    }

    @LLogger(description = "查询社区详情")
    @ApiOperation("查询社区详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "起始页", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", defaultValue = "5"),
            @ApiImplicitParam(name = "id", value = "期id", required = true)
    })
    @ApiOperationSupport(order = 4)
    @GetMapping(CommunityUrls.PeriodCtrls.FIND_COMMUNITY_INFO)
    public Result<PageInfo<PeriodInfoVo>> findCommunittyInfo(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                                            @RequestParam(value = "id") String id){
        PageInfo<PeriodInfoVo> infoByPeriodId = null;
        try {
            infoByPeriodId = communityPeriodService.getInfoByPeriodId(id, pageSize, pageNum);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询社区详情异常");
        }
        return new SuccessResult<>("查询社区详情成功", infoByPeriodId);
    }


    @LLogger(description = "查询社区级联详情")
    @ApiOperation("查询社区级联详情")
    @ApiOperationSupport(order = 5)
    @GetMapping(CommunityUrls.PeriodCtrls.FIND_ALL_COMMUNITY_INFO)
    public Result<List<CascadeVo>> findAllCommunityInfo(){
        List<CascadeVo> cascadeVo = null;
        try {
            cascadeVo = communityPeriodService.getCascadeVo();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询社区级联详情异常");
        }
        return new SuccessResult<>("查询社区级联详情成功", cascadeVo);
    }


    @LLogger(description = "查询登录用户住址信息")
    @ApiOperation("查询登录用户住址信息")
    @ApiOperationSupport(order = 6)
    @GetMapping(CommunityUrls.PeriodCtrls.FIND_INFO_BY_USER)
    public Result<AddressVo> findUserByInfo(){
        AddressVo infoByUser = null;
        try {
            infoByUser = communityPeriodService.findInfoByUser();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询登录用户住址信息异常");
        }
        return new SuccessResult<>("查询登录用户住址信息成功", infoByUser);
    }


    @LLogger(description = "查询登录用户是否含有住址信息")
    @ApiOperation("查询登录用户是否含有住址信息")
    @ApiOperationSupport(order = 6)
    @GetMapping(CommunityUrls.PeriodCtrls.CHECK_USER_HAS_ADDRESS)
    public Result<Integer> checkUserHasAddress(){
        Integer status = null;
        try {
            status = communityPeriodService.checkUserHasAddress();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询登录用户是否含有住址信息异常");
        }
        return new SuccessResult<>("查询登录用户是否含有住址信息成功", status);
    }

}
