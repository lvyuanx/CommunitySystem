package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityHousehold;
import com.lvyx.community.mapper.CommunityHouseholdMapper;
import com.lvyx.community.service.CommunityHouseholdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 小区-户 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Service
public class CommunityHouseholdServiceImpl extends ServiceImpl<CommunityHouseholdMapper, CommunityHousehold> implements CommunityHouseholdService {

    /**
     * 添加层信息
     *
     * @param household 层信息
     * @return CommunityBuilding
     * @author lvyx
     * @since 2022/2/4 15:31
     **/
    @Override
    public CommunityHousehold add(CommunityHousehold household) {
        BaseEntityUtil.add(ShiroUtils.getShiroUser().getId(), household);
        this.baseMapper.insert(household);
        return household;
    }

    /**
     * 查询层
     *
     * @param parentId 父级id
     * @return java.util.List<com.lvyx.community.entity.CommunityPeriod>
     * @author lvyx
     * @since 2022/2/4 17:05
     **/
    @Override
    public List<CommunityHousehold> find(String parentId) {
        LambdaQueryWrapper<CommunityHousehold> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityHousehold::getCreateUser, ShiroUtils.getShiroUser().getId())
                .eq(CommunityHousehold::getLayerId, parentId)
                .eq(CommunityHousehold::getIsEnable, BooleanTypeEnum.YES.getCode())
                .orderByAsc(CommunityHousehold::getSortNo);
        return this.baseMapper.selectList(wrapper);
    }


    public String findPeriodIByHouseholdId(String householdId) {
        return this.baseMapper.findPeriodIByHouseholdId(householdId);
    }
}
