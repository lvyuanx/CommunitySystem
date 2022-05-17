package com.lvyx.author.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvyx.author.entity.Resource;
import com.lvyx.author.entity.RoleResource;
import com.lvyx.author.mapper.ResourceMapper;
import com.lvyx.author.service.ResourceService;
import com.lvyx.author.service.RoleResourceService;
import com.lvyx.commons.enums.ResourceTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @javax.annotation.Resource
    private RoleResourceService roleResourceService;

    /**
     * 根据用户id查询用户拥有的资源ids
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/24 9:37
     **/
    @Override
    public List<String> getResourceIdsByUserId(String userId) {
        return baseMapper.getResourcesByUserId(userId).stream().map(Resource::getId).collect(Collectors.toList());
    }

    /**
     * 根据用户id得到资源信息
     *
     * @param userId 用户id
     * @return java.util.List<com.lvyx.author.entity.Resource>
     * @author lvyx
     * @since 2021/12/31 14:35
     **/
    @Override
    public List<Resource> getResourceByUserId(String userId) {
        List<Resource> resourcesByUserId = baseMapper.getResourcesByUserId(userId);
        return resourcesByUserId;
    }

    /**
     * 添加社区资源
     *
     * @param roleId 角色id
     * @param periodId 期ids
     * @author lvyx
     * @since 2022/5/5 0:09
     **/
    @Override
    public void addCommunityResource(String roleId, List<String> periodId) {
        List<Resource> resourceList = periodId.stream().map(item -> {
            Resource resource = new Resource();
            resource.setParentId(item);
            resource.setResourceType(ResourceTypeEnum.COMMUNITY.getValue());
            resource.setDescription(ResourceTypeEnum.COMMUNITY.getName());
            BaseEntityUtil.add(ShiroUtils.getUserId(), resource);
            return resource;
        }).collect(Collectors.toList());
        this.saveBatch(resourceList);
        List<RoleResource> roleResourceList = resourceList.stream().map(item -> {
            RoleResource roleResource = new RoleResource();
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(item.getId());
            BaseEntityUtil.add(ShiroUtils.getUserId(), roleResource);
            return roleResource;
        }).collect(Collectors.toList());
        roleResourceService.saveBatch(roleResourceList);
    }


}
