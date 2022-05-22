package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvyx.commons.enums.AddressStatusEnum;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityPeriod;
import com.lvyx.community.mapper.CommunityPeriodMapper;
import com.lvyx.community.service.CommunityPeriodService;
import com.lvyx.community.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 小区-期 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Service
public class CommunityPeriodServiceImpl extends ServiceImpl<CommunityPeriodMapper, CommunityPeriod> implements CommunityPeriodService {

    /**
     * 添加期信息
     *
     * @param period 期信息
     * @return CommunityPeriod
     * @author lvyx
     * @since 2022/2/4 15:31
     **/
    @Override
    public CommunityPeriod add(CommunityPeriod period) {
        BaseEntityUtil.add(ShiroUtils.getShiroUser().getId(), period);
        this.baseMapper.insert(period);
        return period;
    }

    /**
     * 查询期
     *
     * @return java.util.List<com.lvyx.community.entity.CommunityPeriod>
     * @author lvyx
     * @since 2022/2/4 17:05
     **/
    @Override
    public List<CommunityPeriod> find() {
        LambdaQueryWrapper<CommunityPeriod> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityPeriod::getCreateUser, ShiroUtils.getShiroUser().getId())
                .eq(CommunityPeriod::getIsEnable, BooleanTypeEnum.YES.getCode())
                .orderByAsc(CommunityPeriod::getSortNo);
        return this.baseMapper.selectList(wrapper);
    }

    /**
     * 查询所有期
     *
     * @return java.util.List<com.lvyx.community.entity.CommunityPeriod>
     * @author lvyx
     * @since 2022/2/5 19:46
     **/
    @Override
    public List<CommunityPeriod> findAll() {
        LambdaQueryWrapper<CommunityPeriod> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityPeriod::getIsEnable, BooleanTypeEnum.YES.getCode())
                .orderByAsc(CommunityPeriod::getSortNo);
        return this.baseMapper.selectList(wrapper);
    }

    /**
     * 根据期id得到小区信息
     *
     * @param id       期id
     * @param pageSize 页面大小
     * @param pageNum  起始页
     * @return com.github.pagehelper.PageInfo<com.lvyx.community.vo.CommunityInfo>
     * @author lvyx
     * @since 2022/2/6 18:30
     **/
    @Override
    public PageInfo<PeriodInfoVo> getInfoByPeriodId(String id, Integer pageSize, Integer pageNum) {
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<ComprehensiveVo> comprehensiveVoList = this.baseMapper.findComprehensiveByPeriodId(id);
        // 封装分页信息
        PageInfo<ComprehensiveVo> comprehensiveVoPageInfo = new PageInfo<>(comprehensiveVoList);
        // 小区信息分页
        PageInfo<PeriodInfoVo> periodInfoVoPageInfo = new PageInfo<>();
        // 复制分页信息到目标分页中
        BeanUtils.copyProperties(comprehensiveVoPageInfo, periodInfoVoPageInfo);
        // 对栋进行分组
        ArrayList<PeriodInfoVo> periodInfoVoList = null;
        if (CollectionUtils.isNotEmpty(comprehensiveVoList)) {
            Map<String, List<ComprehensiveVo>> map = comprehensiveVoList.stream()
                    .filter(f -> !ObjectUtils.isEmpty(f.getBuildingId()))
                    .collect(Collectors.groupingBy(ComprehensiveVo::getBuildingId));
            periodInfoVoList = new ArrayList<>();
            ArrayList<PeriodInfoVo> finalPeriodInfoVoList = periodInfoVoList;
            map.forEach((k, v) -> {
                v.forEach(building -> {
                    PeriodInfoVo periodInfoVo = new PeriodInfoVo();
                    periodInfoVo.setBuildingNumber(building.getBuildingNumber());
                    periodInfoVo.setBuildingId(building.getBuildingId());
                    periodInfoVo.setSortNo(building.getSortNo());
                    ArrayList<InfoVo> infoVoList = new ArrayList<>();
                    List<UnitVo> unitVoList = building.getUnitVoList();
                    if (CollectionUtils.isNotEmpty(unitVoList)) {
                        List<UnitVo> sortUnitList = unitVoList.stream().sorted(Comparator.comparing(UnitVo::getSortNo)).collect(Collectors.toList());
                        sortUnitList.forEach(unit -> {
                            InfoVo infoVo = new InfoVo();
                            infoVo.setUnitId(unit.getId());
                            infoVo.setUnitNumber(unit.getUnitNumber());
                            List<LayerVo> layerVoList = unit.getLayerVoList();
                            if (CollectionUtils.isNotEmpty(layerVoList)) {
                                layerVoList.forEach(layerVo -> {
                                    List<HouseholdVo> householdVoList = layerVo.getHouseholdVoList();
                                    if (CollectionUtils.isNotEmpty(householdVoList)) {
                                        if (!CollectionUtils.isEmpty(householdVoList)) {
                                            // 户信息不为空
                                            infoVo.setAllHousehold(householdVoList.size());
                                            List<Integer> sizeList = householdVoList.stream().map(h -> {
                                                int size = 0;
                                                List<HouseholdUserVo> householdUserVoList = h.getHouseholdUserVoList();
                                                if (CollectionUtils.isEmpty(householdUserVoList)) {
                                                    // 用户不为空
                                                    size = householdUserVoList.size();
                                                }
                                                return size;
                                            }).collect(Collectors.toList());
                                            int userSize = sizeList.stream().mapToInt(s -> s).sum();
                                            infoVo.setAllUser(userSize);
                                        }
                                        if (!ObjectUtils.isEmpty(infoVo.getUnitId())) {
                                            infoVoList.add(infoVo);
                                        }
                                    }
                                });
                            }
                        });
                    }
                    periodInfoVo.setInfoVoList(infoVoList);
                    finalPeriodInfoVoList.add(periodInfoVo);
                });
            });
        }
        if (CollectionUtils.isNotEmpty(periodInfoVoList)) {
            List<PeriodInfoVo> sortInfo = periodInfoVoList.stream().sorted(Comparator.comparing(PeriodInfoVo::getSortNo)).collect(Collectors.toList());
            periodInfoVoPageInfo.setList(sortInfo);
        }
        return periodInfoVoPageInfo;
    }

    /**
     * 获取社区级联选择结构
     *
     * @return java.util.List<com.lvyx.community.vo.CascadeVo>
     * @author lvyx
     * @since 2022/2/8 6:06
     **/
    @Override
    public List<CascadeVo> getCascadeVo() {
//        List<CascadeVo> cascadeVoList = this.baseMapper.findCommunityAllInfo();
//        List<CascadeVo> sortList = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(cascadeVoList)){
//            // 排序期
//            List<CascadeVo> sortedPeriod = cascadeVoList.stream().sorted(Comparator.comparing(CascadeVo::getSortNo)).collect(Collectors.toList());
//            sortedPeriod.forEach(p -> {
//                List<CascadeVo> building = p.getChildren();
//                List<CascadeVo> periodChild = new ArrayList<>();
//                if (CollectionUtils.isNotEmpty(building)){
//                    // 排序栋
//                    List<CascadeVo> sortedBuilding = building.stream().sorted(Comparator.comparing(CascadeVo::getSortNo)).collect(Collectors.toList());
//                    List<CascadeVo> buildingChild = new ArrayList<>();
//                    if (CollectionUtils.isNotEmpty(sortedBuilding)){
//                        sortedBuilding.forEach(b -> {
//                            periodChild.add(b);
//                            List<CascadeVo> uinty = b.getChildren();
//                            List<CascadeVo> unitChild = new ArrayList<>();
//                            if (CollectionUtils.isNotEmpty(uinty)){
//                                // 排序单元
//                                List<CascadeVo> sortedUnity = uinty.stream().sorted(Comparator.comparing(CascadeVo::getSortNo)).collect(Collectors.toList());
//                                sortedUnity.forEach(u -> {
//                                    buildingChild.add(u);
//                                    List<CascadeVo> layer = u.getChildren();
//                                    if (CollectionUtils.isNotEmpty(layer)){
//                                        // 排序层
//                                        List<CascadeVo> sortedLayer = layer.stream().sorted(Comparator.comparing(CascadeVo::getSortNo)).collect(Collectors.toList());
//                                        List<CascadeVo> layerChild = new ArrayList<>();
//                                        if (CollectionUtils.isNotEmpty(sortedLayer)){
//                                            // 排序户
//                                            sortedLayer.forEach(l -> {
//                                                unitChild.add(l);
//                                                List<CascadeVo> sortHousehold = new ArrayList<>();
//                                                List<CascadeVo> household = l.getChildren();
//                                                if (CollectionUtils.isNotEmpty(household)) {
//                                                    sortHousehold = household.stream().sorted(Comparator.comparing(CascadeVo::getSortNo)).collect(Collectors.toList());
//                                                    sortHousehold.forEach(h ->{
//                                                        layerChild.add(l);
//                                                    });
//                                                }
//                                            });
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                    }
//                }
//            });
//        }
        return this.baseMapper.findCommunityAllInfo();
    }
    

    /**
     * 获取当前登录人的住址信息
     *
     * @return AddressVo
     * @author lvyx
     * @since 2022/2/9 21:24
     **/
    @Override
    public AddressVo findInfoByUser() {
        return this.baseMapper.findInfoByUser(ShiroUtils.getUserId());
    }

    /**
     * 查询登录用户是否含有住址信息
     *
     * @return java.lang.Integer
     * @author lvyx
     * @since 2022/5/22 16:12
     **/
    @Override
    public Integer checkUserHasAddress() {
        Integer isEable = this.baseMapper.checkUserHasAddress(ShiroUtils.getUserId());
        if(ObjectUtils.isEmpty(isEable)){
            return AddressStatusEnum.NO_CREATE.getCode();
        }
        return isEable;
    }
}
