package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityRedCode;
import com.lvyx.community.mapper.CommunityRedCodeMapper;
import com.lvyx.community.service.CommunityRedCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 * 红码用户关联表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
@Service
public class CommunityRedCodeServiceImpl extends ServiceImpl<CommunityRedCodeMapper, CommunityRedCode> implements CommunityRedCodeService {

    /**
     * 判断用户是否在红码表中
     *
     * @param userId 用户id
     * @return java.lang.Boolean
     * @author lvyx
     * @since 2022/4/14 8:40
     **/
    @Override
    public Boolean isExist(String userId) {
        LambdaQueryWrapper<CommunityRedCode> redWrapper = new LambdaQueryWrapper<>();
        redWrapper.eq(CommunityRedCode::getUserId, userId)
                .eq(CommunityRedCode::getIsEnable, BooleanTypeEnum.YES.getCode());
        return this.count(redWrapper) > 0;
    }

    /**
     * 根据用户id查询红码
     *
     * @param userId 用户id
     * @return com.lvyx.community.entity.CommunityRedCode
     * @author lvyx
     * @since 2022/4/14 8:40
     **/
    @Override
    public CommunityRedCode findByUserId(String userId) {
        LambdaQueryWrapper<CommunityRedCode> redWrapper = new LambdaQueryWrapper<>();
        redWrapper.eq(CommunityRedCode::getUserId, userId)
                .eq(CommunityRedCode::getIsEnable, BooleanTypeEnum.YES.getCode())
                .orderByDesc(CommunityRedCode::getCreateTime);
        List<CommunityRedCode> communityRedCodeList = this.list(redWrapper);
        return CollectionUtils.isNotEmpty(communityRedCodeList) ? communityRedCodeList.get(0) : null;
    }

    /**
     * 失效红码
     *
     * @param userId 用户id
     * @author lvyx
     * @since 2022/4/28 13:56
     **/
    @Override
    public void invalidate(String userId) {
        CommunityRedCode communityRedCode = this.findByUserId(userId);
        if (ObjectUtils.isEmpty(communityRedCode)) {
            return;
        }
        communityRedCode.setIsEnable(BooleanTypeEnum.NO.getCode());
        BaseEntityUtil.edit(ShiroUtils.getUserId(), communityRedCode);
        this.updateById(communityRedCode);
    }

    /**
     * 根据单元id查询数量
     *
     * @param unitId 单元id
     * @return int
     * @author lvyx
     * @since 2022/5/5 23:04
     **/
    @Override
    public int selectCountByUintId(String unitId) {
        return this.baseMapper.selectCountByUintId(unitId);
    }
}
