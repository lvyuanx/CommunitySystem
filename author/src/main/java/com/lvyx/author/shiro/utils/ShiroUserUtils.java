package com.lvyx.author.shiro.utils;

import cn.hutool.core.util.ObjectUtil;
import com.lvyx.commons.pojo.ShiroUser;
import com.lvyx.commons.utils.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.ThreadContext;

/**
 * <p>
 * shiro用户工具类
 * </p>
 *
 * @author lvyx
 * @since 2022-01-13 13:53:46
 */
public class ShiroUserUtils extends ShiroUtils {

    /**
     * 得到shiroUser对象
     * @return com.lvyx.author.shiro.pojo.ShiroUser
     * @author lvyx
     * @since 2022/1/13 13:55
     **/
    public static ShiroUser getShiroUser(){
        if (ObjectUtil.isNotEmpty(ThreadContext.getSubject()) && ObjectUtil.isNotEmpty(ThreadContext.getSubject().getPrincipal())){
            return (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        }
        return null;
    }

}
