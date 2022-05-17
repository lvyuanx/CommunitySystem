package com.lvyx.author.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.lvyx.author.bo.QueryUserListBo;
import com.lvyx.author.bo.RegisterBO;
import com.lvyx.author.entity.User;
import com.lvyx.commons.exception.LException;
import com.lvyx.commons.exception.LMailException;
import com.lvyx.community.vo.UserHouseholdVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户信息查询用户
     * @param user 用户实体
     * @return java.util.List<com.lvyx.author.entity.User>
     * @author lvyx
     * @since 2022/1/23 16:38
     **/
    List<User> listByUser(User user);

    /**
     * 注册用户
     * @param registerBO 注册用户参数
     * @return java.lang.Integer
     * @author lvyx
     * @since 2022/1/23 16:39
     **/
    Integer addUser(RegisterBO registerBO);

    /**
     * 用户修改密码
     * @param oldPassword       旧密码
     * @param newPassword       新密码
     * @param repeatPassword    重复密码
     * @author lvyx
     * @since 2022/1/31 11:56
     **/
    void updatePassword(String oldPassword, String newPassword, String repeatPassword);
    
    /**
     * 用户绑定房间号从而绑定角色
     * @param householdId 房间id
     * @author lvyx
     * @since 2022/2/8 19:14
     **/
    @Transactional(rollbackFor = Exception.class)
    void bindRoleAndHousehold(String householdId);

    /**
     * 用户搬出房间
     * @param userId 用户id
     * @author lvyx
     * @since 2022/4/26 10:02
     **/
    void userMoveOut(String userId);

    /**
     * 审批用户绑定房间信息
     * @param userId            用户id
     * @param examinationStatus 审批状态
     * @author lvyx
     * @since 2022/4/26 9:36
     **/
    @Transactional(rollbackFor = Exception.class)
    void updateExamination(String userId, String examinationStatus) throws LMailException;


    /**
     * 查询用户列表
     * @param queryUserBo 查询参数
     * @param pageNum     页码
     * @param pageSize    页大小
     * @return com.github.pagehelper.PageInfo<com.lvyx.community.vo.UserHouseholdVo>
     * @author lvyx
     * @since 2022/5/3 22:39
     **/
    PageInfo<UserHouseholdVo> findUserList(QueryUserListBo queryUserBo, Integer pageNum, Integer pageSize);

    /**
     * 重置用户密码
     * @param userId 用户id
     * @author lvyx
     * @since 2022/5/4 12:27
     **/
    void resetPassword(String userId) throws  LException;

    /**
     * 删除用户
     * @param userId 用户id
     * @author lvyx
     * @since 2022/5/4 12:27
     **/
    void deleteUser(String userId) throws LException;

    /**
     * 分页查询用户
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @param name  用户名
     * @return com.github.pagehelper.PageInfo<com.lvyx.author.entity.User>
     * @author lvyx
     * @since 2022/5/5 18:40
     **/
    PageInfo<User> findAllUser(Integer pageNum, Integer pageSize, String name);

}
