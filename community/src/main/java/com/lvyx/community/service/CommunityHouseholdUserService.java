package com.lvyx.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lvyx.community.bo.QueryUserHouseholdBo;
import com.lvyx.community.entity.CommunityHouseholdUser;
import com.lvyx.community.vo.UserHouseholdVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户与户关联表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
public interface CommunityHouseholdUserService extends IService<CommunityHouseholdUser> {

    /**
     * 用户绑定房间
     * @param userId 用户id
     * @param householdId 户id
     * @author lvyx
     * @since 2022/2/8 19:34
     **/
    void bind(String userId, String householdId);

    /**
     * 用户绑定房间
     * @param userId 用户id
     * @param householdId 户id
     * @param isEnable 是否启用
     * @author lvyx
     * @since 2022/2/8 19:34
     **/
    void bind(String userId, String householdId, Integer isEnable);

    /**
     * 查询用户绑定房间信息
     * @param queryUserHouseholdBo   请求参数
     * @param pageNum           起始页
     * @param pageSize          页面大小
     * @return com.github.pagehelper.PageInfo<com.lvyx.community.vo.UserHouseHoldVo>
     * @author lvyx
     * @since 2022/4/25 21:48
     **/
    PageInfo<UserHouseholdVo> findUserHousehold(QueryUserHouseholdBo queryUserHouseholdBo, Integer pageNum, Integer pageSize);


    /**
     * 修改用户住址信息
     * @param userId        用户id
     * @param householdId   新户id
     * @param isEnable      是否启用
     * @author lvyx
     * @since 2022/4/27 22:08
     **/
    @Transactional(rollbackFor = Exception.class)
    void updateUserHousehold(String userId, String householdId, Integer isEnable);

    /**
     * 根据用户id删除关联关系
     * @param userId   用户id
     * @author lvyx
     * @since 2022/4/30 15:00
     **/
    void deleteByUser(String userId);

    String findPeriodIdByUserId(String userId);

    List<String> findUserPeriodId(String userId);


}
