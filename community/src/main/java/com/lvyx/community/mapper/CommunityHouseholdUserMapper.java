package com.lvyx.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvyx.community.bo.QueryUserHouseholdBo;
import com.lvyx.community.entity.CommunityHouseholdUser;
import com.lvyx.community.vo.UserHouseholdVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户与户关联表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Mapper
public interface CommunityHouseholdUserMapper extends BaseMapper<CommunityHouseholdUser> {

    /**
     * 查询住户信息
     * @param queryUserHouseholdBo 查询条件
     * @return java.util.List<com.lvyx.community.vo.UserHouseholdVo>
     * @author lvyx
     * @since 2022/4/25 21:54
     **/
    List<UserHouseholdVo> findUserHoseholdByQuery(QueryUserHouseholdBo queryUserHouseholdBo);

    String findPeriodIdByUserId(@Param("userId") String userId);

    void  updateUserPeriod(@Param("userId") String userId, @Param("periodId") String periodId);

    List<String> findUserPeriodId(String userId);

}
