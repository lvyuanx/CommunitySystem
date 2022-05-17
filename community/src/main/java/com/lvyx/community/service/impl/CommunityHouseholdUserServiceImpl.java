package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvyx.commons.enums.BooleanTypeEnum;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.bo.QueryUserHouseholdBo;
import com.lvyx.community.entity.CommunityHouseholdUser;
import com.lvyx.community.mapper.CommunityHouseholdUserMapper;
import com.lvyx.community.service.CommunityHouseholdService;
import com.lvyx.community.service.CommunityHouseholdUserService;
import com.lvyx.community.vo.UserHouseholdVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户与户关联表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-02-01
 */
@Service
public class CommunityHouseholdUserServiceImpl extends ServiceImpl<CommunityHouseholdUserMapper, CommunityHouseholdUser> implements CommunityHouseholdUserService {

    @Resource
    private CommunityHouseholdService communityHouseholdService;



    /**
     * 用户绑定房间
     *
     * @param userId      用户id
     * @param householdId 户id
     * @author lvyx
     * @since 2022/2/8 19:34
     **/
    @Override
    public void bind(String userId, String householdId) {
        this.bind(userId, householdId, null);
    }

    /**
     * 用户绑定房间
     *
     * @param userId      用户id
     * @param householdId 户id
     * @param isEnable    是否启用
     * @author lvyx
     * @since 2022/2/8 19:34
     **/
    @Override
    public void bind(String userId, String householdId, Integer isEnable) {
        CommunityHouseholdUser communityHouseholdUser = new CommunityHouseholdUser();
        communityHouseholdUser.setHouseholdId(householdId);
        communityHouseholdUser.setUserId(userId);
        BaseEntityUtil.add(userId, communityHouseholdUser);
        if (ObjectUtils.isNotEmpty(isEnable)) {
            communityHouseholdUser.setIsEnable(isEnable);
        }else {
            communityHouseholdUser.setIsEnable(BooleanTypeEnum.NO.getCode());
        }
        this.baseMapper.insert(communityHouseholdUser);
        String periodId = communityHouseholdService.findPeriodIByHouseholdId(householdId);
        this.baseMapper.updateUserPeriod(userId, periodId);
    }

    /**
     * 查询用户绑定房间信息
     *
     * @param queryUserHouseholdBo 请求参数
     * @param pageNum              起始页
     * @param pageSize             页面大小
     * @return com.github.pagehelper.PageInfo<com.lvyx.community.vo.UserHouseHoldVo>
     * @author lvyx
     * @since 2022/4/25 21:48
     **/
    @Override
    public PageInfo<UserHouseholdVo> findUserHousehold(QueryUserHouseholdBo queryUserHouseholdBo, Integer pageNum, Integer pageSize) {
        // 查询当前用户资源
        List<String> periodIds = this.findUserPeriodId(ShiroUtils.getUserId());
        queryUserHouseholdBo.setPeriodIds(periodIds);
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        // 查询
        List<UserHouseholdVo> userHouseholdVoList = this.baseMapper.findUserHoseholdByQuery(queryUserHouseholdBo);
        return new PageInfo<>(userHouseholdVoList);
    }


    /**
     * 修改用户住址信息
     *
     * @param userId      用户id
     * @param householdId 新户id
     * @param isEnable   是否启用
     * @author lvyx
     * @since 2022/4/27 22:08
     **/
    @Override
    public void updateUserHousehold(String userId, String householdId, Integer isEnable) {
        // 逻辑删除原来的关联关系
        LambdaUpdateWrapper<CommunityHouseholdUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CommunityHouseholdUser::getUserId, userId);
        this.remove(wrapper);
        // 新增新的关联关系
        this.bind(userId, householdId, isEnable);
    }

    /**
     * 根据用户id删除关联关系
     *
     * @param userId 用户id
     * @author lvyx
     * @since 2022/4/30 15:00
     **/
    @Override
    public void deleteByUser(String userId) {
        LambdaQueryWrapper<CommunityHouseholdUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityHouseholdUser::getUserId, userId);
        this.remove(wrapper);
    }

    @Override
    public String findPeriodIdByUserId(String userId) {
        return this.baseMapper.findPeriodIdByUserId(userId);
    }

    @Override
    public List<String> findUserPeriodId(String userId) {
        return this.baseMapper.findUserPeriodId(userId);
    }
}
