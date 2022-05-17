package com.lvyx.community.service;

import com.lvyx.community.entity.CommunityRedCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 红码用户关联表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
public interface CommunityRedCodeService extends IService<CommunityRedCode> {

    /**
     * 判断用户是否在红码表中
     * @param userId  用户id
     * @return java.lang.Boolean
     * @author lvyx
     * @since 2022/4/14 8:40
     **/
    Boolean isExist(String userId);


    /**
     * 根据用户id查询红码
     * @param userId 用户id
     * @return com.lvyx.community.entity.CommunityRedCode
     * @author lvyx
     * @since 2022/4/14 8:40
     **/
    CommunityRedCode findByUserId(String userId);

    /**
     * 失效红码
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
