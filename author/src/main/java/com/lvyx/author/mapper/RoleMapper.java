package com.lvyx.author.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvyx.author.entity.Resource;
import com.lvyx.author.entity.Role;
import com.lvyx.author.entity.User;
import com.lvyx.author.vo.CommunityRoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户信息查询角色权限;
     * @param user 用户信息
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/24 10:09
     **/
    List<Role> getRoleIdsByUser(@Param("user") User user);


    /**
     * 根据角色id查询资源信息
     * @param userId 用户id
     * @return java.util.List<com.lvyx.author.entity.Resource>
     * @author lvyx
     * @since 2022/5/4 17:19
     **/
    List<Resource> findUserResource(@Param("userId") String userId, @Param("resourceType") String resourceType);


    List<CommunityRoleVo> findCommunityRole(@Param("label") String label, @Param("name") String name);
}
