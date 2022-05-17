package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityBuilding;
import com.lvyx.community.mapper.CommunityBuildingMapper;
import com.lvyx.community.service.CommunityBuildingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 小区-栋 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Service
public class CommunityBuildingServiceImpl extends ServiceImpl<CommunityBuildingMapper, CommunityBuilding> implements CommunityBuildingService {

    /**
     * 添加栋信息
     *
     * @param building 栋信息
     * @return CommunityPeriod
     * @author lvyx
     * @since 2022/2/4 15:31
     **/
    @Override
    public CommunityBuilding add(CommunityBuilding building) {
        BaseEntityUtil.add(ShiroUtils.getShiroUser().getId(), building);
        this.baseMapper.insert(building);
        return building;
    }

    /**
     * 查询栋
     * @param parentId 父级Id
     * @return java.util.List<com.lvyx.community.entity.CommunityPeriod>
     * @author lvyx
     * @since 2022/2/4 17:05
     **/
    @Override
    public List<CommunityBuilding> find(String parentId) {
        LambdaQueryWrapper<CommunityBuilding> wrappr = new LambdaQueryWrapper<>();
        wrappr.eq(CommunityBuilding::getCreateUser, ShiroUtils.getShiroUser().getId())
                .eq(CommunityBuilding::getPeriodId, parentId)
                .eq(CommunityBuilding::getIsEnable, BooleanTypeEnum.YES.getCode())
                .orderByAsc(CommunityBuilding::getSortNo);
        return this.baseMapper.selectList(wrappr);
    }

    /**
     * 查询所有栋
     *
     * @return java.util.List<com.lvyx.community.entity.CommunityBuilding>
     * @author lvyx
     * @since 2022/2/5 19:43
     **/
    @Override
    public List<CommunityBuilding> findAll() {
        LambdaQueryWrapper<CommunityBuilding> wrappr = new LambdaQueryWrapper<>();
        wrappr.eq(CommunityBuilding::getIsEnable, BooleanTypeEnum.YES.getCode())
                .orderByAsc(CommunityBuilding::getSortNo);
        return this.baseMapper.selectList(wrappr);
    }

    /**
     * 分页查询期下的所有栋
     *
     * @param periodId 期id
     * @param pageNum  起始页
     * @param pageSize 页面大小
     * @return java.util.List<com.lvyx.community.entity.CommunityBuilding>
     * @author lvyx
     * @since 2022/2/7 15:14
     **/
    @Override
    public PageInfo<CommunityBuilding> findAllByPeriodId(String periodId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<CommunityBuilding> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityBuilding::getPeriodId, periodId)
                .orderByAsc(CommunityBuilding::getSortNo);
        List<CommunityBuilding> communityBuildings = this.baseMapper.selectList(wrapper);
        return new PageInfo<>(communityBuildings);
    }


}
