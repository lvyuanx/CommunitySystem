package com.lvyx.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lvyx.commons.exception.LMailException;
import com.lvyx.mail.bo.BaseSenderMessageBo;
import com.lvyx.mail.entity.CommunityMessage;
import com.lvyx.mail.vo.MessageVo;

/**
 * <p>
 * 社区消息 服务类
 * </p>
 *
 * @author lvyx
 * @since 2022-04-30
 */
public interface CommunityMessageService extends IService<CommunityMessage> {

    /**
     * 发送普通邮件
     * @param baseSenderMessageBo 邮件发送参数
     * @author lvyx
     * @since 2022/5/1 15:19
     **/
    void sendSimpleMessage(BaseSenderMessageBo baseSenderMessageBo) throws LMailException;

    /**
     * 查询消息
     * @param isEmail 是否邮件提醒
     * @param isEnable 是否已读
     * @param title  邮件主题
     * @return com.github.pagehelper.PageInfo<com.lvyx.mail.vo.MessageVo>
     * @author lvyx
     * @since 2022/5/5 23:48
     **/
    PageInfo<MessageVo> findListMessageVo(Integer pageNum, Integer pageSize, Integer isEmail, Integer isEnable, String title);

}
