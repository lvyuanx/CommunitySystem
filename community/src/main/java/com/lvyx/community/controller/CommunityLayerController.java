package com.lvyx.community.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.community.CommunityUrls;
import com.lvyx.community.entity.CommunityLayer;
import com.lvyx.community.service.CommunityLayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 小区-层 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Slf4j
@Api(tags = "社区-层")
@ApiSupport(order = 4)
@RestController
@RequestMapping(CommunityUrls.LayerCtrls.BASE_URL)
public class CommunityLayerController {

    @Resource
    private CommunityLayerService communityLayerService;

    @LLogger(description = "添加层", params = {"社区-层信息"})
    @ApiOperation("添加层")
    @ApiOperationSupport(order = 1)
    @PostMapping(CommunityUrls.LayerCtrls.ADD)
    public Result<CommunityLayer> add(@RequestBody CommunityLayer layer){
        CommunityLayer res = null;
        try {
            res = communityLayerService.add(layer);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("添加层异常");
        }
        return new SuccessResult<>("添加层成功", res);
    }


    @LLogger(description = "查询层")
    @ApiOperation("查询层")
    @ApiOperationSupport(order = 2)
    @GetMapping(CommunityUrls.LayerCtrls.FIND)
    public Result<List<CommunityLayer>> find(String parentId){
        List<CommunityLayer> communityLayerList = null;
        try {
            communityLayerList = communityLayerService.find(parentId);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询层异常");
        }
        return new SuccessResult<>("查询层成功", communityLayerList);
    }

}
