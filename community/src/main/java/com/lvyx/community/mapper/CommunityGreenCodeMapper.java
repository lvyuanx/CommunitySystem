package com.lvyx.community.mapper;

import com.lvyx.community.entity.CommunityGreenCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 绿码用户关联表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
@Mapper
public interface CommunityGreenCodeMapper extends BaseMapper<CommunityGreenCode> {

    int selectCountByUintId(@Param("unitId") String unitId);

}
