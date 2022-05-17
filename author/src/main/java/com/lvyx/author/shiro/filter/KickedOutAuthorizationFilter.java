package com.lvyx.author.shiro.filter;

import cn.hutool.core.util.ObjectUtil;
import com.lvyx.author.shiro.config.ShiroProperties;
import com.lvyx.author.shiro.utils.ShiroUserUtils;
import com.lvyx.commons.enums.ShiroCacheEnum;
import com.lvyx.commons.utils.ApplicationContextUtils;
import com.lvyx.commons.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>
 *  在线并发人数控制器
 * </p>
 *
 * @author lvyx
 * @since 2022-01-13 10:44:24
 */
@Slf4j
public class KickedOutAuthorizationFilter extends AccessControlFilter {

    @Resource
    private ShiroProperties shiroProperties;

    private RedissonClient redissonClient;

    private SessionDAO redisSessionDao;

    private DefaultWebSessionManager sessionManager;

    public KickedOutAuthorizationFilter(RedissonClient redissonClient, SessionDAO redisSessionDao, DefaultWebSessionManager sessionManager) {
        this.redissonClient = redissonClient;
        this.redisSessionDao = redisSessionDao;
        this.sessionManager = sessionManager;
    }

    /**
     * 是否允许访问，返回true表示允许
     * @param servletRequest request
     * @param servletResponse  response
     * @param o 请求信息
     * @return boolean
     * @author lvyx
     * @since 2022/1/13 15:36
     **/
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 未登录的用户直接放行登录
        if (! ShiroUtils.isAuthenticated()){
            return true;
        }
        // 得到当前登录用户的SessionId 和 用户名
        String sessionId = ShiroUtils.getSessionId();
        String username = ShiroUserUtils.getShiroUser().getLoginName();
        // 使用Redis客户得到
        RDeque<Object> deque = redissonClient.getDeque(ShiroCacheEnum.USER_QUEUE.getValue() + username);
        // 判断当前用户的SessionId是否已经在队列中
        boolean flag = deque.contains(sessionId);
        if (!flag){
            // 不存在就加入到队尾
            deque.addLast(sessionId);
        }
        if (ObjectUtil.isEmpty(shiroProperties)){
            shiroProperties = ApplicationContextUtils.getBean(ShiroProperties.class);
        }
        // 判断该队列是否超过了最大在线人数
        if (deque.size() > shiroProperties.getOnline()){
            // 超过从队列的头部拿掉SessionId
            sessionId = (String) deque.getFirst();
            deque.removeFirst();
            Session session = null;
            try {
                session = sessionManager.getSession(new DefaultSessionKey(sessionId));
            } catch (UnknownSessionException e){
                log.info("session已经失效了");
            } catch (ExpiredSessionException e) {
                log.info("session已经过期了！");
            }
            // 如果没有过期就要删除队首的用户
            if (ObjectUtil.isNotEmpty(session)){
                redisSessionDao.delete(session);
            }
        }
        // 没有超过直接放行
        return true;
    }
}
