package com.lvyx.commons;

/**
 * <p>
 * 公共模块请求地址
 * </p>
 *
 * @author lvyx
 * @since 2021-12-30 13:54:57
 */
public class CommonsUrls {

    /**
     * 公共模块跟请求路径
     * @since 2021/12/30 13:56
     **/
    public final static String PACKAGE_URL = "/commons";

    /**
     * 验证码请求地址控制
     * @author lvyx
     * @since 2021/12/30 13:59
     **/
    public static class CaptchaCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/captcha";
        public static final String DEFAULT_CAPTCHA = "/default";
    }

    /**
     * 文件流控制器
     * @author lvyx
     * @since 2021/12/30 13:59
     **/
    public static class FileCtrls{
        public static final String BASE_URL =  PACKAGE_URL + "/file";
        public static final String GET_IMAGE_FOR_PATH = "/getImageForPath";
        public static final String FILE_UPLOAD = "/fileUpload";
    }
}
