package com.lvyx.commons.QrCodeFactory;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.json.JSONUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

/**
 * <p>
 * 红色健康码
 * </p>
 *
 * @author lvyx
 * @since 2022-04-05 21:32:28
 */
public class RedQrCode extends QrCodeBase {


    /**
     * 创建红色二维码
     * @param resultMap 返回参数
     * @return java.awt.image.BufferedImage
     * @author lvyx
     * @since 2022/4/5 21:37
     **/
    @Override
    public BufferedImage createQrCode(Map<String, Object> resultMap) {
        QrConfig config = new QrConfig(300, 300);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（红色）
        config.setForeColor(new Color(229, 62, 49));
        // 设置背景色（白色）
        config.setBackColor(Color.white.getRGB());
        // 设置logo，既二维码中间的图片
        config.setImg(new File(systemProperties.getLogoPath()));
        // 生成二维码到文件，也可以到流
        return QrCodeUtil.generate(JSONUtil.toJsonStr(resultMap), config);
    }

}
