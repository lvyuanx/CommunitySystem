package com.lvyx.community.controller;


import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.community.CommunityUrls;
import com.lvyx.community.entity.CommunityBuilding;
import com.lvyx.community.service.CommunityBuildingService;
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
 * 小区-栋 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Slf4j
@Api(tags = "社区-栋")
@ApiSupport(order = 2)
@RestController
@RequestMapping(CommunityUrls.BuildingCtrls.BASE_URL)
public class CommunityBuildingController {

    @Resource
    private CommunityBuildingService communityBuildingService;

    @LLogger(description = "添加栋", params = {"社区-栋信息"})
    @ApiOperation("添加栋")
    @ApiOperationSupport(order = 1)
    @PostMapping(CommunityUrls.BuildingCtrls.ADD)
    public Result<CommunityBuilding> add(@RequestBody CommunityBuilding building){
        CommunityBuilding res = null;
        try {
            res = communityBuildingService.add(building);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("添加栋异常");
        }
        return new SuccessResult<>("添加栋成功", res);
    }


    @LLogger(description = "查询栋")
    @ApiOperation("查询栋")
    @ApiOperationSupport(order = 2)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "父级id", required = true, dataType = "int", paramType = "query"),
    })
    @GetMapping(CommunityUrls.BuildingCtrls.FIND)
    public Result<List<CommunityBuilding>> find(String parentId){
        List<CommunityBuilding> communityBuildings = null;
        try {
            communityBuildings = communityBuildingService.find(parentId);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询栋异常");
        }
        return new SuccessResult<>("查询栋成功", communityBuildings);
    }

    @LLogger(description = "查询所有栋")
    @ApiOperation("查询所有栋")
    @ApiOperationSupport(order = 3)
    @GetMapping(CommunityUrls.BuildingCtrls.FIND_ALL)
    public Result<List<CommunityBuilding>> findAll(){
        List<CommunityBuilding> communityBuildings = null;
        try {
            communityBuildings = communityBuildingService.findAll();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询所有栋异常");
        }
        return new SuccessResult<>("查询所有栋成功", communityBuildings);
    }

    @LLogger(description = "查询期下所有栋")
    @ApiOperation("查询期下所有栋")
    @ApiOperationSupport(order = 3)
    @ApiImplicitParams({
      @ApiImplicitParam(name = "pageNum", value = "起始页", required = true, dataType = "int", paramType = "query"),
      @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int", paramType = "query"),
      @ApiImplicitParam(name = "periodId", value = "单元id", required = true, dataType = "int", paramType = "query"),
    })
    @GetMapping(CommunityUrls.BuildingCtrls.FIND_BY_PERIOND_ID)
    public Result<PageInfo<CommunityBuilding>> findByPeriodId(Integer pageNum, Integer pageSize,String periodId){
        PageInfo<CommunityBuilding> allByPeriodId = null;
        try {
            allByPeriodId = communityBuildingService.findAllByPeriodId(periodId, pageNum, pageSize);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询期下所有栋异常");
        }
        return new SuccessResult<>("查询期下所有栋成功", allByPeriodId);
    }

}
