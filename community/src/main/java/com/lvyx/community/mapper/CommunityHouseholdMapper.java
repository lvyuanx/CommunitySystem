package com.lvyx.community.mapper;

import com.lvyx.community.entity.CommunityHousehold;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 小区-户 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Mapper
public interface CommunityHouseholdMapper extends BaseMapper<CommunityHousehold> {

    String findPeriodIByHouseholdId(@Param("householdId") String householdId);
}
