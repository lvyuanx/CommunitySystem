package com.lvyx.commons.utils;

import cn.hutool.core.util.ObjectUtil;
import com.lvyx.commons.cache.service.SysSimpleMapCacheService;
import com.lvyx.commons.enums.SystemCacheEnum;
import com.mysql.cj.util.StringUtils;
import javassist.NotFoundException;
import org.apache.shiro.cache.Cache;

/**
 * <p>
 * 验证码校验工具类
 * </p>
 *
 * @author lvyx
 * @since 2022-01-23 16:45:28
 */
public class CaptchaUtils {

    private static SysSimpleMapCacheService sysSimpleMapCacheService;

    static {
        sysSimpleMapCacheService = ApplicationContextUtils.getBean(SysSimpleMapCacheService.class);
    }

    /**
     * 校验验证码
     * @param purpose   用途
     * @param captchaId 验证码id
     * @param code      验证码字符
     * @return java.lang.Boolean
     * @author lvyx
     * @since 2022/1/23 16:48
     **/
    public static Boolean verifyCaptcha(String purpose, String captchaId, String code) throws Exception{
        if (StringUtils.isNullOrEmpty(code)) {
            throw new NotFoundException("验证码不能未空！");
        }else {
            String captchaKey = SystemCacheEnum.CAPTCHA.getValue() + captchaId + ":" + purpose;
            Cache<Object, Object> cache = sysSimpleMapCacheService.getCache(captchaKey);
            if (ObjectUtil.isNull(cache)) {
               throw new NotFoundException("验证码已经过期");
            }
            String realCode = (String)cache.get(captchaKey);
            String upperCaseCode = realCode.toUpperCase();
            return upperCaseCode.equals(code.toUpperCase());
        }
    }
}
