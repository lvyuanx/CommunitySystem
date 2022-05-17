package com.lvyx.commons.encrypt_decrypt;

import com.lvyx.commons.encrypt_decrypt.factory.LEncryptDecryptFactory;
import com.lvyx.commons.encrypt_decrypt.factory.service.LEncryptDecryptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 *  自定义拦截器
 *      使用@LEncryptDecrypt的字段在查询数据库时自动进行解密操作
 * </p>
 *
 * @author lvyx
 * @since 2021-12-10 15:48:37
 */

@Slf4j
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
@Component
public class LDecryptInterceptor implements Interceptor {


    @Resource
    private LEncryptDecryptFactory lEncryptDecryptFactory;

    @SuppressWarnings("all") // 关闭所有警告
    @Override
    public Object intercept(Invocation invocation) throws Exception {
        // 取出查询的结果
        Object resultObject = invocation.proceed();
        if (Objects.isNull(resultObject)){
            // 没有查询到结果直接返回空
            return null;
        }
        if (resultObject instanceof ArrayList){
            // 查询到多条结果
            ArrayList resultList = (ArrayList) resultObject;
            // 结果集不为空，且含有加密解密注解
            if (! CollectionUtils.isEmpty(resultList) && this.isDecrypt(resultList.get(0))){
                resultList.forEach(result -> {
                    // 执行解密操作
                    try {
                        decryptField(result);
                    } catch (Exception e) {
                        new RuntimeException("解密失败！", e);
                    }
                });
            }
        } else {
            // 查询到单条结果
            if (this.isDecrypt(resultObject)){
                decryptField(resultObject);
            }
        }
        return resultObject;
    }


    /**
     * 判断是否需要进行解密
     * @param obj 查询结果
     * @return java.lang.Boolean
     * @author lvyx
     * @since 2021/12/11 22:01
     **/
    private Boolean isDecrypt(Object obj){
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
            if (Objects.nonNull(annotation) && annotation.encrypt()) {
                flag.set(true);
            }
        });
        return flag.get();
    }

    /**
     * 解密属性
     * @param result 查询到的结果
     * @author lvyx
     * @since 2021/12/11 22:45
     **/
    private void decryptField(Object result) {
        // 得到结果的字节码文件
        Class<?> resultClass = result.getClass();
        // 得到所有私有属性
        Field[] fields = resultClass.getDeclaredFields();
        Arrays.asList(fields).forEach(field -> {
            // 关闭程序安全检查
            field.setAccessible(true);
            LEncryptDecrypt annotation = field.getAnnotation(LEncryptDecrypt.class);
            // 属性上携带注解进行解密
            if (Objects.nonNull(annotation)){
                String encryptData = null;
                try {
                    encryptData = (String) field.get(result);
                    // 参数的值不为null，进行解密
                    if (Objects.nonNull(encryptData)){
                        // 解密
                        LEncryptDecryptService encryptDecryptService = lEncryptDecryptFactory.getEncryptDecryptService();
                        String decryptData = encryptDecryptService.decrypt(encryptData);
                        // 解密后赋值给原字段
                        field.set(result, decryptData);
                    }
                } catch (Exception e){
                    throw new RuntimeException("解密失败！", e);
                }
            }
        });
    }
}
