package com.lvyx.author.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvyx.author.bo.QueryUserListBo;
import com.lvyx.author.entity.User;
import com.lvyx.community.vo.UserHouseholdVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> listByUser(@Param("user") User user);

    List<UserHouseholdVo> findUserList(QueryUserListBo user);
}
