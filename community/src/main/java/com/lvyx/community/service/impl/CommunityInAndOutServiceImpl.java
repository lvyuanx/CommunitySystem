package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityGreenCode;
import com.lvyx.community.entity.CommunityInAndOut;
import com.lvyx.community.entity.CommunityYellowCode;
import com.lvyx.community.mapper.CommunityInAndOutMapper;
import com.lvyx.community.service.CommunityGreenCodeService;
import com.lvyx.community.service.CommunityInAndOutService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 小区-进出登记表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
@Service
public class CommunityInAndOutServiceImpl extends ServiceImpl<CommunityInAndOutMapper, CommunityInAndOut> implements CommunityInAndOutService {

    @Resource
    private CommunityGreenCodeService communityGreenCodeService;

    @Resource
    private CommunityYellowCodeServiceImpl communityYellowCodeService;


    /**
     * 添加社区进出登记
     *
     * @param communityInAndOut 社区进出登记信息
     * @author lvyx
     * @since 2022/4/20 10:12
     **/
    @Override
    public void addCommunityInAndOut(CommunityInAndOut communityInAndOut) {
        // 保存社区进出登记信息
        BaseEntityUtil.add(ShiroUtils.getUserId(), communityInAndOut);
        this.save(communityInAndOut);
        // 判断体温是否正常
        if (communityInAndOut.getIsHealth().toString().equals(BooleanTypeEnum.NO.getValue())) {
            /*
             * 体温异常
             *  1. 如果是绿码，则更新为黄码
             *  2. 如果非绿码，则不更新
             **/
            // 判断是否是绿码
            CommunityGreenCode greenCode = communityGreenCodeService.findByUserId(ShiroUtils.getUserId());
            if(ObjectUtils.isNotNull(greenCode)){
                // 失效绿码
                greenCode.setIsEnable(BooleanTypeEnum.NO.getCode());
                communityGreenCodeService.updateById(greenCode);
                // 创建黄码
                CommunityYellowCode yellowCode = new CommunityYellowCode();
                yellowCode.setUserId(ShiroUtils.getUserId());
                yellowCode.setIsEnable(BooleanTypeEnum.YES.getCode());
                BaseEntityUtil.add(ShiroUtils.getUserId(), yellowCode);
                communityYellowCodeService.save(yellowCode);
            }
        }
    }
}
