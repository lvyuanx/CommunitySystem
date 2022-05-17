package com.lvyx.community.mapper;

import com.lvyx.community.entity.CommunityYellowCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 黄码用户关联表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2022-03-20
 */
@Mapper
public interface CommunityYellowCodeMapper extends BaseMapper<CommunityYellowCode> {
    int selectCountByUintId(@Param("unitId") String unitId);
}
