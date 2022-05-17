package com.lvyx.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvyx.community.entity.CommunityLayer;

import java.util.List;

/**
 * <p>
 * 小区-层 服务类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
public interface CommunityLayerService extends IService<CommunityLayer> {

    /**
     * 添加层信息
     * @param layer 层信息
     * @return CommunityBuilding
     * @author lvyx
     * @since 2022/2/4 15:31
     **/
    CommunityLayer add(CommunityLayer layer);

    /**
     * 查询层
     * @param parentId 父级id
     * @return java.util.List<com.lvyx.community.entity.CommunityPeriod>
     * @author lvyx
     * @since 2022/2/4 17:05
     **/
    List<CommunityLayer> find(String parentId);

}
