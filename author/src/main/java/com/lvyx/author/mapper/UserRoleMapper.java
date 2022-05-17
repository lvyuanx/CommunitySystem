package com.lvyx.author.mapper;

import com.lvyx.author.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<UserRole> findUserRole(@Param("userId") String userId, @Param("label") String label);

}
