package com.lvyx.author.mapper;

import com.lvyx.author.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 根据用户id查询用户拥有的资源ids
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     * @author lvyx
     * @since 2021/12/24 9:37
     **/
    List<Resource> getResourcesByUserId(@Param("userId") String userId);

}
