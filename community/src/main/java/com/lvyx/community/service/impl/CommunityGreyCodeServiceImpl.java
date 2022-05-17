package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityGreyCode;
import com.lvyx.community.mapper.CommunityGreyCodeMapper;
import com.lvyx.community.service.CommunityGreyCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 * 灰码用户关联表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
@Service
public class CommunityGreyCodeServiceImpl extends ServiceImpl<CommunityGreyCodeMapper, CommunityGreyCode> implements CommunityGreyCodeService {

    /**
     * 根据用户id查询黄码
     *
     * @param userId 用户id
     * @return com.lvyx.community.entity.CommunityRedCode
     * @author lvyx
     * @since 2022/4/14 8:40
     **/
    @Override
    public CommunityGreyCode findByUserId(String userId) {
        LambdaQueryWrapper<CommunityGreyCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityGreyCode::getUserId, userId)
                .eq(CommunityGreyCode::getIsEnable, BooleanTypeEnum.YES.getCode())
                .orderByDesc(CommunityGreyCode::getCreateTime);
        List<CommunityGreyCode> communityGreyCodeList = this.list(wrapper);
        return CollectionUtils.isNotEmpty(communityGreyCodeList) ? communityGreyCodeList.get(0) : null;
    }

    /**
     * 判断用户是否已经绑定灰码
     *
     * @param userId 用户id
     * @return java.lang.Boolean
     * @author lvyx
     * @since 2022/4/28 13:44
     **/
    @Override
    public Boolean isExist(String userId) {
        LambdaQueryWrapper<CommunityGreyCode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityGreyCode::getUserId, userId)
                .eq(CommunityGreyCode::getIsEnable, BooleanTypeEnum.YES.getCode());
        return this.count(wrapper) > 0;
    }

    /**
     * 失效灰码
     *
     * @param userId 用户id
     * @author lvyx
     * @since 2022/4/28 13:56
     **/
    @Override
    public void invalidate(String userId) {
        CommunityGreyCode communityGreyCode = this.findByUserId(userId);
        if (ObjectUtils.isEmpty(communityGreyCode)) {
            return;
        }
        communityGreyCode.setIsEnable(BooleanTypeEnum.NO.getCode());
        BaseEntityUtil.edit(ShiroUtils.getUserId(), communityGreyCode);
        this.updateById(communityGreyCode);
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
