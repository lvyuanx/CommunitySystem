package com.lvyx.mail.controller;


import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.exception.LMailException;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.mail.MailUrls;
import com.lvyx.mail.bo.BaseSenderMessageBo;
import com.lvyx.mail.service.CommunityMessageEmailService;
import com.lvyx.mail.service.CommunityMessageService;
import com.lvyx.mail.vo.MessageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 社区消息 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-04-30
 */
@Slf4j
@Api(tags = "社区消息")
@ApiSupport(order = 1)
@RestController
@RequestMapping(MailUrls.MessageCtrls.BASE_URL)
public class CommunityMessageController {

    @Resource
    private CommunityMessageService communityMessageService;

    @Resource
    private CommunityMessageEmailService communityMessageEmailService;

    @LLogger(description = "发送消息", params = {"发送消息"})
    @ApiOperation("发送消息")
    @ApiOperationSupport(order = 1)
    @PostMapping(MailUrls.MessageCtrls.SENDER_SIMPLE_MAIL)
    public Result<String> sendSimpleMessage(@RequestBody BaseSenderMessageBo baseSenderMessageBo) {
        try {
            communityMessageService.sendSimpleMessage(baseSenderMessageBo);
        } catch (LMailException mailException) {
            log.error(mailException.getMessage(), mailException);
            return new ErrorResult<>(mailException.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ErrorResult<>("邮件发送异常");
        }
        return new SuccessResult<>("邮件栋成功");
    }

    @LLogger(description = "消息状态修改", params = {"邮件id", "邮件状态"})
    @ApiOperation("消息状态修改")
    @ApiOperationSupport(order = 2)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "emailId", value = "邮件id"),
            @ApiImplicitParam(name = "isEnable", value = "邮件状态")
    })
    @GetMapping(MailUrls.MessageCtrls.READ_MESSAGE)
    public Result<String> readMessage(@RequestParam String emailId,
                                      @RequestParam Integer isEnable) {
        try {
            communityMessageEmailService.updateEmailIsEnable(emailId, isEnable);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ErrorResult<>("消息状态修改异常");
        }
        return new SuccessResult<>("消息状态修改成功");
    }

    @LLogger(description = "查询消息列表", params = {"起始页", "页面大小", "是否发送邮件", "是否已读", "主题"})
    @ApiOperation("查询消息列表")
    @ApiOperationSupport(order = 3)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "起始页", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", defaultValue = "10"),
            @ApiImplicitParam(name = "isEmail", value = "是否发送邮件"),
            @ApiImplicitParam(name = "isEnable", value = "是否已读"),
            @ApiImplicitParam(name = "subject", value = "主题"),
    })
    @GetMapping(MailUrls.MessageCtrls.FIND_MESSAGE)
    public Result<PageInfo<MessageVo>> findMessage(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                   @RequestParam(value = "isEmail", required = false) Integer isEmail,
                                                   @RequestParam(value = "isEnable", required = false) Integer isEnable,
                                                   @RequestParam(value = "subject", required = false) String subject) {
        PageInfo<MessageVo> messageVoPageInfo = new PageInfo<>();
        try {
            messageVoPageInfo = communityMessageService.findListMessageVo(pageNum, pageSize, isEmail, isEnable, subject);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询消息列表异常");
        }
        return new SuccessResult<>("查询消息列表成功", messageVoPageInfo);
    }


}
