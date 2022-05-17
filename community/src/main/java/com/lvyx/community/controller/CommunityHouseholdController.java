package com.lvyx.community.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.community.CommunityUrls;
import com.lvyx.community.entity.CommunityHousehold;
import com.lvyx.community.service.CommunityHouseholdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 小区-户 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Slf4j
@Api(tags = "社区-户")
@ApiSupport(order = 5)
@RestController
@RequestMapping(CommunityUrls.HouseholdCtrls.BASE_URL)
public class CommunityHouseholdController {

    @Resource
    private CommunityHouseholdService communityHouseholdService;

    @LLogger(description = "添加层", params = {"社区-层信息"})
    @ApiOperation("添加层")
    @ApiOperationSupport(order = 1)
    @PostMapping(CommunityUrls.HouseholdCtrls.ADD)
    public Result<CommunityHousehold> add(@RequestBody CommunityHousehold household){
        CommunityHousehold res = null;
        try {
            res = communityHouseholdService.add(household);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("添加层异常");
        }
        return new SuccessResult<>("添加层成功", res);
    }


    @LLogger(description = "查询层")
    @ApiOperation("查询层")
    @ApiOperationSupport(order = 2)
    @GetMapping(CommunityUrls.HouseholdCtrls.FIND)
    public Result<List<CommunityHousehold>> find(String parentId){
        List<CommunityHousehold> communityHouseholdList = null;
        try {
            communityHouseholdList = communityHouseholdService.find(parentId);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询层异常");
        }
        return new SuccessResult<>("查询层成功", communityHouseholdList);
    }

}
