package com.lvyx.author.mapper;

import com.lvyx.author.entity.RoleResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvyx.author.vo.CommunityResourceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色资源关联表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Mapper
public interface RoleResourceMapper extends BaseMapper<RoleResource> {

    List<String> findUserPeriodId(@Param("userId") String userId);

    /**
     * 查询社区角色资源信息，根据角色id查询
     *
     * @param roleId 角色id
     * @return java.util.List<com.lvyx.author.vo.CommunityResourceVo>
     * @author lvyx
     * @since 2022/5/13 20:30
     **/
    List<CommunityResourceVo> findCommunityResourceByRoleId(@Param("roleId") String roleId);

}
