package com.lvyx.author;

/**
 * <p>
 * 用户管理请求地址
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23 12:38:07
 */
public class AuthorUrls {

    /**
     * 用户管理模块根路径
     * @since 2021/12/23 12:39
     **/
    public final static String PACKAGE_URL = "/author";

    /**
     * 用户相关请求
     * @author lvyx
     * @since 2021/12/23 12:43
     **/
    public static class UserCtrl {
        public static final String BASE_URL =  PACKAGE_URL + "/user";
        public static final String TEST = "/test";
        public static final String TEST2 = "/test2";
        public static final String LOGIN = "/login";
        public static final String LOGOUT = "/logout";
        public static final String NAME_IS_REPEAT = "/nameIsRepeat";
        public static final String INSERT_USER = "/insertUser";
        public static final String LOGIN_INFO = "/loginInfo";
        public static final String UPDATE_PASSWORD = "/updatePassword";
        public static final String UPDATE_USER_INFO = "/updateUserInfo";
        public static final String BIND_ROLE_AND_HOUSEHOLD = "/bindRoleAndHousehold";
        public static final String USER_MOVE_OUT = "/userMoveOut";
        public static final String FIND_USER_LIST = "/findUserList";
        public static final String HOUSEHOLD_EXAMINATION = "/householdExamination";
        public static final String RESET_PASSWORD = "/resetPassword";
        public static final String DELETE_USER = "/deleteUser";
        public static final String FIND_ALL_USER = "/findAllUser";
    }

    /**
     * 角色相关请求
     * @author lvyx
     * @since 2021/12/23 12:43
     **/
    public static class RoleCtrl {
        public static final String BASE_URL =  PACKAGE_URL + "/Role";
        public static final String ADD_COMMUNITY_ROLE =  "/addCommunityRole";
        public static final String FIND_COMMUNITY_ROLE =  "/findCommunityRole";
        public static final String FIND_USER_FUNCTION =  "/findUserFunction";
        public static final String DELETE_ROLE =  "/deleteRole";
        public static final String EDIT_USER_ROLE =  "/editUserRole";
        public static final String FIND_COMMUNITY_ROLE_RESOURCE_NAME =  "/findCommunityRoleResource";
    }

}
