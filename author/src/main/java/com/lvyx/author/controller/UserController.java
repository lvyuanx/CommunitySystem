package com.lvyx.author.controller;


import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.author.AuthorUrls;
import com.lvyx.author.bo.LoginBO;
import com.lvyx.author.bo.QueryUserListBo;
import com.lvyx.author.bo.RegisterBO;
import com.lvyx.author.entity.User;
import com.lvyx.author.service.UserService;
import com.lvyx.author.shiro.core.impl.JwtTokenManager;
import com.lvyx.author.shiro.pojo.SimpleToken;
import com.lvyx.author.shiro.utils.ShiroUserUtils;
import com.lvyx.author.vo.LoginInfoVO;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.cache.service.SysSimpleMapCacheService;
import com.lvyx.commons.config.SystemProperties;
import com.lvyx.commons.encrypt_decrypt.factory.LEncryptDecryptFactory;
import com.lvyx.commons.enums.CaptchaNameEnum;
import com.lvyx.commons.enums.ResultCodeEnum;
import com.lvyx.commons.enums.ShiroEnum;
import com.lvyx.commons.enums.SystemCacheEnum;
import com.lvyx.commons.exception.LException;
import com.lvyx.commons.pojo.ShiroUser;
import com.lvyx.commons.result.Result;
import com.lvyx.commons.result.impl.ErrorResult;
import com.lvyx.commons.result.impl.SuccessResult;
import com.lvyx.commons.utils.CaptchaUtils;
import com.lvyx.community.vo.UserHouseholdVo;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23
 */
@Api(tags = "用户管理")
@ApiSupport(order = 1)
@RestController
@RequestMapping(AuthorUrls.UserCtrl.BASE_URL)
@Slf4j
public class UserController {

    @Resource
    private LEncryptDecryptFactory lEncryptDecryptFactory;

    @Resource
    private JwtTokenManager jwtTokenManager;

    @Resource
    private SysSimpleMapCacheService sysSimpleMapCacheService;

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private UserService userService;

    @LLogger(description = "登录接口", params = {"用户登录参数"})
    @ApiOperation(value = "登录接口")
    @ApiOperationSupport(order = 1)
    @PostMapping(AuthorUrls.UserCtrl.LOGIN)
    public Result<LoginInfoVO> login(@RequestBody LoginBO loginBo){
        if (StringUtils.isNullOrEmpty(loginBo.getUsername()) ||
                StringUtils.isNullOrEmpty(loginBo.getPassword())){
            return new ErrorResult<>("用户名或者密码不能为");
        }
        // 校验验证码
        if (! systemProperties.getActive().equals("test") || ! loginBo.getCaptcha().equals(systemProperties.getTestCaptchaCode())){
            if (StringUtils.isNullOrEmpty(loginBo.getCaptcha())) {
                return new ErrorResult<>("验证码不能为空");
            }else {
                String captchaKey = SystemCacheEnum.CAPTCHA.getValue() + loginBo.getCaptchaId() + CaptchaNameEnum.LOGIN_CAPTCHA.getValue();
                Cache<Object, Object> cache = sysSimpleMapCacheService.getCache(captchaKey);
                if (ObjectUtil.isNull(cache)) {
                    return new ErrorResult<>("验证码已经过期");
                }
                String code = (String)cache.get(captchaKey);
                String upperCaseCode = code.toUpperCase();
                if (! upperCaseCode.equals(loginBo.getCaptcha().toUpperCase())) {
                    return new ErrorResult<>("验证码错误");
                }
            }
        }
        loginBo.setSystemCode(ShiroEnum.PLATFORM_MGT.getValue());
        Subject subject = SecurityUtils.getSubject();
        // 如果选择记住我需要解密
        if (loginBo.getRememberFlag()){
            String decryptPwd = lEncryptDecryptFactory.getEncryptDecryptService().decrypt(loginBo.getPassword());
            loginBo.setPassword(decryptPwd);
        }
        // 创建登录的token
        SimpleToken simpleToken = new SimpleToken(null, loginBo.getUsername(), loginBo.getPassword());
        subject.login(simpleToken);
        // 登录成功后颁发令牌
        String sessionId = ShiroUserUtils.getSessionId();
        ShiroUser shiroUser = ShiroUserUtils.getShiroUser();
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("shiroUser", shiroUser);
        String jwtToken = jwtTokenManager.issuedToken(shiroUser.getLoginName(), subject.getSession().getTimeout(), sessionId, claims);
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        // 前端不可见明文密码
        shiroUser.setPassword(lEncryptDecryptFactory.getEncryptDecryptService().encrypt(shiroUser.getPassword()));
        loginInfoVO.setShiroUser(shiroUser);
        loginInfoVO.setToken(jwtToken);
        return new SuccessResult<>("登录成功", loginInfoVO);
    }


