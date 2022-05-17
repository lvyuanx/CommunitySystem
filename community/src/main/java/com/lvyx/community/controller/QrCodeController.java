package com.lvyx.community.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.pojo.QrCodeInfo;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.CommunityUrls;
import com.lvyx.community.service.CommunityGreenCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * <p>
 * 二维码控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-04-05 21:09:47
 */
@Slf4j
@Api(tags = "二维码")
@ApiSupport(order = 9)
@RestController
@RequestMapping(CommunityUrls.QrCoceCtrls.BASE_URL)
public class QrCodeController {

    @Resource
    private CommunityGreenCodeService communityGreenCodeService;

    @LLogger(description = "查看用户二维码", params = {"用户id"})
    @ApiOperation("查看用户二维码")
    @ApiOperationSupport(order = 1)
    @GetMapping(CommunityUrls.QrCoceCtrls.GET_BY_USER)
    public void getByUser(HttpServletResponse response, @RequestParam(required = false) String userId) {
        try {
            BufferedImage qrCodeByUserId = communityGreenCodeService.getQrCodeByUserId(StringUtils.isBlank(userId) ? ShiroUtils.getUserId() : userId);
            if (ObjectUtils.isNotNull(qrCodeByUserId)) {
                // 图片下载
                response.setContentType("image/png");
                ServletOutputStream out = response.getOutputStream();
                // 输出图片到流中
                ImageIO.write(qrCodeByUserId, "png", out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @LLogger(description = "查看用户二维码详情", params = {"用户id"})
    @ApiOperation("查看用户二维码详情")
    @ApiOperationSupport(order = 2)
    @GetMapping(CommunityUrls.QrCoceCtrls.GET_INFO_BY_USER)
    public Result<QrCodeInfo> getInfoByUser(HttpServletResponse response, @RequestParam(required = false) String userId) {
        QrCodeInfo qrCodeInfoByUserId = null;
        try {
            qrCodeInfoByUserId = communityGreenCodeService.getQrCodeInfoByUserId(StringUtils.isBlank(userId) ? ShiroUtils.getUserId() : userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查看用户二维码详情异常");
        }
        return new SuccessResult<>("查看用户二维码详情成功", qrCodeInfoByUserId);
    }

    @LLogger(description = "转码", params = {"用户id", "二维码类型"})
    @ApiOperation("转码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "codeType", value = "二维码类型", required = true, dataType = "String")
    })
    @ApiOperationSupport(order = 3)
    @GetMapping(CommunityUrls.QrCoceCtrls.CHANGE_CODE)
    public Result<String> changeCode( @RequestParam String userId, @RequestParam String codeType) {
        try {
            communityGreenCodeService.changeCode(userId, codeType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ErrorResult<>("转码异常");
        }
        return new SuccessResult<>("转码成功");
    }
}
