package com.lvyx.community.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvyx.commons.enums.QrCodeColorEnum;
import com.lvyx.commons.exception.LExceException;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.bo.QueryExceptionBo;
import com.lvyx.community.entity.CommunityException;
import com.lvyx.community.entity.CommunityRedCode;
import com.lvyx.community.entity.CommunityYellowCode;
import com.lvyx.community.mapper.CommunityExceptionMapper;
import com.lvyx.community.service.*;
import com.lvyx.community.vo.ExceptionVo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 社区异常表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2022-05-02
 */
@Service
public class CommunityExceptionServiceImpl extends ServiceImpl<CommunityExceptionMapper, CommunityException> implements CommunityExceptionService {

    @Resource
    private CommunityGreenCodeService communityGreenCodeService;

    @Resource
    private CommunityYellowCodeService communityYellowCodeService;

    @Resource
    private CommunityRedCodeService communityRedCodeService;

    @Resource
    private CommunityHouseholdUserService communityHouseholdUserService;


    /**
     * 查询异常列表
     *
     * @param pageNum     页码
     * @param pageSize    页大小
     * @param queryExceptionBo 异常查询条件
     * @return com.github.pagehelper.PageInfo<com.lvyx.community.vo.ExceptionBo>
     * @author lvyx
     * @since 2022/5/2 15:18
     **/
    @Override
    public PageInfo<ExceptionVo> findByQuery(Integer pageNum, Integer pageSize,  QueryExceptionBo queryExceptionBo) {
        List<String> periodIds = communityHouseholdUserService.findUserPeriodId(ShiroUtils.getUserId());
        queryExceptionBo.setPeriodIds(periodIds);
        // 分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<ExceptionVo> exceptionVoList = this.baseMapper.findList(queryExceptionBo);
        return new PageInfo<>(exceptionVoList);
    }

    /**
     * 修改异常状态
     *
     * @param id     异常id
     * @param status 异常状态
     * @param result 处理结果
     * @param isTransferredCode 是否转码
     * @author lvyx
     * @since 2022/5/3 12:50
     **/
    @Override
    public void updateStatus(String id, Integer status, String result, String isTransferredCode) throws LExceException {
        CommunityException communityException = this.getById(id);
        if (ObjectUtils.isEmpty(communityException)) {
            throw new LExceException("异常信息不存在");
        }
        communityException.setIsDealWith(status);
        if (StringUtils.isNotBlank(result)) {
            communityException.setResult(result);
        }
        BaseEntityUtil.edit(ShiroUtils.getUserId(), communityException);
        this.updateById(communityException);

        // 判断是否需要转码
        if (StringUtils.isNotBlank(isTransferredCode)) {
            // 失效所有二维码
            communityGreenCodeService.invalidateAll(communityException.getCreateUser());
            // 转码
            if (isTransferredCode.equals(QrCodeColorEnum.YELLOW.getValue())){
                CommunityYellowCode communityYellowCode = new CommunityYellowCode();
                communityYellowCode.setUserId(communityException.getCreateUser());
                BaseEntityUtil.add(ShiroUtils.getUserId(), communityYellowCode);
                communityYellowCodeService.save(communityYellowCode);
            }else if (isTransferredCode.equals(QrCodeColorEnum.RED.getValue())){
                CommunityRedCode communityRedCode = new CommunityRedCode();
                communityRedCode.setUserId(communityException.getCreateUser());
                BaseEntityUtil.add(ShiroUtils.getUserId(), communityRedCode);
                communityRedCodeService.save(communityRedCode);
            }else {
                throw new LExceException("转码类型不存在");
            }
        }
    }
}
