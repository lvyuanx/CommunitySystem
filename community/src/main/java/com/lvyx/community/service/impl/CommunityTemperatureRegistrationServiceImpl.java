package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvyx.commons.config.CommunityProperties;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityGreenCode;
import com.lvyx.community.entity.CommunityTemperatureRegistration;
import com.lvyx.community.mapper.CommunityTemperatureRegistrationMapper;
import com.lvyx.community.service.CommunityGreenCodeService;
import com.lvyx.community.service.CommunityRedCodeService;
import com.lvyx.community.service.CommunityTemperatureRegistrationService;
import com.lvyx.community.service.CommunityYellowCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 小区-体温登记表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-13
 */
@Service
public class CommunityTemperatureRegistrationServiceImpl extends ServiceImpl<CommunityTemperatureRegistrationMapper, CommunityTemperatureRegistration> implements CommunityTemperatureRegistrationService {

    @Resource
    private CommunityGreenCodeService communityGreenCodeService;

    @Resource
    private CommunityRedCodeService communityRedCodeService;

    @Resource
    private CommunityYellowCodeService communityYellowCodeService;

    @Resource
    private CommunityProperties communityProperties;

    /**
     * 添加一条体温记录信息
     *
     * @param communityTemperatureRegistration 体温登记信息
     * @author lvyx
     * @since 2022/3/12 15:59
     **/
    @Override
    public void add(CommunityTemperatureRegistration communityTemperatureRegistration) {
        // 获取用户id
        String userId = ShiroUtils.getUserId();
        // 判断当天是否已经登记过，如果登记，则以当前登记信息为最终信息
        LambdaQueryWrapper<CommunityTemperatureRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityTemperatureRegistration::getCreateUser, userId)
                .gt(CommunityTemperatureRegistration::getCreateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIN))
                .lt(CommunityTemperatureRegistration::getCreateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        List<CommunityTemperatureRegistration> communityTemperatureRegistrationList = this.baseMapper.selectList(wrapper);
        if(CollectionUtils.isNotEmpty(communityTemperatureRegistrationList)){
            // 将历史信息改为禁用状态
            List<CommunityTemperatureRegistration> disableList = communityTemperatureRegistrationList.stream().filter(item -> item.getIsEnable().equals(BooleanTypeEnum.YES.getCode()))
                    .peek(item -> item.setIsEnable(BooleanTypeEnum.NO.getCode())).collect(Collectors.toList());
            this.updateBatchById(disableList);
            if (CollectionUtils.isNotEmpty(disableList)) {
                communityTemperatureRegistration.setContinuous(disableList.get(0).getContinuous() - 1);
            }else {
                communityTemperatureRegistration.setContinuous(0);
            }
        }
        // 如果当前体温大于37.5，并且之前记录为绿码或者灰码，则记录黄码
        if(communityTemperatureRegistration.getTemperature() > Float.parseFloat(communityProperties.getAbnormalBodyTemperature())){
            communityGreenCodeService.addRecord(userId);
        }
        // 判断是否在红码，黄码表中，并且当前打卡为不健康，健康打开次数清零
        if (communityRedCodeService.isExist(ShiroUtils.getUserId()) || communityYellowCodeService.isExist(ShiroUtils.getUserId())) {
            if (communityTemperatureRegistration.getTemperature() > Float.parseFloat(communityProperties.getAbnormalBodyTemperature())) {
                // 后面的操作会+1，健康打卡次数清零
                communityTemperatureRegistration.setContinuous(-1);
            }
        }
        BaseEntityUtil.add(ShiroUtils.getUserId(), communityTemperatureRegistration);
        communityTemperatureRegistration.setContinuous(communityTemperatureRegistration.getContinuous() + 1);
        this.save(communityTemperatureRegistration);
        // 修改二维码状态，如果连续打卡七天，并且绿码表中没有当前用户的二维码，则添加一个
        if(communityTemperatureRegistration.getContinuous() >= communityProperties.getConsecutiveDays()){
            // 查看绿码表中是否有当前用户的二维码，没有就进行添加
            CommunityGreenCode communityGreenCode = new CommunityGreenCode();
            communityGreenCode.setUserId(userId);
            BaseEntityUtil.add(ShiroUtils.getUserId(), communityGreenCode);
            communityGreenCodeService.addGreenCode(communityGreenCode);
        }
    }

    /**
     * 查看当前登陆人连续打卡天数
     *
     * @return java.lang.Integer
     * @author lvyx
     * @since 2022/3/20 22:03
     **/
    @Override
    public Integer continuousDay() {
        // 获取当前登陆人id
        String userId = ShiroUtils.getUserId();
        // 获取前一天到今天时间范围
        LocalDateTime start = LocalDateTime.of(LocalDate.now().plusDays(-1), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        // 根据时间查询前一天是否打卡
        LambdaQueryWrapper<CommunityTemperatureRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(CommunityTemperatureRegistration::getCreateTime, end)
                .ge(CommunityTemperatureRegistration::getCreateTime, start)
                .eq(CommunityTemperatureRegistration::getIsEnable, BooleanTypeEnum.YES.getCode())
                .eq(CommunityTemperatureRegistration::getCreateUser, userId)
                .orderByDesc(CommunityTemperatureRegistration::getCreateTime);
        // 防止数据错误查询列表
        List<CommunityTemperatureRegistration> communityTemperatureRegistrationList = this.baseMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(communityTemperatureRegistrationList)){
            return communityTemperatureRegistrationList.get(0).getContinuous();
        }else {
            return 0;
        }
    }

    /**
     * 获得当前登录人的二维码
     *
     * @return java.awt.image.BufferedImage
     * @author lvyx
     * @since 2022/4/5 22:06
     **/
    @Override
    public BufferedImage getQrcode() {
        return null;

    }

    /**
     * 查询打卡记录
     *
     * @param pageNum  页码
     * @param pageSize 页码大小
     * @param isEnable 是否作废
     * @param isHealth 是否健康
     * @return com.github.pagehelper.PageInfo<com.lvyx.community.entity.CommunityTemperatureRegistration>
     * @author lvyx
     * @since 2022/5/6 2:39
     **/
    @Override
    public PageInfo<CommunityTemperatureRegistration> findList(Integer pageNum, Integer pageSize, Integer isEnable, Integer isHealth) {
        String userId = ShiroUtils.getUserId();
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<CommunityTemperatureRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityTemperatureRegistration::getCreateUser, userId)
                .eq(ObjectUtils.isNotEmpty(isEnable), CommunityTemperatureRegistration::getIsEnable, isEnable);
        if (ObjectUtils.isNotEmpty(isHealth)){
            if(isHealth.equals(BooleanTypeEnum.NO.getCode())){
                wrapper.le(CommunityTemperatureRegistration::getTemperature, Float.parseFloat(communityProperties.getAbnormalBodyTemperature()));
            }else {
                wrapper.gt(CommunityTemperatureRegistration::getTemperature, Float.parseFloat(communityProperties.getAbnormalBodyTemperature()));
            }
        }
        wrapper.orderByDesc(CommunityTemperatureRegistration::getCreateTime);
        List<CommunityTemperatureRegistration> communityTemperatureRegistrationList = this.list(wrapper);
        return new PageInfo<>(communityTemperatureRegistrationList);
    }

}
