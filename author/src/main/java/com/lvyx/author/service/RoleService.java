package com.lvyx.author.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lvyx.author.bo.AddRoleBo;
import com.lvyx.author.entity.Resource;
import com.lvyx.author.entity.Role;
import com.lvyx.author.entity.User;
import com.lvyx.author.vo.CommunityRoleVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户信息得到用户权限信息
     * @param user 用户信息
     * @return java.util.List<com.lvyx.author.entity.Role>
     * @author lvyx
     * @since 2021/12/24 13:49
     **/
    List<Role>  findRoleListByUser(User user);

    /**
     * 根据用户id得到用户角色Ids
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/24 13:53
     **/
    List<String> findRoleIdsByUserId(String userId);

    /**
     * 根据用户的id得到角色信息
     * @param userId 用户id
     * @return java.util.List<com.lvyx.author.entity.Role>
     * @author lvyx
     * @since 2021/12/31 14:46
     **/
    List<Role> findRoleByUserId(String userId);

    /**
     * 查询当前用户资源
     * @return java.util.List<com.lvyx.author.entity.Resource>
     * @author lvyx
     * @since 2022/5/4 17:18
     **/
    List<Resource> findCurrentUserFunction();


    /**
     * 根据角色类型查询角色信息
     * @param label  角色标签
     * @return Role
     * @author lvyx
     * @since 2022/5/4 17:49
     **/
    List<Role> findRoleIdByLabel( String... label);

    /**
     * 添加社区管理角色
     * @param addRoleBo 社区管理角色信息
     * @author lvyx
     * @since 2022/5/4 23:51
     **/
    @Transactional(rollbackFor = Exception.class)
    void editCommunityRole(AddRoleBo addRoleBo);

    /**
     * 查询社区管理角色
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @param name    角色名称
     * @author lvyx
     * @since 2022/5/5 0:41
     *
     **/
    PageInfo<CommunityRoleVo> findCommunityRole(Integer pageNum, Integer pageSize, String name);

    /**
     * 删除角色
     * @param id 角色id
     * @author lvyx
     * @since 2022/5/5 2:08
     **/
    @Transactional(rollbackFor = Exception.class)
    void deleteRole(String id);

    /**
     * 编辑用户社区角色形象
     * @param userId 用户ids
     * @param roleIds 角色ids
     * @author lvyx
     * @since 2022/5/5 15:03
     **/
    @Transactional(rollbackFor = Exception.class)
    void editUserRole(String userId, List<String> roleIds);






}
