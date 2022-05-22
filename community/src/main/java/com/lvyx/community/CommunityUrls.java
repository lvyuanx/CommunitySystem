package com.lvyx.community;

/**
 * <p>
 * 社区模块模块请求地址
 * </p>
 *
 * @author lvyx
 * @since 2021-12-30 13:54:57
 */
public class CommunityUrls {

    /**
     * 社区模块跟请求路径
     * @since 2021/12/30 13:56
     **/
    public final static String PACKAGE_URL = "/community";

    /**
     * 社区-期 地址控制
     * @author lvyx
     * @since 2021/12/30 13:59
     **/
    public static class PeriodCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/period";
        public static final String ADD = "/add";
        public static final String FIND = "/find";
        public static final String FIND_ALL = "/findAll";
        public static final String FIND_COMMUNITY_INFO = "/findCommunityInfo";
        public static final String FIND_ALL_COMMUNITY_INFO = "/findAllCommunityInfo";
        public static final String FIND_INFO_BY_USER = "/findInfoByUser";
        public static final String CHECK_USER_HAS_ADDRESS = "/checkUserHasAddress";
    }

    /**
     * 社区-栋 地址控制
     * @author lvyx
     * @since 2022/2/4 19:06
     **/
    public static class BuildingCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/building";
        public static final String ADD = "/add";
        public static final String FIND = "/find";
        public static final String FIND_ALL = "/findAll";
        public static final String FIND_BY_PERIOND_ID = "/findByPerionId";
    }

    /**
     * 社区-单元 地址控制
     * @author lvyx
     * @since 2022/2/4 19:06
     **/
    public static class UnitCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/unit";
        public static final String ADD = "/add";
        public static final String FIND = "/find";
        public static final String FIND_UNIT_INFO_BY_BUINDING = "/findUnitInfoByBuilding";
        public static final String FIND_UNIT_ALL_BY_BUINDING = "/findUnitAllByBuilding";
    }

    /**
     * 社区-层 地址控制
     * @author lvyx
     * @since 2022/2/4 19:06
     **/
    public static class LayerCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/layer";
        public static final String ADD = "/add";
        public static final String FIND = "/find";
    }

    /**
     * 社区-户 地址控制
     * @author lvyx
     * @since 2022/2/4 19:06
     **/
    public static class HouseholdCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/household";
        public static final String ADD = "/add";
        public static final String FIND = "/find";
    }

    /**
     * 社区-户关联 地址控制
     * @author lvyx
     * @since 2022/2/4 19:06
     **/
    public static class HouseholdUserCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/householdUser";
        public static final String FIND_USER_HOUSEHOLD = "/findUserHousehold";
        public static final String UPDATE_HOUSEHOLD_USER = "/updateHouseholdUser";
    }

    /**
     * 社区-户健康打卡 地址控制
     * @author lvyx
     * @since 2022/2/4 19:06
     **/
    public static class TemperatureRegistrationCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/temperatureRegistration";
        public static final String ADD = "/add";
        public static final String CONTINUOUS_DAY = "/continuousDay";
        public static final String FIND_LIST = "/findList";
    }

    /**
     * 社区-二维码 地址控制
     * @author lvyx
     * @since 2022/2/4 19:06
     **/
    public static class QrCoceCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/QrCode";
        public static final String GET_BY_USER = "/getByUser";
        public static final String GET_INFO_BY_USER = "/getInfoByUser";
        public static final String CHANGE_CODE = "/changeCode";
    }

    /**
     * 社区-进出管理 地址控制
     * @author lvyx
     * @since 2022/2/4 19:06
     **/
    public static class InAndOutCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/InAndOut";
        public static final String ADD = "/add";
    }


    /**
     * 社区-异常管理 地址控制
     * @author lvyx
     * @since 2022/2/4 19:06
     **/
    public static class ExceptionCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/exception";
        public static final String ADD = "/add";
        public static final String FIND_LIST_BY_QUERY = "/findListByQuery";
        public static final String UPDATE_STATUS = "/updateStatus";
    }
}
