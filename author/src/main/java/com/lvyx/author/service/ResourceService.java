package com.lvyx.author.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvyx.author.entity.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 根据用户id查询用户拥有的资源ids
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/24 9:37
     **/
    List<String> getResourceIdsByUserId(String userId);

    /**
     * 根据用户id得到资源信息
     * @param userId 用户id
     * @return java.util.List<com.lvyx.author.entity.Resource>
     * @author lvyx
     * @since 2021/12/31 14:35
     **/
    List<Resource> getResourceByUserId(String userId);

    /**
     * 添加社区资源
     * @param periodId 期ids
     * @author lvyx
     * @since 2022/5/5 0:09
     **/
    @Transactional(rollbackFor = Exception.class)
    void addCommunityResource(String roleId, List<String> periodId);





}
