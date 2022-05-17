package com.lvyx.mail.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.exception.LMailException;
import com.lvyx.commons.pojo.ShiroUser;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.mail.bo.BaseSenderMessageBo;
import com.lvyx.mail.entity.CommunityMessage;
import com.lvyx.mail.entity.CommunityMessageEmail;
import com.lvyx.mail.mapper.CommunityMessageMapper;
import com.lvyx.mail.service.CommunityMessageEmailService;
import com.lvyx.mail.service.CommunityMessageService;
import com.lvyx.mail.vo.MessageVo;
import com.lvyx.mail.vo.UserMessageVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 社区消息 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-04-30
 */
@Service
public class CommunityMessageServiceImpl extends ServiceImpl<CommunityMessageMapper, CommunityMessage> implements CommunityMessageService {

    @Resource
    private CommunityMessageEmailService communityMessageEmailService;

    @Value("${spring.mail.username}")
    private String sysFromEmail;

    /**
     * 发送普通邮件
     *
     * @param baseSenderMessageBo 邮件发送参数
     * @author lvyx
     * @since 2022/5/1 15:19
     **/
    @Override
    public void sendSimpleMessage(BaseSenderMessageBo baseSenderMessageBo) throws LMailException {
        List<CommunityMessageEmail> communityMessageEmailList = new ArrayList<>();
        CommunityMessage communityMessage = new CommunityMessage();
        String fromEmail;
        String[] toEmails;
        // 消息主键
        String messageId = UUID.randomUUID().toString();
        // 查询接收者的邮件地址
        if (CollectionUtils.isNotEmpty(baseSenderMessageBo.getTo())){
            List<UserMessageVo> to = communityMessageEmailService.findByUserIds(baseSenderMessageBo.getTo());
            if (CollectionUtils.isNotEmpty(to)){
                communityMessageEmailList = to.stream().map(userMessageVo -> {
                    CommunityMessageEmail communityMessageEmail = new CommunityMessageEmail();
                    communityMessageEmail.setToEmail(userMessageVo.getEmail());
                    communityMessageEmail.setToUser(userMessageVo.getUserId());
                    communityMessageEmail.setMessageId(messageId);
                    return communityMessageEmail;
                }).collect(Collectors.toList());
                toEmails = to.stream().map(UserMessageVo::getEmail).toArray(String[]::new);
            }else {
                throw new LMailException("接收者不存在");
            }
        }else {
            throw new LMailException("接收者邮件地址不能为空");
        }
        // 如果发送者不为空，查询发送者的邮件地址
        if (StringUtils.isNotBlank(baseSenderMessageBo.getFrom())) {
            List<UserMessageVo> from = communityMessageEmailService.findByUserIds(Arrays.asList(baseSenderMessageBo.getFrom()));
            fromEmail = from.get(0).getEmail();
            if(CollectionUtils.isNotEmpty(from)){
                communityMessageEmailList = communityMessageEmailList.stream().peek(communityMessageEmail -> {
                    communityMessageEmail.setFromUser(from.get(0).getUserId());
                    communityMessageEmail.setFromEmail(from.get(0).getEmail());
                }).collect(Collectors.toList());
            }
        }else {
            // 否则为当前登录人
            ShiroUser shiroUser = ShiroUtils.getShiroUser();
            fromEmail = shiroUser.getEmail();
            communityMessageEmailList = communityMessageEmailList.stream().peek(communityMessageEmail -> {
                communityMessageEmail.setFromUser(shiroUser.getId());
                communityMessageEmail.setFromEmail(shiroUser.getEmail());
                BaseEntityUtil.add(shiroUser.getId(), communityMessageEmail);
                communityMessageEmail.setIsEnable(BooleanTypeEnum.NO.getCode());
            }).collect(Collectors.toList());
        }
        // 封装消息内容
        communityMessage.setContent(baseSenderMessageBo.getContent());
        communityMessage.setSubject(baseSenderMessageBo.getSubject());
        communityMessage.setId(messageId);
        communityMessage.setIsEmail(baseSenderMessageBo.getIsSenderEmail());
        if (Objects.equals(baseSenderMessageBo.getIsSenderEmail(), BooleanTypeEnum.YES.getCode())){
            // 开启多线程发送邮件
            new Thread(() -> {
                // 发送简单邮件
                com.lyx.mail.Utils.MailSenderUtils.sendSimpleMail(sysFromEmail, toEmails, baseSenderMessageBo.getSubject(), baseSenderMessageBo.getContent());
            }).start();
        }
        // 保存发送记录
        communityMessageEmailService.saveBatch(communityMessageEmailList);
        // 保存消息
        this.save(communityMessage);
    }

    /**
     * 查询消息
     *
     * @param isEmail  是否邮件提醒
     * @param isEnable 是否已读
     * @param title    邮件主题
     * @return com.github.pagehelper.PageInfo<com.lvyx.mail.vo.MessageVo>
     * @author lvyx
     * @since 2022/5/5 23:48
     **/
    @Override
    public PageInfo<MessageVo> findListMessageVo(Integer pageNum, Integer pageSize, Integer isEmail, Integer isEnable, String title) {
        String userId = ShiroUtils.getUserId();
        PageHelper.startPage(pageNum, pageSize);
        List<MessageVo> messageVoList = this.baseMapper.findListMessageVo(userId, isEmail, isEnable, title);
        return new PageInfo<>(messageVoList);
    }
}
