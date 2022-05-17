package com.lvyx.commons.encrypt_decrypt.factory.service.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.lvyx.commons.config.EncryptDencryptProperties;
import com.lvyx.commons.encrypt_decrypt.factory.service.LEncryptDecryptService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * RSA加密解密
 * </p>
 *
 * @author lvyx
 * @since 2021-12-10 13:26:42
 */
@Component("RSA")
public class RSAEncryptDecryptServiceImpl implements LEncryptDecryptService {

    @Resource
    private EncryptDencryptProperties.RsaKey rsaKey;


    /**
     * 加密
     * @param content 被加密字符串
     * @return java.lang.String 加密后字符串
     * @author lvyx
     * @since 2021/12/10 13:24
     **/
    @Override
    public String encrypt(String content) {
        // 使用公钥加密
        RSA rsa = new RSA(null, rsaKey.getPublicKeyByBase64());
        return rsa.encryptBase64(content, StandardCharsets.UTF_8, KeyType.PublicKey);
    }

    /**
     * 解密
     * @param content 加密的字符串
     * @return java.lang.String 原字符串
     * @author lvyx
     * @since 2021/12/10 13:25
     **/
    @Override
    public String decrypt(String content) {
        // 使用私钥解密
        RSA rsa = new RSA(rsaKey.getParivateKeyByBase64(), null);
        return rsa.decryptStr(content, KeyType.PrivateKey);
    }
}
