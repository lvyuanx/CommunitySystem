package com.lvyx.mail.mapper;

import com.lvyx.mail.entity.CommunityMessageEmail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvyx.mail.vo.UserMessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 社区邮件信息表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2022-05-01
 */
@Mapper
public interface CommunityMessageEmailMapper extends BaseMapper<CommunityMessageEmail> {

    /**
     * 查询用户的邮件信息
     * @param userIds 用户ids
     * @return java.util.List<com.lvyx.mail.vo.UserMessageVo>
     * @author lvyx
     * @since 2022/5/1 15:29
     **/
    List<UserMessageVo> findByUserIds(@Param("userIds") List<String> userIds);

}
