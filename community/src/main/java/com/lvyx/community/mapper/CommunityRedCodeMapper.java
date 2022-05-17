package com.lvyx.community.mapper;

import com.lvyx.community.entity.CommunityRedCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 红码用户关联表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
@Mapper
public interface CommunityRedCodeMapper extends BaseMapper<CommunityRedCode> {
    int selectCountByUintId(@Param("unitId") String unitId);
}
