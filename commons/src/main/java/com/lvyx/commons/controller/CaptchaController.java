package com.lvyx.commons.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lvyx.commons.CommonsUrls;
import com.lvyx.commons.annotation.logger.LLogger;
import com.lvyx.commons.cache.SimpleMapCache;
import com.lvyx.commons.cache.service.SysSimpleMapCacheService;
import com.lvyx.commons.enums.SystemCacheEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 验证码控制层
 * </p>
 *
 * @author lvyx
 * @since 2021-12-30 13:54:23
 */
@Api(tags = "验证码管理")
@ApiSupport(order = 1)
@Controller
@RequestMapping(CommonsUrls.CaptchaCtrls.BASE_URL)
@Slf4j
public class CaptchaController {

    @Resource
    private SysSimpleMapCacheService sysSimpleMapCacheService;

    @LLogger(description = "生成普通验证码", params = {"response", "验证码使用位置", "验证码id"})
    @ApiOperation("生成普通验证码")
    @GetMapping(CommonsUrls.CaptchaCtrls.DEFAULT_CAPTCHA)
    public void defaultCaptcha(HttpServletResponse response,
                               @RequestParam(value = "purpose") String purpose,
                               @RequestParam(value = "id") String id) throws IOException {
        // 过滤难识别的字符
        List<String> filters = Arrays.asList("0", "o", "1", "l", "s", "5", "b", "6", "9", "g");
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = null;
        while (ObjectUtil.isNull(captcha)){
            ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(220, 100, 4, 4);
            String code = shearCaptcha.getCode();
            boolean isFilter = filters.stream().anyMatch(code::contains);
            if (! isFilter){
                captcha = shearCaptcha;
            }
        }
        // 将生成的验证码放入缓存中
        String key = SystemCacheEnum.CAPTCHA.getValue() + id + ":" + purpose;
        HashMap<Object, Object> cacheMap = new HashMap<>();
        cacheMap.put(key, captcha.getCode());
        Cache<Object, Object> cache = sysSimpleMapCacheService.getCache(key);
        if (ObjectUtil.isNotNull(cache)){
            // 更新验证码缓存
            sysSimpleMapCacheService.updateCahce(key, new SimpleMapCache(key, cacheMap),300L);
        }else {
            // 创建验证码缓存
            sysSimpleMapCacheService.createCache(key, new SimpleMapCache(key, cacheMap),300L);
        }
        try(ServletOutputStream outputStream = response.getOutputStream();) {
            captcha.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new IOException("验证码生成失败！");
        }
    }

}
