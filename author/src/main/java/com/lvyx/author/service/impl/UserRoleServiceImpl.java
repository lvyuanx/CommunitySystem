package com.lvyx.author.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvyx.author.entity.Role;
import com.lvyx.author.entity.UserRole;
import com.lvyx.author.mapper.UserRoleMapper;
import com.lvyx.author.service.RoleService;
import com.lvyx.author.service.UserRoleService;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.enums.RoleTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Resource
    private RoleService roleService;

    /**
     * 角色绑定
     *
     * @param userId 用户id
     * @param roleIds 角色ids
     * @author lvyx
     * @since 2022/2/8 19:20
     **/
    @Override
    public void bind(String userId, List<String> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<UserRole> userRoleList = roleIds.stream().map(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                BaseEntityUtil.add(userId, userRole);
                // 不生效，等待管理员激活
                userRole.setIsEnable(BooleanTypeEnum.NO.getCode());
                return userRole;
            }).collect(Collectors.toList());
            this.saveBatch(userRoleList);
        }
    }

    /**
     * 激活角色
     *
     * @param userId 用户id
     * @param Label 角色标签
     * @author lvyx
     * @since 2022/5/4 18:01
     **/
    @Override
    public void activeRole(String userId, String... Label) {
        List<Role> roleList = roleService.findRoleIdByLabel(Label);
        // 激活普通角色
        List<Role> userRole = roleList.stream().filter(item -> item.getLabel().equals(RoleTypeEnum.USER.getValue())).collect(Collectors.toList());
        LambdaUpdateWrapper<UserRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, userRole.get(0).getId())
                .set(UserRole::getIsEnable, BooleanTypeEnum.YES.getCode());
        this.update(wrapper);
        // 添加资源权限
        List<String> funcRoleIds = roleList.stream().filter(item -> item.getLabel().equals(RoleTypeEnum.USER_FUNCTION.getValue())).map(Role::getId).collect(Collectors.toList());
        List<UserRole> userRoleList = funcRoleIds.stream().map(roleId -> {
            UserRole funcUserRole = new UserRole();
            funcUserRole.setRoleId(roleId);
            funcUserRole.setUserId(userId);
            BaseEntityUtil.add(ShiroUtils.getUserId(), funcUserRole);
            return funcUserRole;
        }).collect(Collectors.toList());
        this.saveBatch(userRoleList);
    }

    /**
     * 根据角色id删除关联关系
     *
     * @param roleId 角色id
     * @author lvyx
     * @since 2022/5/5 2:15
     **/
    @Override
    public void deleteByRoleId(String roleId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, roleId);
        List<UserRole> userRoleList = this.list(wrapper);
        if (CollectionUtils.isNotEmpty(userRoleList)){
            String userId = ShiroUtils.getUserId();
            List<UserRole> deleteUserRoleList = userRoleList.stream().peek(item -> {
                BaseEntityUtil.delete(userId, item);
            }).collect(Collectors.toList());
            this.updateBatchById(deleteUserRoleList);
            List<String> deleteUserRoleIds = deleteUserRoleList.stream().map(UserRole::getId).collect(Collectors.toList());
            this.removeByIds(deleteUserRoleIds);
        }
    }

    /**
     * 查询用户角色信息
     *
     * @param userId userId
     * @param label  角色标签
     * @return java.util.List<com.lvyx.author.entity.UserRole>
     * @author lvyx
     * @since 2022/5/5 15:17
     **/
    @Override
    public List<UserRole> findUserRole(String userId, String label) {
        return this.baseMapper.findUserRole(userId, label);
    }

}
