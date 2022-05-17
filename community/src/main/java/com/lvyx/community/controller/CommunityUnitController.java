package com.lvyx.community.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.community.CommunityUrls;
import com.lvyx.community.entity.CommunityUnit;
import com.lvyx.community.service.CommunityUnitService;
import com.lvyx.community.vo.InfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 小区-单元 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Slf4j
@Api(tags = "社区-期")
@ApiSupport(order = 3)
@RestController
@RequestMapping(CommunityUrls.UnitCtrls.BASE_URL)
public class CommunityUnitController {
    @Resource
    private CommunityUnitService communityUnitService;

    @LLogger(description = "添加单元", params = {"社区-单元信息"})
    @ApiOperation("添加单元")
    @ApiOperationSupport(order = 1)
    @PostMapping(CommunityUrls.UnitCtrls.ADD)
    public Result<CommunityUnit> add(@RequestBody CommunityUnit base){
        CommunityUnit res = null;
        try {
            res = communityUnitService.add(base);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("添加单元异常");
        }
        return new SuccessResult<>("添加单元成功", res);
    }


    @LLogger(description = "查询单元", params = {"期id"})
    @ApiOperation("查询单元")
    @ApiOperationSupport(order = 2)
    @GetMapping(CommunityUrls.UnitCtrls.FIND)
    public Result<List<CommunityUnit>> find(String parentId){
        List<CommunityUnit> baseList = null;
        try {
            baseList = communityUnitService.find(parentId);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询单元异常");
        }
        return new SuccessResult<>("查询单元成功", baseList);
    }


    @LLogger(description = "查询单元", params = {"期id"})
    @ApiOperation("查询单元详情")
    @ApiOperationSupport(order = 3)
    @GetMapping(CommunityUrls.UnitCtrls.FIND_UNIT_INFO_BY_BUINDING)
    public Result<List<InfoVo>> findUnitInfoByBuinding(String buindingId){
        List<InfoVo> info = null;
        try {
            info = communityUnitService.findInfo(buindingId);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询单元详情异常");
        }
        return new SuccessResult<>("查询单元详情成功", info);
    }


}
