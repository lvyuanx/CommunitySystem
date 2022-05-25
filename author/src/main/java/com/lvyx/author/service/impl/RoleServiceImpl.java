package com.lvyx.author.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvyx.author.bo.AddRoleBo;
import com.lvyx.author.entity.*;
import com.lvyx.author.mapper.RoleMapper;
import com.lvyx.author.service.*;
import com.lvyx.author.vo.CommunityResourceVo;
import com.lvyx.author.vo.CommunityRoleVo;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.enums.ResourceTypeEnum;
import com.lvyx.commons.enums.RoleTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityGreyCode;
import com.lvyx.community.service.CommunityGreenCodeService;
import com.lvyx.community.service.CommunityGreyCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @javax.annotation.Resource
    private ResourceService resourceService;

    @javax.annotation.Resource
    private UserRoleService userRoleService;

    @javax.annotation.Resource
    private RoleResourceService roleResourceService;

    @javax.annotation.Resource
    private UserService userService;

    @javax.annotation.Resource
    private CommunityGreenCodeService communityGreenCodeService;

    @javax.annotation.Resource
    private CommunityGreyCodeService communityGreyCodeService;


    /**
     * 根据用户信息得到用户权限信息
     * @param user 用户信息
     * @return java.util.List<com.lvyx.author.entity.Role>
     * @author lvyx
     * @since 2021/12/24 13:49
     **/
    @Override
    public List<Role> findRoleListByUser(User user) {
        return baseMapper.getRoleIdsByUser(user);
    }

    /**
     * 根据用户id得到用户角色Ids
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/24 13:53
     **/
    @Override
    public List<String> findRoleIdsByUserId(String userId) {
        User user = new User();
        user.setId(userId);
        List<Role> roleIdsByUser = baseMapper.getRoleIdsByUser(user);
        return roleIdsByUser.stream().map(Role::getId).collect(Collectors.toList());
    }

    /**
     * 根据用户的id得到角色信息
     *
     * @param userId 用户id
     * @return java.util.List<com.lvyx.author.entity.Role>
     * @author lvyx
     * @since 2021/12/31 14:46
     **/
    @Override
    public List<Role> findRoleByUserId(String userId) {
        User user = new User();
        user.setId(userId);
        return baseMapper.getRoleIdsByUser(user);
    }

    /**
     * 查询当前用户资源
     *
     * @return java.util.List<com.lvyx.author.entity.Resource>
     * @author lvyx
     * @since 2022/5/4 17:18
     **/
    @Override
    public List<Resource> findCurrentUserFunction() {
        return baseMapper.findUserResource(ShiroUtils.getUserId(), ResourceTypeEnum.FUNCTION.getValue());
    }

    /**
     * 根据角色类型查询角色信息
     *
     * @param label 角色标签
     * @return Role
     * @author lvyx
     * @since 2022/5/4 17:49
     **/
    @Override
    public List<Role> findRoleIdByLabel(String... label) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Role::getLabel, Arrays.asList(label));
        return this.list(wrapper);
    }

    /**
     * 添加社区管理角色
     *
     * @param addRoleBo 社区管理角色信息
     * @author lvyx
     * @since 2022/5/4 23:51
     **/
    @Override
    public void editCommunityRole(AddRoleBo addRoleBo) {
        if (StringUtils.isNotBlank(addRoleBo.getRoleId())){
            updateCommunityRole(addRoleBo);
        }else  {
            // 添加
            addCommunityRole(addRoleBo);
        }
    }

    private void addCommunityRole(AddRoleBo addRoleBo){
        // 保存角色
        Role role = new Role();
        role.setRoleName(addRoleBo.getRoleName());
        role.setDescription(addRoleBo.getDescription());
        role.setLabel(RoleTypeEnum.COMMUNITY.getValue());
        BaseEntityUtil.add(ShiroUtils.getUserId(), role);
        this.save(role);
        // 保存资源
        resourceService.addCommunityResource(role.getId(), addRoleBo.getPeriodIds());
    }

    private void updateCommunityRole(AddRoleBo addRoleBo){
        // 新期id
        List<String> newPeriodIds = addRoleBo.getPeriodIds();
        List<String> addPeriodIds = new ArrayList<>();
        List<String> deleteResourceId = new ArrayList<>();
        // 查询旧的关联关系
        List<RoleResource> roleResourceList = roleResourceService.list(new LambdaQueryWrapper<RoleResource>().eq(RoleResource::getRoleId, addRoleBo.getRoleId()));
        // 查询旧的关联的期的id
        List<String> oldResourceIds = roleResourceList.stream().map(RoleResource::getResourceId).collect(Collectors.toList());
        List<Resource> resourceList = resourceService.listByIds(oldResourceIds);
        Map<String, List<Resource>> oldMap = resourceList.stream().collect(Collectors.groupingBy(Resource::getParentId));
        oldMap.forEach((k, v) -> {
            if (! newPeriodIds.contains(k)){
                deleteResourceId.add(v.get(0).getId());
            }
        });
        addPeriodIds = newPeriodIds.stream().filter(periodId -> ObjectUtils.isEmpty(oldMap.get(periodId))).collect(Collectors.toList());

        // 删除不要的关联关系
        if (CollectionUtils.isNotEmpty(deleteResourceId)){
            roleResourceService.remove(new LambdaQueryWrapper<RoleResource>().eq(RoleResource::getRoleId, addRoleBo.getRoleId()).in(RoleResource::getResourceId, deleteResourceId));
        }
        // 添加新的关联关系
        if (CollectionUtils.isNotEmpty(addPeriodIds)){
            resourceService.addCommunityResource(addRoleBo.getRoleId(), addPeriodIds );
        }
    }

    /**
     * 查询社区管理角色
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param name    角色名称
     * @author lvyx
     * @since 2022/5/5 0:41
     *
     * */
    @Override
    public PageInfo<CommunityRoleVo> findCommunityRole(Integer pageNum, Integer pageSize, String name) {
        // 翻页查询角色
        PageHelper.startPage(pageNum, pageSize);
        List<CommunityRoleVo> communityRoleVoList = baseMapper.findCommunityRole(RoleTypeEnum.COMMUNITY.getValue(), name);
        PageInfo<CommunityRoleVo> communityRoleVoPageInfo = new PageInfo<>(communityRoleVoList);
        // 查询角色携带的资源信息
        communityRoleVoList = communityRoleVoList.stream().peek(communityRoleVo -> {
            List<CommunityResourceVo> communityResourceVoList = roleResourceService.findCommunityResourceByRoleId(communityRoleVo.getRoleId());
            communityRoleVo.setCommunityResourceVoList(communityResourceVoList);
            List<String> periodNameArr = communityResourceVoList.stream().map(CommunityResourceVo::getPeriodName).collect(Collectors.toList());
            communityRoleVo.setPeriodNameStr(org.apache.commons.lang.StringUtils.join(periodNameArr, ","));
        }).collect(Collectors.toList());
        communityRoleVoPageInfo.setList(communityRoleVoList);
        return communityRoleVoPageInfo;
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     * @author lvyx
     * @since 2022/5/5 2:08
     **/
    @Override
    public void deleteRole(String id) {
        // 删除角色
        Role role = this.getById(id);
        BaseEntityUtil.delete(ShiroUtils.getUserId(), role);
        this.updateById(role);
        this.removeById(role);
        userRoleService.deleteByRoleId(id);
        roleResourceService.deleteByRoleId(id);
    }

    /**
     * 编辑用户社区角色形象
     *
     * @param userId 用户id
     * @param roleIds 角色ids
     * @author lvyx
     * @since 2022/5/5 15:03
     **/
    @Override
    public void editUserRole(String userId, List<String> roleIds) {
        // 判断以前是否绑定过角色
        List<UserRole> communityRoleList = userRoleService.findUserRole(userId, RoleTypeEnum.COMMUNITY_ADMIN.getValue());
        if (CollectionUtils.isEmpty(communityRoleList)){
            List<Role> roleList = this.findRoleIdByLabel(RoleTypeEnum.COMMUNITY_ADMIN.getValue());
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleList.get(0).getId());
            BaseEntityUtil.add(ShiroUtils.getUserId(), userRole);
            userRoleService.save(userRole);
            List<Role> funcRoleList = this.findRoleIdByLabel(RoleTypeEnum.COMMUNITY_FUNCTION.getValue());
            UserRole funcUserRole = new UserRole();
            funcUserRole.setUserId(userId);
            funcUserRole.setRoleId(funcRoleList.get(0).getId());
            BaseEntityUtil.add(ShiroUtils.getUserId(), funcUserRole);
            userRoleService.save(funcUserRole);
        }
        List<UserRole> userRoleList = userRoleService.findUserRole(userId, RoleTypeEnum.USER.getValue());
        if (CollectionUtils.isEmpty(userRoleList)){
            List<Role> roleList = this.findRoleIdByLabel(RoleTypeEnum.USER.getValue());
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleList.get(0).getId());
            BaseEntityUtil.add(ShiroUtils.getUserId(), userRole);
//            userRoleService.save(userRole);
//            List<Role> funcRoleList = this.findRoleIdByLabel(RoleTypeEnum.USER_FUNCTION.getValue());
//            UserRole funcUserRole = new UserRole();
//            funcUserRole.setUserId(userId);
//            funcUserRole.setRoleId(funcRoleList.get(0).getId());
//            BaseEntityUtil.add(ShiroUtils.getUserId(), funcUserRole);
//            userRoleService.save(funcUserRole);
            // 激活用户信息
            LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(User::getId, userId)
                    .set(User::getIsEnable, BooleanTypeEnum.YES.getCode());
            userService.update(wrapper);
            // 激活用户角色信息
            userRoleService.activeRole(userId, RoleTypeEnum.USER.getValue(), RoleTypeEnum.USER_FUNCTION.getValue());
            /*
             * 处理通过，默认你创建灰色二维码
             *  1. 防止异常，失效所有二维码
             *  2. 创建灰色二维码
             **/
            communityGreenCodeService.invalidateAll(userId);
            CommunityGreyCode communityGreyCode = new CommunityGreyCode();
            BaseEntityUtil.add(ShiroUtils.getUserId(), communityGreyCode);
            communityGreyCode.setUserId(userId);
            communityGreyCodeService.save(communityGreyCode);
        }
        List<String> newRoleIds =  roleIds;
        List<String> deleteRoleIds = new ArrayList<>();
        List<UserRole> oldUserRole = userRoleService.findUserRole(userId, RoleTypeEnum.COMMUNITY.getValue());
        if (CollectionUtils.isNotEmpty(oldUserRole)){
            List<String> oldUserRoleIds = oldUserRole.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            newRoleIds = roleIds.stream().filter(newId -> !oldUserRoleIds.contains(newId)).collect(Collectors.toList());
            deleteRoleIds = oldUserRoleIds.stream().filter(oldId -> !roleIds.contains(oldId)).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(newRoleIds)){
            List<UserRole> addUserRoleList = newRoleIds.stream().map(newId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(newId);
                BaseEntityUtil.add(ShiroUtils.getUserId(), userRole);
                return userRole;
            }).collect(Collectors.toList());
            userRoleService.saveBatch(addUserRoleList);
        }
        if (CollectionUtils.isNotEmpty(deleteRoleIds)){
            userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId).in(UserRole::getRoleId, deleteRoleIds));
        }
    }



}