    @LLogger(description = "退出登录")
    @ApiOperation(value = "退出登录")
    @ApiOperationSupport(order = 2)
    @GetMapping(AuthorUrls.UserCtrl.LOGOUT)
    public Result<String> logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
            return new SuccessResult<>("退出成功");
        }else {
            return new ErrorResult<>(ResultCodeEnum.NO_LOGIN.getCode(), "请先登录");
        }
    }

    @LLogger(description = "注册重复性查询", params = "用户实体")
    @ApiOperation(value = "注册重复性查询")
    @ApiOperationSupport(order = 3)
    @PostMapping(AuthorUrls.UserCtrl.NAME_IS_REPEAT)
    public Result<String> nameIsRepeat(@RequestBody User user){
        String result = "未重复!";
        List<User> users = userService.listByUser(user);
        if (! CollectionUtils.isEmpty(users)){
            result = "重复!";
        }
        return new SuccessResult<>(result);
    }

    @LLogger(description = "注册用户", params = "用户实体")
    @ApiOperation(value = "注册用户")
    @ApiOperationSupport(order = 4)
    @PostMapping(AuthorUrls.UserCtrl.INSERT_USER)
    public Result<String> insertUser(@RequestBody RegisterBO user) throws Exception {
        // 校验重复输入的密码
        if (! user.getPassword().equals(user.getConfirmPassword())){
            return new ErrorResult<>("两次输入的密码不一致");
        }
        // 校验验证码
        Boolean verifyCaptcha = CaptchaUtils.verifyCaptcha(user.getPurpose(),
                user.getCaptchaId(),
                user.getCaptcha());
        if (! verifyCaptcha){
            return new ErrorResult<>("验证码错误，请重新输入");
        }
        int insertRow = userService.addUser(user);
        if (insertRow < 1){
            return new SuccessResult<>("注册失败");
        }
        return new SuccessResult<>("注册成功");
    }

    @LLogger(description = "查询当前登录人信息")
    @ApiOperation(value = "查询当前登录人信息")
    @ApiOperationSupport(order = 5)
    @GetMapping(AuthorUrls.UserCtrl.LOGIN_INFO)
    public Result<ShiroUser> loginInfo(){
        ShiroUser shiroUser = ShiroUserUtils.getShiroUser();
        // 加密密码
        shiroUser.setPassword(lEncryptDecryptFactory.getEncryptDecryptService().encrypt(shiroUser.getPassword()));
        return new SuccessResult<>(shiroUser);
    }

    @LLogger(description = "密码修改", params = {"旧密码", "新密码", "重复密码"})
    @ApiOperation(value = "密码修改")
    @ApiOperationSupport(order = 6)
    @PostMapping(AuthorUrls.UserCtrl.UPDATE_PASSWORD)
    public Result<String> updatePassword(String oldPassword, String newPassword, String repeatPassword){
        try {
            userService.updatePassword(oldPassword, newPassword, repeatPassword);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("密码修改失败");
        }
        // 如果美誉抛出异常，则修改成功
        return new SuccessResult<>("密码修改成功");
    }

    @LLogger(description = "修改用户信息", params = {"用户实体"})
    @ApiOperation(value = "修改用户信息")
    @ApiOperationSupport(order = 7)
    @PostMapping(AuthorUrls.UserCtrl.UPDATE_USER_INFO)
    public Result<String> updateUsereInfo(@RequestBody User user){
        try {
            // 解密密码，存入数据库时会自动加密
            user.setPassword(lEncryptDecryptFactory
                    .getEncryptDecryptService().decrypt(user.getPassword()));
            userService.updateById(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("用户信息修改失败");
        }
        // 如果美誉抛出异常，则修改成功
        return new SuccessResult<>("修改用户信息成功");
    }

    @LLogger(description = "用户绑定角色房间", params = {"房间id"})
    @ApiOperation(value = "用户绑定角色房间")
    @ApiOperationSupport(order = 8)
    @PostMapping(AuthorUrls.UserCtrl.BIND_ROLE_AND_HOUSEHOLD)
    public Result<String> bindRole(@RequestParam String householdId){
        try {
          userService.bindRoleAndHousehold(householdId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("用户绑定角色房间失败");
        }
        return new SuccessResult<>("绑定成功");
    }

    @LLogger(description = "用户搬出", params = {"用户搬出"})
    @ApiOperation("用户搬出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id")
    })
    @ApiOperationSupport(order = 9)
    @GetMapping(AuthorUrls.UserCtrl.USER_MOVE_OUT)
    public Result<String> userMoveOut(@RequestParam(value = "userId") String userId){
        try {
            userService.userMoveOut(userId);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("用户搬出异常");
        }
        return new SuccessResult<>("用户搬出成功");
    }

    @LLogger(description = "处理用户审批信息", params = {"用户id", "审批状态"})
    @ApiOperation("处理用户审批信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id"),
            @ApiImplicitParam(name = "examineStatus", value = "审批状态(1，审批通过， 0，审批不通过)"),
    })
    @ApiOperationSupport(order = 10)
    @GetMapping(AuthorUrls.UserCtrl.HOUSEHOLD_EXAMINATION)
    public Result<String> householdExamination(@RequestParam(value = "userId") String userId,
                                               @RequestParam(value = "examineStatus") String examineStatus){
        try {
            userService.updateExamination(userId, examineStatus);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("用户审批异常");
        }
        return new SuccessResult<>("用户审批处理成功");
    }

    @LLogger(description = "查询用户列表")
    @ApiOperation("查询用户列表")
    @ApiOperationSupport(order = 11)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sizeSize", value = "每页数量", defaultValue = "10", dataType = "int", paramType = "query"),
    })
    @PostMapping(AuthorUrls.UserCtrl.FIND_USER_LIST)
    public Result<PageInfo<UserHouseholdVo>> findUserList(@RequestBody QueryUserListBo queryUserBo,
                                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum){
        PageInfo<UserHouseholdVo> userHouseholdVoPageInfo = new PageInfo<>();
        try {
            userHouseholdVoPageInfo = userService.findUserList(queryUserBo, pageNum, pageSize);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ErrorResult<>("查询用户列表异常");
        }
        return new SuccessResult<>("查询用户列表成功", userHouseholdVoPageInfo);
    }

    @LLogger(description = "重置密码", params = {"用户id"})
    @ApiOperation(value = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id")
    })
    @ApiOperationSupport(order = 12)
    @GetMapping(AuthorUrls.UserCtrl.RESET_PASSWORD)
    public Result<String> resetPassword(String userId){
        try {
            userService.resetPassword(userId);
        } catch (LException le){
            log.error(le.getMessage(), le);
            return new ErrorResult<>(le.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("重置密码失败");
        }
        return new SuccessResult<>("重置密码成功");
    }

    @LLogger(description = "删除用户", params = {"用户id"})
    @ApiOperation(value = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id")
    })
    @ApiOperationSupport(order = 13)
    @GetMapping(AuthorUrls.UserCtrl.DELETE_USER)
    public Result<String> deleteUser(String userId){
        try {
            userService.deleteUser(userId);
        } catch (LException le){
            log.error(le.getMessage(), le);
            return new ErrorResult<>(le.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("删除用户失败");
        }
        return new SuccessResult<>("删除用户成功");
    }

    @LLogger(description = "查询所有用户", params = {"用户id"})
    @ApiOperation(value = "查询所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", defaultValue = "1", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sizeSize", value = "每页数量", defaultValue = "10", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sizeSize", value = "每页数量", defaultValue = "10", dataType = "int", paramType = "query"),
    })
    @ApiOperationSupport(order = 14)
    @GetMapping(AuthorUrls.UserCtrl.FIND_ALL_USER)
    public Result<PageInfo<User>> findAllUser(@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "name", required = false) String name){
        PageInfo<User> userPageInfo = new PageInfo<>();
        try {
            userPageInfo = userService.findAllUser(pageNum, pageSize, name);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("查询所有用户失败");
        }
        return new SuccessResult<>("查询所有用户成功", userPageInfo);
    }





    @LLogger(description = "测试接口-密码加密")
    @ApiOperation(value = "测试接口-密码加密")
    @ApiOperationSupport(order = 98)
    @GetMapping(AuthorUrls.UserCtrl.TEST)
    public Result<String> test(String pwd){
        return new SuccessResult<>(lEncryptDecryptFactory.getEncryptDecryptService().encrypt(pwd));
    }

    @LLogger(description = "测试接口-密码解密")
    @ApiOperation(value = "测试接口-密码解密")
    @ApiOperationSupport(order = 99)
    @GetMapping(AuthorUrls.UserCtrl.TEST2)
    public Result<String> test2(String pwd){
        return new SuccessResult<>(lEncryptDecryptFactory.getEncryptDecryptService().decrypt(pwd));
    }



}
