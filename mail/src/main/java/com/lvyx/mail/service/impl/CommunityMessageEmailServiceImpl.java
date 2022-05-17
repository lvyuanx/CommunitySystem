package com.lvyx.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvyx.mail.entity.CommunityMessageEmail;
import com.lvyx.mail.mapper.CommunityMessageEmailMapper;
import com.lvyx.mail.service.CommunityMessageEmailService;
import com.lvyx.mail.vo.UserMessageVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 社区邮件信息表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-05-01
 */
@Service
public class CommunityMessageEmailServiceImpl extends ServiceImpl<CommunityMessageEmailMapper, CommunityMessageEmail> implements CommunityMessageEmailService {

    /**
     * 查询用户的邮件信息
     *
     * @param userIds 用户ids
     * @return java.util.List<com.lvyx.mail.vo.UserMessageVo>
     * @author lvyx
     * @since 2022/5/1 15:29
     **/
    @Override
    public List<UserMessageVo> findByUserIds(List<String> userIds) {
        return this.baseMapper.findByUserIds(userIds);
    }

    @Override
    public void updateEmailIsEnable(String emailId, Integer isEnable) {
        LambdaUpdateWrapper<CommunityMessageEmail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CommunityMessageEmail::getId, emailId)
                .set(CommunityMessageEmail::getIsEnable, isEnable);
        this.update(wrapper);
    }
}
