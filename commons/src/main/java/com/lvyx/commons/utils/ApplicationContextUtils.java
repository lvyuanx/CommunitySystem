package com.lvyx.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * <P>
 * sping上下文工具
 * </P>
 *
 * @author lvyx
 * @since 2021/12/7 11:05
 **/
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context=applicationContext;
    }

    /**
     * 根据Bean的名称获取实例对象
     * @param beanName Bean的名称
     * @return java.lang.Object
     * @author lvyx
     * @since 2021/12/7 11:06
     **/
    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    /**
     * 根据Bean的类型得到实例对象
     * @param clazz  Bean的类型
     * @return java.lang.Object
     * @author lvyx
     * @since 2021/12/10 9:40
     **/
    public static <T> T getBean(Class<T> clazz){
        return (T) context.getBean(clazz);
    }
}