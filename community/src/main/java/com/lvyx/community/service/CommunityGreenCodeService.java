package com.lvyx.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvyx.commons.pojo.QrCodeInfo;
import com.lvyx.community.entity.CommunityGreenCode;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;

/**
 * <p>
 * 绿码用户关联表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
public interface CommunityGreenCodeService extends IService<CommunityGreenCode> {

    /**
     * 添加绿码用户关联，如果用户已经存在，则不添加
     * @param communityGreenCode 用户绿码关联信息
     * @author lvyx
     * @since 2022/4/13 15:33
     **/
    void addGreenCode(CommunityGreenCode communityGreenCode);

    /**
     * 根据用户id查询绿码
     * @param userId 用户id
     * @return com.lvyx.community.entity.CommunityGreenCode
     * @author lvyx
     * @since 2022/4/14 8:40
     **/
    CommunityGreenCode findByUserId(String userId);

    /**
     * 根据用户id得到二维码
     * @param userId  用户id
     * @return java.awt.image.BufferedImage
     * @author lvyx
     * @since 2022/4/14 9:12
     **/
    BufferedImage getQrCodeByUserId(String userId);

    /**
     * 得到二维码信息
     * @param userId  用户id
     * @return com.lvyx.commons.pojo.QrCodeInfo
     * @author lvyx
     * @since 2022/4/24 22:30
     **/
    QrCodeInfo getQrCodeInfoByUserId(String userId);

    /**
     * 判断用户是否已经绑定绿码
     * @param userId 用户id
     * @return java.lang.Boolean
     * @author lvyx
     * @since 2022/4/28 13:44
     **/
    Boolean isExist(String userId);

    /**
     * 失效绿码
     * @param userId 用户id
     * @author lvyx
     * @since 2022/4/28 13:56
     **/
    void invalidate(String userId);

    /**
     * 如果用户之前为绿码或者灰码，则更新为黄码
     * @param userId userId
     * @author lvyx
     * @since 2022/4/28 11:38
     **/
    @Transactional(rollbackFor = Exception.class)
    void addRecord(String userId);

    /**
     * 失效所有健康码
     * @param userId 用户id
     * @author lvyx
     * @since 2022/5/3 15:46
     **/
    @Transactional(rollbackFor = Exception.class)
    void invalidateAll(String userId);

    /**
     * 转码
     * @param userId   用户id
     * @param codeType 二维码类型
     * @author lvyx
     * @since 2022/5/4 1:37
     **/
    @Transactional(rollbackFor = Exception.class)
    void changeCode(String userId, String codeType);

    /**
     * 根据单元id查询数量
     * @param unitId 单元id
     * @return int
     * @author lvyx
     * @since 2022/5/5 23:04
     **/
    int selectCountByUintId(String unitId);

}