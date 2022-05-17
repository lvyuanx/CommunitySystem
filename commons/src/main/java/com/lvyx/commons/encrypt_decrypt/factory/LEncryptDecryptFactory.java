package com.lvyx.commons.encrypt_decrypt.factory;

import com.lvyx.commons.config.EncryptDencryptProperties;
import com.lvyx.commons.encrypt_decrypt.factory.service.LEncryptDecryptService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 自定义加密解密工厂
 *      目前系统支持的对称加密有：AES
 *      目前系统支持的非对称加密有：RSA
 * </p>
 *
 * @author lvyx
 * @since 2021-12-10 13:17:34
 */
@Component
public class LEncryptDecryptFactory {

    @Resource
    private EncryptDencryptProperties encryptDencryptProperties;

    @Resource
    private Map<String, LEncryptDecryptService> strategys = new ConcurrentHashMap(2);

    public LEncryptDecryptFactory() {
    }

    /**
     * 得到自定义加密解密服务
     * @return com.lvyx.shiro_boot02.annotation.encrypt_decrype.factory.service.LEncryptDecryptService
     * @author lvyx
     * @since 2021/12/10 13:43
     **/
    public LEncryptDecryptService getEncryptDecryptService(){
        String strategy = encryptDencryptProperties.getEncryptType();
        LEncryptDecryptService lEncryptDecryptService = this.strategys.get(strategy);
        if (lEncryptDecryptService == null){
            throw new RuntimeException("加密方式不存在！");
        }
        return lEncryptDecryptService;
    }
}
