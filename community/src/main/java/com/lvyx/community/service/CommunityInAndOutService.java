package com.lvyx.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvyx.community.entity.CommunityInAndOut;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 小区-进出登记表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
public interface CommunityInAndOutService extends IService<CommunityInAndOut> {

    /**
     * 添加社区进出登记
     * @param communityInAndOut  社区进出登记信息
     * @author lvyx
     * @since 2022/4/20 10:12
     **/
    @Transactional(rollbackFor = Exception.class)
    void addCommunityInAndOut(CommunityInAndOut communityInAndOut);

}
