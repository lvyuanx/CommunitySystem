package com.lvyx.commons.QrCodeFactory;

import com.lvyx.commons.enums.QrCodeColorEnum;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * <p>
 * 二维码工厂类
 * </p>
 *
 * @author lvyx
 * @since 2022-04-05 21:41:17
 */
public class QrCodeFactory {

    /**
     * 构建二维码
     * @param qrCodeColorEnum 二维码类型
     * @param resultMap  携带参数
     * @return java.awt.image.BufferedImage
     * @author lvyx
     * @since 2022/4/5 21:54
     **/
    public static BufferedImage getQrCode(QrCodeColorEnum qrCodeColorEnum, Map<String, Object> resultMap){
        QrCodeBase qrCodeBase = null;
        if (qrCodeColorEnum.getValue().equals(QrCodeColorEnum.GREEN.getValue())){
            qrCodeBase = new GreenQrCode();
        }else if (qrCodeColorEnum.getValue().equals(QrCodeColorEnum.RED.getValue())){
            qrCodeBase = new RedQrCode();
        }else if (qrCodeColorEnum.getValue().equals(QrCodeColorEnum.YELLOW.getValue())){
            qrCodeBase = new YellowQrCode();
        }else if (qrCodeColorEnum.getValue().equals(QrCodeColorEnum.GRAY.getValue())){
            qrCodeBase = new GrayQrCode();
        }
        return qrCodeBase.createQrCode(resultMap);
    }

}
