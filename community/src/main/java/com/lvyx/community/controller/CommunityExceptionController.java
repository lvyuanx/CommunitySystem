package com.lvyx.community.controller;


import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.exception.LExceException;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.CommunityUrls;
import com.lvyx.community.bo.QueryExceptionBo;
import com.lvyx.community.entity.CommunityException;
import com.lvyx.community.service.CommunityExceptionService;
import com.lvyx.community.vo.ExceptionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 社区异常表 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-05-02
 */
@Slf4j
@Api(tags = "异常管理")
@ApiSupport(order = 10)
@RestController
@RequestMapping(CommunityUrls.ExceptionCtrls.BASE_URL)
public class CommunityExceptionController {

    @Resource
    private CommunityExceptionService communityExceptionService;

    @LLogger(description = "添加异常信息", params = {"添加异常信息"})
    @ApiOperation("添加异常信息")
    @ApiOperationSupport(order = 1)
    @PostMapping(CommunityUrls.ExceptionCtrls.ADD)
    public Result<String> add(@RequestBody CommunityException communityException) {
        try {
            BaseEntityUtil.add(ShiroUtils.getUserId(), communityException);
            communityExceptionService.save(communityException);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ErrorResult<>("添加异常信息异常");
        }
        return new SuccessResult<>("添加异常信息成功");
    }

    @LLogger(description = "查询异常信息", params = {"查询异常信息BO", "每页数量", "页码"})
    @ApiOperation("查询异常信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sizeSize", value = "每页数量", defaultValue = "10", dataType = "int", paramType = "query"),
    })
    @ApiOperationSupport(order = 2)
    @PostMapping(CommunityUrls.ExceptionCtrls.FIND_LIST_BY_QUERY)
    public Result<PageInfo<ExceptionVo>> findListByQuery(@RequestBody QueryExceptionBo queryExceptionBo,
                                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        PageInfo<ExceptionVo> exceptionVoPageInfo = new PageInfo<>();
        try {
            exceptionVoPageInfo = communityExceptionService.findByQuery(pageNum, pageSize, queryExceptionBo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询异常信息异常");
        }
        return new SuccessResult<>("查询异常信息成功", exceptionVoPageInfo);
    }

    @LLogger(description = "修改异常状态", params = {"状态(0:未开始,1:进行中,2:已完成)", "异常id", "处理结果"})
    @ApiOperation("修改异常状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态(0:未开始,1:进行中,2:已完成)", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "isTransferredCode", value = "是否转码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "异常id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "result", value = "处理结果", dataType = "String", paramType = "query"),
    })
    @ApiOperationSupport(order = 2)
    @GetMapping(CommunityUrls.ExceptionCtrls.UPDATE_STATUS)
    public Result<String> updateStatus(@RequestParam Integer status,
                                       @RequestParam String id,
                                       @RequestParam(value = "isTransferredCode", required = false) String isTransferredCode,
                                       @RequestParam(value = "result", required = false) String result) {
        try {
            communityExceptionService.updateStatus(id, status, result, isTransferredCode);
        } catch (LExceException ee) {
            log.error(ee.getMessage(), ee);
            return new ErrorResult<>(ee.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ErrorResult<>("修改异常状态异常");
        }
        return new SuccessResult<>("修改异常状态成功");
    }


}
