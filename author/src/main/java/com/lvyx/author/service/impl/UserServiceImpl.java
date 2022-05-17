package com.lvyx.author.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvyx.author.bo.QueryUserListBo;
import com.lvyx.author.bo.RegisterBO;
import com.lvyx.author.entity.Role;
import com.lvyx.author.entity.User;
import com.lvyx.author.mapper.UserMapper;
import com.lvyx.author.service.RoleResourceService;
import com.lvyx.author.service.RoleService;
import com.lvyx.author.service.UserRoleService;
import com.lvyx.author.service.UserService;
import com.lvyx.author.shiro.utils.ShiroUserUtils;
import com.lvyx.commons.config.SystemProperties;
import com.lvyx.commons.encrypt_decrypt.factory.LEncryptDecryptFactory;
import com.lvyx.commons.enums.*;
import com.lvyx.commons.exception.LException;
import com.lvyx.commons.exception.LMailException;
import com.lvyx.commons.pojo.RoleVo;
import com.lvyx.commons.pojo.ShiroUser;
import com.lvyx.commons.utils.BaseEntityUtil;
import com.lvyx.commons.utils.ShiroUtils;
import com.lvyx.community.entity.CommunityGreyCode;
import com.lvyx.community.entity.CommunityHouseholdUser;
import com.lvyx.community.service.CommunityGreenCodeService;
import com.lvyx.community.service.CommunityGreyCodeService;
import com.lvyx.community.service.CommunityHouseholdUserService;
import com.lvyx.community.vo.UserHouseholdVo;
import com.lvyx.mail.bo.BaseSenderMessageBo;
import com.lvyx.mail.service.CommunityMessageService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private LEncryptDecryptFactory lEncryptDecryptFactory;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private CommunityHouseholdUserService communityHouseholdUserService;

    @Resource
    private CommunityMessageService communityMessageService;

    @Resource
    private CommunityGreenCodeService communityGreenCodeService;

    @Resource
    private CommunityGreyCodeService communityGreyCodeService;

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private RoleService roleService;

    @Resource
    private RoleResourceService roleResourceService;



    /**
     * 根据用户信息查询用户
     * @param user 用户实体
     * @return java.util.List<com.lvyx.author.entity.User>
     * @author lvyx
     * @since 2022/1/23 16:38
     **/
    @Override
    public List<User> listByUser(User user) {
        return baseMapper.listByUser(user);
    }

    /**
     * 注册用户
     *
     * @param registerBO 注册用户参数
     * @return java.lang.Integer
     * @author lvyx
     * @since 2022/1/23 16:39
     **/
    @Override
    public Integer addUser(RegisterBO registerBO) {
        User user = new User();
        BeanUtils.copyProperties(registerBO, user);
        // -1表示自己注册
        user.setCreateUser("-1");
        user.setCreateTime(LocalDateTime.now());
        return this.baseMapper.insert(user);
    }

    /**
     * 用户修改密码
     *
     * @param oldPassword    旧密码
     * @param newPassword    新密码
     * @param repeatPassword 重复密码
     * @author lvyx
     * @since 2022/1/31 11:56
     **/
    @Override
    public void updatePassword(String oldPassword, String newPassword, String repeatPassword) {
        if (! newPassword.equals(repeatPassword)){
            throw new RuntimeException("两次输入密码不一致");
        }
        ShiroUser shiroUser = ShiroUserUtils.getShiroUser();
        if (! oldPassword.equals(shiroUser.getPassword())){
            throw new RuntimeException("原始码输入错误");
        }
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getPassword, lEncryptDecryptFactory.getEncryptDecryptService().encrypt(newPassword))
                .eq(User::getId, shiroUser.getId());
        this.baseMapper.update(null, wrapper);
    }

    /**
     * 用户绑定房间号从而绑定角色
     *
     * @param householdId 房间id
     * @author lvyx
     * @since 2022/2/8 19:14
     **/
    @Override
    public void bindRoleAndHousehold(String householdId) {
        String userId = ShiroUtils.getUserId();
        List<Role> roleList = roleService.findRoleIdByLabel(RoleTypeEnum.USER_FUNCTION.getValue(), RoleTypeEnum.USER.getValue());
        if (CollectionUtils.isNotEmpty(roleList)){
            List<String> roleIds = roleList.stream().map(Role::getId).collect(Collectors.toList());
            // 保存角色(默认为普通用户角色)关联信息
            userRoleService.bind(userId, roleIds);
        }
        // 绑定房间信息
        communityHouseholdUserService.bind(userId, householdId);
    }

    /**
     * 用户搬出房间
     *
     * @param userId 用户id
     * @author lvyx
     * @since 2022/4/26 10:02
     **/
    @Override
    public void userMoveOut(String userId) {
        // 逻辑删除用户
        this.removeById(userId);
        // 逻辑删除用户关联房间信息
        this.communityHouseholdUserService
                .remove(new LambdaQueryWrapper<CommunityHouseholdUser>()
                        .eq(CommunityHouseholdUser::getUserId, userId));
    }

    /**
     * 审批用户绑定房间信息
     *
     * @param userId            用户id
     * @param examinationStatus 审批状态
     * @author lvyx
     * @since 2022/4/26 9:36
     **/
    @Override
    public void updateExamination(String userId, String examinationStatus) throws LMailException {
        LambdaUpdateWrapper<CommunityHouseholdUser> householdUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        householdUserLambdaUpdateWrapper.eq(CommunityHouseholdUser::getUserId, userId);
        // 如果不通过，删除此条记录
        if (BooleanTypeEnum.NO.getCode().toString().equals(examinationStatus)) {
            communityHouseholdUserService.remove(householdUserLambdaUpdateWrapper);
            return;
        }
        householdUserLambdaUpdateWrapper.set(CommunityHouseholdUser::getIsEnable, BooleanTypeEnum.YES.getCode());
        communityHouseholdUserService.update(householdUserLambdaUpdateWrapper);
        String periodId = communityHouseholdUserService.findPeriodIdByUserId(userId);
        // 审批通过，激活用户信息
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId)
                .set(User::getIsEnable, BooleanTypeEnum.YES.getCode())
                .set(User::getPeriodId, periodId);
        this.update(wrapper);
        // 激活用户角色信息
        userRoleService.activeRole(userId, RoleTypeEnum.USER.getValue(), RoleTypeEnum.USER_FUNCTION.getValue());
        /*
         * 处理通过，默认你创建灰色二维码
         *  1. 防止异常，失效所有二维码
         *  2. 创建灰色二维码
         **/
        communityGreenCodeService.invalidateAll(userId);
        CommunityGreyCode communityGreyCode = new CommunityGreyCode();
        BaseEntityUtil.add(ShiroUtils.getUserId(), communityGreyCode);
        communityGreyCode.setUserId(userId);
        communityGreyCodeService.save(communityGreyCode);
        // 发送邮件提醒
        BaseSenderMessageBo baseSenderMessageBo = new BaseSenderMessageBo();
        baseSenderMessageBo.setTo(Collections.singletonList(userId));
        baseSenderMessageBo.setSubject("xx社区审批处理结果");
        User user = this.getById(userId);
        String sex = user.getSex().equals(SexTypeEnum.MALE.getCode()) ? " 先生":" 女士";
        String content = "尊敬的"+this.getById(userId).getRealName()+ sex +"，您的审批" + ExamineStatusEnum.isSuccessEnum(Integer.parseInt(examinationStatus));
        if (Objects.equals(Integer.parseInt(examinationStatus), ExamineStatusEnum.YES.getCode())){
            content += "，您的账号已经激活，请登录系统使用";
        }else {
            content += "，请登录系统重新提交审批";
        }
        baseSenderMessageBo.setContent(content);
        baseSenderMessageBo.setIsSenderEmail(BooleanTypeEnum.YES.getCode());
        communityMessageService.sendSimpleMessage(baseSenderMessageBo);
    }

    /**
     * 查询用户列表
     *
     * @param queryUserBo 查询参数
     * @param pageNum     页码
     * @param pageSize    页大小
     * @return com.github.pagehelper.PageInfo<com.lvyx.community.vo.UserHouseholdVo>
     * @author lvyx
     * @since 2022/5/3 22:39
     **/
    @Override
    public PageInfo<UserHouseholdVo> findUserList(QueryUserListBo queryUserBo, Integer pageNum, Integer pageSize) {
        if (queryUserBo.getStatus().equals(UserTypeEnum.COMMUNITY_USER.getCode())) {
            // 社区用户
            queryUserBo.setIsEnableUser(BooleanTypeEnum.YES.getCode());
            queryUserBo.setIsEnableHouseHold(BooleanTypeEnum.YES.getCode());
        }else if(queryUserBo.getStatus().equals(UserTypeEnum.SYSTEM_USER.getCode())){
            // 管理员
            queryUserBo.setIsEnableUser(BooleanTypeEnum.YES.getCode());
        }else if(queryUserBo.getStatus().equals(UserTypeEnum.OTHER_USER.getCode())){
            // 非社区用户
            queryUserBo.setIsEnableUser(BooleanTypeEnum.NO.getCode());
        }else if (queryUserBo.getStatus().equals(UserTypeEnum.SYSTEM_OTHER_USER.getCode())){
            // 管理员  非社区用户
        }
        List<String> periodIds = roleResourceService.findUserPeriodId(ShiroUtils.getUserId());
        queryUserBo.setPeriodIds(periodIds);
        // 分页插件
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<UserHouseholdVo> userHouseholdVoPageInfo = new PageInfo<>(this.baseMapper.findUserList(queryUserBo));
        List<UserHouseholdVo> userHouseholdVoList = userHouseholdVoPageInfo.getList();
        userHouseholdVoList = userHouseholdVoList.stream().peek(item -> {
            // 查询用户角色
            List<Role> roleList = roleService.findRoleByUserId(item.getUserId());
            if (CollectionUtils.isNotEmpty(roleList)){
                List<RoleVo> roleVoList = roleList.stream().map(role -> {
                    RoleVo roleVo = new RoleVo();
                    BeanUtils.copyProperties(role, roleVo);
                    return roleVo;
                }).collect(Collectors.toList());
                ShiroUser user = item.getUser();
                user.setRoleVoList(roleVoList);
                item.setUser(user);
            }
            AtomicReference<String> roleTypeName = new AtomicReference<>("非系统管理员");
            List<RoleVo> roleVoList = item.getUser().getRoleVoList();
            if (CollectionUtils.isNotEmpty(roleVoList)) {
                roleVoList.forEach(role -> {
                    if (Objects.equals(RoleTypeEnum.ADMIN.getValue(), role.getLabel()) || Objects.equals(RoleTypeEnum.COMMUNITY_ADMIN.getValue(), role.getLabel())) {
                        if (!roleTypeName.get().equals(RoleTypeEnum.ADMIN.getName())) {
                            roleTypeName.set(role.getRoleName());
                        }
                    }
                });
            }
            item.setRoleTypeName(roleTypeName.get());
        }).collect(Collectors.toList());
        userHouseholdVoPageInfo.setList(userHouseholdVoList);
        return userHouseholdVoPageInfo;
    }

    /**
     * 重置用户密码
     *
     * @param userId 用户id
     * @author lvyx
     * @since 2022/5/4 12:27
     **/
    @Override
    public void resetPassword(String userId) throws  LException {
        // 查询用户
        User user = this.getById(userId);
        if (Objects.isNull(user)) {
            throw new LException("用户不存在");
        }
        // 重置密码
        user.setPassword(systemProperties.getSystemPassword());
        // 修改用户信息
        this.updateById(user);
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     * @author lvyx
     * @since 2022/5/4 12:27
     **/
    @Override
    public void deleteUser(String userId) throws  LException {
        // 查询用户
        User user = this.getById(userId);
        if (Objects.isNull(user)) {
            throw new LException("用户不存在");
        }
        // 删除用户
        this.removeById(userId);
        // 删除用户户关联关系
        communityHouseholdUserService.deleteByUser(userId);
        // 失效二维码
        communityGreenCodeService.invalidateAll(userId);
    }

    /**
     * 分页查询用户
     *
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @param name     用户名
     * @return com.github.pagehelper.PageInfo<com.lvyx.author.entity.User>
     * @author lvyx
     * @since 2022/5/5 18:40
     **/
    @Override
    public PageInfo<User> findAllUser(Integer pageNum, Integer pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name),User::getRealName, name);
        List<User> userList = this.list(wrapper);
        return new PageInfo<>(userList);
    }
}
