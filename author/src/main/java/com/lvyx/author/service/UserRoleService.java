package com.lvyx.author.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvyx.author.entity.UserRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 角色绑定
     * @param userId 用户id
     * @param roleIds 角色id
     * @author lvyx
     * @since 2022/2/8 19:20
     **/
    @Transactional(rollbackFor = Exception.class)
    void bind(String userId, List<String> roleIds);

    /**
     * 激活角色
     * @param userId 用户id
     * @param label 角色标签
     * @author lvyx
     * @since 2022/5/4 18:01
     **/
    void activeRole(String userId, String... label);

    /**
     * 根据角色id删除关联关系
     * @param roleId 角色id
     * @author lvyx
     * @since 2022/5/5 2:15
     **/
    @Transactional(rollbackFor = Exception.class)
    void deleteByRoleId(String roleId);


    /**
     * 查询用户角色信息
     * @param userId userId
     * @param label  角色标签
     * @return java.util.List<com.lvyx.author.entity.UserRole>
     * @author lvyx
     * @since 2022/5/5 15:17
     **/
    List<UserRole> findUserRole(String userId, String label);




}
