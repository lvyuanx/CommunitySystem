package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityLayer;
import com.lvyx.community.mapper.CommunityLayerMapper;
import com.lvyx.community.service.CommunityLayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 小区-层 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Service
public class CommunityLayerServiceImpl extends ServiceImpl<CommunityLayerMapper, CommunityLayer> implements CommunityLayerService {

    /**
     * 添加层信息
     *
     * @param layer 层信息
     * @return CommunityBuilding
     * @author lvyx
     * @since 2022/2/4 15:31
     **/
    @Override
    public CommunityLayer add(CommunityLayer layer) {
        BaseEntityUtil.add(ShiroUtils.getShiroUser().getId(), layer);
        this.baseMapper.insert(layer);
        return layer;
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
    public List<CommunityLayer> find(String parentId) {
        LambdaQueryWrapper<CommunityLayer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityLayer::getCreateUser, ShiroUtils.getShiroUser().getId())
                .eq(CommunityLayer::getUnitId, parentId)
                .eq(CommunityLayer::getIsEnable, BooleanTypeEnum.YES.getCode())
                .orderByAsc(CommunityLayer::getSortNo);
        return this.baseMapper.selectList(wrapper);
    }
}
