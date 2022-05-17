package com.lvyx.author.shiro.utils;

import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *     读取Properties的工具类
 * </p>
 *
 * @author lvyx
 * @since 2021/12/27 14:38
 **/
@Slf4j
public class PropertiesUtil {

    public static LinkProperties propertiesShiro = new LinkProperties();


    //读取properties配置文件信息
    static {
        String sysName = System.getProperty("sys.name");
        if (StringUtils.isNullOrEmpty(sysName)) {
            sysName = "application.properties";
        } else {
            sysName += ".properties";
        }
        try {
            propertiesShiro.load(PropertiesUtil.class.getClassLoader()
                    .getResourceAsStream("authentication.properties"));
        } catch (Exception e) {
            log.warn("资源路径中不存在authentication.properties权限文件，忽略读取！");
        }
    }

    /**
     * 根据key得到value的值
     * @author lvyx
     * @since 2021/12/28 16:26
     **/
    public static String getShiroValue(String key) {
        return propertiesShiro.getProperty(key);
    }

}