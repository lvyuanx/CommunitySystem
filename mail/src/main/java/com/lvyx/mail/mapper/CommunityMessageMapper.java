package com.lvyx.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvyx.mail.entity.CommunityMessage;
import com.lvyx.mail.vo.MessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 社区消息 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2022-04-30
 */
@Mapper
public interface CommunityMessageMapper extends BaseMapper<CommunityMessage> {

    List<MessageVo> findListMessageVo(@Param("userId") String userId, @Param("isEmail") Integer isEmail, @Param("isEnable") Integer isEnable, @Param("title") String title);

}
