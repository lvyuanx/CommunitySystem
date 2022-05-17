package com.lvyx.commons.annotation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvyx.commons.annotation.entity.Log;
import com.lvyx.commons.annotation.mapper.LogMapper;
import com.lvyx.commons.annotation.service.LogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

}
