package com.lvyx.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvyx.community.entity.CommunityPeriod;
import com.lvyx.community.vo.AddressVo;
import com.lvyx.community.vo.CascadeVo;
import com.lvyx.community.vo.ComprehensiveVo;
import com.lvyx.community.vo.PeriodVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 小区-期 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Mapper
public interface CommunityPeriodMapper extends BaseMapper<CommunityPeriod> {

    /**
     * 根据期id得到小区信息
     * @param id  期Id
     * @return java.util.List<com.lvyx.community.vo.PeriodVo>
     * @author lvyx
     * @since 2022/2/6 18:34
     **/
    List<PeriodVo> findCommunityByPeriodId(@Param("id") String id);

    /**
     * 根据期id得到小区信息
     * @return java.util.List<com.lvyx.community.vo.PeriodVo>
     * @author lvyx
     * @since 2022/2/6 18:34
     **/
    List<CascadeVo> findCommunityAllInfo();

    /**
     *  根据期id得到小区户信息
     * @param id 期id
     * @return java.util.List<com.lvyx.community.vo.PeriodVo>
     * @author lvyx
     * @since 2022/2/6 19:12
     **/
    List<ComprehensiveVo> findComprehensiveByPeriodId(@Param("id") String id);


    /**
     * 获取当前登录人的住址信息
     * @param userId 用户id
     * @return AddressVo
     * @author lvyx
     * @since 2022/2/9 21:24
     **/
    AddressVo findInfoByUser(@Param("userId") String userId);

    /**
     * 查询登录用户是否含有住址信息
     * @return 房间状态
     * @author lvyx
     * @since 2022/5/22 16:12
     **/
    Integer checkUserHasAddress(@Param("userId") String userId);


}
