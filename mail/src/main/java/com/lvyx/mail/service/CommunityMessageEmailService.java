package com.lvyx.mail.service;

import com.lvyx.mail.entity.CommunityMessageEmail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lvyx.mail.vo.UserMessageVo;

import java.util.List;

/**
 * <p>
 * 社区邮件信息表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2022-05-01
 */
public interface CommunityMessageEmailService extends IService<CommunityMessageEmail> {

    /**
     * 查询用户的邮件信息
     * @param userIds 用户ids
     * @return java.util.List<com.lvyx.mail.vo.UserMessageVo>
     * @author lvyx
     * @since 2022/5/1 15:29
     **/
    List<UserMessageVo> findByUserIds(List<String> userIds);

    void updateEmailIsEnable(String emailId,  Integer isEnable);

}
