package com.lvyx.commons.utils;

import cn.hutool.core.util.ObjectUtil;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Base64;

/**
 * <p>
 *  自定义序列化工具类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-31 09:26:42
 */
@Slf4j
public class LSerializeUtils {

    /**
     * 反序列化方法
     * @param str 序列化的字符串
     * @return java.lang.Object
     * @author lvyx
     * @since 2021/12/31 9:27
     **/
    public static Object deserialize(String str) {
        if (StringUtils.isNullOrEmpty(str)) {
            return null;
        }
        Object object=null;
        try(ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(str));
            ObjectInputStream ois = new ObjectInputStream(bis);) {
            object = ois.readObject();
        } catch (IOException |ClassNotFoundException e) {
            log.error("流读取异常",e);
        }
        return object;
    }

    /**
     * 序列化方法
     * @param obj 被序列化的对象
     * @return java.lang.String
     * @author lvyx
     * @since 2021/12/31 9:32
     **/
    public static String serialize(Object obj) {
        if (ObjectUtil.isEmpty(obj)) {
            return null;
        }
        String base64String = null;
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);) {
            oos.writeObject(obj);
            base64String = Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (IOException e) {
            log.error("流写入异常",e);
        }
        return base64String;
    }
}
