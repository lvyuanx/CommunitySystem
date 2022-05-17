package com.lvyx.commons.annotation.mapper;

import com.lvyx.commons.annotation.entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}
