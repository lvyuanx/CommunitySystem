package com.lvyx.commons;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * 测试AES加密
 * </p>
 *
 * @author lvyx
 * @since 2021-12-30 10:33:51
 */
public class AESTest {

    @Test
   public void generatorTest() {
        String k = "dsrHDJUiRxKIMe/YiEhcWQ==";

        //随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        String encode = Base64.encode(key);
        System.out.println(encode);
    }

    @Test
    public void encrypt(){
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES.getValue(), Base64.decode("dsrHDJUiRxKIMe/YiEhcWQ=="));
        String helloworld = aes.encryptHex("helloworld");
        System.out.println(helloworld);
        String s = aes.decryptStr(helloworld);
        System.out.println(s);
    }
}
