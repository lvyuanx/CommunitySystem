package com.lvyx.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvyx.community.bo.QueryExceptionBo;
import com.lvyx.community.entity.CommunityException;
import com.lvyx.community.vo.ExceptionVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 社区异常表 Mapper 接口
 * </p>
 *
 * @author lvyx
 * @since 2022-05-02
 */
@Mapper
public interface CommunityExceptionMapper extends BaseMapper<CommunityException> {

    /**
     * 查询异常信息
     * @param queryExceptionBo  查询条件
     * @return com.lvyx.community.vo.ExceptionVo
     * @author lvyx
     * @since 2022/5/2 15:52
     **/
    List<ExceptionVo> findList(QueryExceptionBo queryExceptionBo);

}
