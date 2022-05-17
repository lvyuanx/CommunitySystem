package com.lvyx.commons.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 加密解密配置类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23 18:55:56
 */
@Component
@ConfigurationProperties(
        prefix = "lvyx.enc-denc"
)
@Data
public class EncryptDencryptProperties {
    /**
     * 开启加密解密
     * @since 2021/12/23 18:58
     **/
    private Boolean encAndDenc = true;

    /**
     * 加密解密方式
     * @since 2021/12/23 18:58
     **/
    private String encryptType = "RSA";

    /**
     * <p>
     *     RSA加密方式，非对称式加密
     * </p>
     * @author lvyx
     * @since 2021/12/30 10:13
     **/
    @Data
    @Component
    public class RsaKey {
        /**
         * 私钥用于解密
         * @since 2021/12/30 13:40
         **/
        private String parivateKeyByBase64 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANJ2daIx8bQU6zaR6Xnjp+1eUVdTJyCwQ+64cQcb/L1q9PtYuSZEzHDUGBxYSBYx33HWMwU8LHFbB3agqv6/idcyJ/7me89amQ1zJHNDlpQi9SkA2122GWRHFIbq/2TExofnP4+YE0Yy7383ZIE+ThhMK4QskMf0T2C+4aRV2alFAgMBAAECgYEAg8gA6Ep1mdjvXwAGPXg7EfpY3GEIwwMbxkJfI6B2pPRv6WIorLmveYaUgksU+DDKRLFmyQRgn6nsBm+cIl80oxPN8Ig8xW/NFraG0qXJ528BZV2fYlMYNkGKPXQTxykti5VkQk4KUX8zs4ZaOwbJZt/Avq9yAPG0Ycf7X0gsogECQQDug6yuYhMw6fG+tn7qe381NXA3DY7aNAu1+iAnRd8aOxUSk8yLIdEVWuBzMSoGlah2L+yjbym6WcgTBBQfqrNlAkEA4eRTR1BsTrMVyw2ND5a4PrfvF/qs1OSdJ1JBC4ikgMSQFUbtCIXCnLy0KfNmlTHAWrBdxERtEcaP1eFcYrnwYQJBAJb0U2gSvl6o0c2IJw80ljAQ4DKXrB/B7N+BrLPubuNlHVMio8Qy5OeFGqUNKrH8KiCS+Ev/4QMGK7CNVkcsfFkCQQCdx62jkjkUf2Ipgj2VNeFbbX5DcYRm74gVfw6Pz9GXX7SEKBZIbaWRdP0ikDCG5UlGDwpvnJvSXq745YByNQ4BAkBXhf9hRJ4yRa6gsOnQt7UZB3E5dixlGNWOrvk0wGG9nG/saVY11LFzx0JuESBaRmhXlBH1gO2O5PzPT6OJc9Ph";
        /**
         * 公钥用于加密
         * @since 2021/12/30 13:40
         **/
        private String publicKeyByBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSdnWiMfG0FOs2kel546ftXlFXUycgsEPuuHEHG/y9avT7WLkmRMxw1BgcWEgWMd9x1jMFPCxxWwd2oKr+v4nXMif+5nvPWpkNcyRzQ5aUIvUpANtdthlkRxSG6v9kxMaH5z+PmBNGMu9/N2SBPk4YTCuELJDH9E9gvuGkVdmpRQIDAQAB";

    }

    /**
     * <p>
     *     AES加密方式，对称式加密
     * </p>
     * @author lvyx
     * @since 2021/12/30 13:39
     **/
    @Component
    @Data
    public class AesKey{
        /**
         * 加密解密
         * @since 2021/12/30 13:41
         **/
        private String key = "dsrHDJUiRxKIMe/YiEhcWQ==";
    }

}
