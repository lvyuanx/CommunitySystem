package com.lvyx.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvyx.community.entity.CommunityGreyCode;

/**
 * <p>
 * 灰码用户关联表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
public interface CommunityGreyCodeService extends IService<CommunityGreyCode> {

    /**
     * 根据用户id查询黄码
     * @param userId 用户id
     * @return com.lvyx.community.entity.CommunityRedCode
     * @author lvyx
     * @since 2022/4/14 8:40
     **/
    CommunityGreyCode findByUserId(String userId);

    /**
     * 判断用户是否已经绑定灰码
     * @param userId 用户id
     * @return java.lang.Boolean
     * @author lvyx
     * @since 2022/4/28 13:44
     **/
    Boolean isExist(String userId);

    /**
     * 失效灰码
     * @param userId 用户id
     * @author lvyx
     * @since 2022/4/28 13:56
     **/
    void invalidate(String userId);

    /**
     * 根据单元id查询数量
     * @param unitId 单元id
     * @return int
     * @author lvyx
     * @since 2022/5/5 23:04
     **/
    int selectCountByUintId(String unitId);

}
