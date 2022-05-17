package com.lvyx.commons.QrCodeFactory;

import com.lvyx.commons.config.SystemProperties;
import com.lvyx.commons.utils.ApplicationContextUtils;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * <p>
 * QrCode基类
 * </p>
 *
 * @author lvyx
 * @since 2022-04-05 21:29:11
 */
public abstract class QrCodeBase {

    protected final SystemProperties systemProperties;

    public QrCodeBase() {
        systemProperties = ApplicationContextUtils.getBean(SystemProperties.class);
    }

    /**
     * 创建二维码
     * @param resultMap  二维码携带的信息
     * @return java.awt.image.BufferedImage
     * @author lvyx
     * @since 2022/4/24 19:52
     **/
    public abstract BufferedImage createQrCode(Map<String, Object> resultMap);
}
