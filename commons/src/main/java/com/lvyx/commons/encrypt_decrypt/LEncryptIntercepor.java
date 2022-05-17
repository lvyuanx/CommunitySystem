package com.lvyx.commons.encrypt_decrypt;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lvyx.commons.encrypt_decrypt.factory.LEncryptDecryptFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 *  自定义拦截器
 *      结合字段上的@LEncryptDecrypt在入参时自动加密
 * </p>
 *
 * @author lvyx
 * @since 2021-12-12 19:00:38
 */
@Slf4j
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class})
})
@Component
public class LEncryptIntercepor implements Interceptor {

    @Resource
    private LEncryptDecryptFactory lEncryptDecryptFactory;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // @Signature中指定了类型，则getTarget()类型为parameterHandler
       if (invocation.getTarget() instanceof  ParameterHandler){
           ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
           // 得到入参
           Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
           // 关闭安全性检查
           parameterField.setAccessible(true);
           // 得到具体的参数对象
           Object parameterObject = parameterField.get(parameterHandler);
           // 判断是否需要进行加密
           if (Objects.nonNull(parameterObject) && this.isEncrypt(parameterObject)){
               // 加密
                this.encrypt(parameterObject);
           }
       }

        // 执行
        return invocation.proceed();
    }


    /**
     * 判断是否需要进行加密
     * @param obj 查询参数
     * @return java.lang.Boolean
     * @author lvyx
     * @since 2021/12/12 19:24
     **/
    private Boolean isEncrypt(Object obj){
        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        if (Objects.isNull(obj)){
            return flag.get();
        }
        // 通过反射得到字节码文件
        Class<?> objClass = obj.getClass();
        // 判断是否含有加密注解
        Field[] fields = objClass.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            LEncryptDecrypt annotation = field.getAnnotation(LEncryptDecrypt.class);
            if (Objects.nonNull(annotation) && annotation.dencrypt()) {
                flag.set(true);
            }
        });
        return flag.get();
    }

    /**
     * 加密属性
     * @param obj 入参
     * @author lvyx
     * @since 2021/12/12 19:40
     **/
    private void encrypt(Object obj){
        Class<?> objClass = obj.getClass();
        // 得到所有的参数
        Field[] fields = obj.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            // 关闭程序安全检查
            field.setAccessible(true);
            // 判断是否带了加密注解
            LEncryptDecrypt annotation = field.getAnnotation(LEncryptDecrypt.class);
            // 携带了加密注解进行加密
            if (Objects.nonNull(annotation) && annotation.dencrypt()){
                // 原始数据
                String originData = null;
                try {
                    originData = (String) field.get(obj);
                    // 进行加密
                    if (StringUtils.isNotEmpty(originData)){
                        String encryptData = lEncryptDecryptFactory.getEncryptDecryptService().encrypt(originData);
                        // 赋值给原字段
                        field.set(obj, encryptData);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("加密失败！", e);
                }
            }
        });
    }

}
