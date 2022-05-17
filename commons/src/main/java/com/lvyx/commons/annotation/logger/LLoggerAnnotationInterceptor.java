package com.lvyx.commons.annotation.logger;

import cn.hutool.core.io.file.FileWriter;
import com.lvyx.commons.annotation.entity.Log;
import com.lvyx.commons.annotation.service.LogService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 自定义日志注解切面
 * </p>
 *
 * @author lvyx
 * @since 2021-12-02 13:53:34
 */
@Aspect
@Component
public class LLoggerAnnotationInterceptor {

    @Resource
    private LogService lLogService;

    /**
     * 异常信息
     * @since 2021/12/29 9:15
     **/
    private Exception e;

    /**
     * 切点
     * @author lvyx
     * @since 2021/12/4 12:06
     **/
    @Pointcut(value = "@annotation(com.lvyx.commons.annotation.logger.LLogger)")
    private void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 得到目标类的HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 得到log信息
        Log log = getLog(request, pjp);
        // 记录方法开始时间
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = null;
        // 执行目标方法发
        Object proceed = null;
        try {
            // 异常交给全局异常处理
            proceed = pjp.proceed();
        } catch (Exception e){
            log.setErrorMessage(e.getMessage());
            throw e;
        } finally {
            // 记录方法结束时间
            end = LocalDateTime.now();
            // 记录方法运行时间
            log.setRunTime(Duration.between(start, end).toMillis());
            // 记录方法返回值
            if (proceed != null){
                log.setMethodReturn(proceed.toString());
            }
            // 日志创建时间
            log.setCreateTime(LocalDateTime.now());
            // 格式化控制台输出信息
            StringBuilder stringBuilder = formateLogOut(log);
            System.out.println(stringBuilder);
            // 保存日志信息到数据库中
            lLogService.save(log);
            // 保存错误信息到日志文件中
            if (! ObjectUtils.isEmpty(log.getErrorMessage())){
                try {
                    saveError(stringBuilder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return proceed;
    }

    /**
     * 格式化控制台输出信息
     * @param log 日志信息
     * @return StringBuilder
     * @author lvyx
     * @since 2021/12/4 21:04
     **/
    private StringBuilder  formateLogOut(Log log){
        String thisClassName = this.getClass().getName();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        // 控制台绿色输出
        String outGreenHeader = new String("\033[32m").intern();
        // 控制台紫色输出
        String outPurpleHeader = new String("\033[35m").intern();
        // 控制台红色输出
        String outRadHeader = new String("\033[31m").intern();
        // 控制台结束颜色控制
        String outEnd = new String("\033[0m").intern();
        // 箭头
        String arrow = new String("======>").intern();
        sb.append(outGreenHeader).append(thisClassName).append("\t").append(LocalDateTime.now().format(df)).append("\t******\t自定义日志 start\t↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓").append(outEnd).append("\n")
                .append(outPurpleHeader).append(arrow).append(outEnd).append(" 方法注释 : ").append(log.getMethodDoc()).append("\n")
                .append(outPurpleHeader).append(arrow).append(outEnd).append(" IP : ").append(log.getIp()).append("\n")
                .append(outPurpleHeader).append(arrow).append(outEnd).append(" URL : ").append(log.getUrl()).append("\n")
                .append(outPurpleHeader).append(arrow).append(outEnd).append(" 请求类型 : ").append(log.getRequestType()).append("\n")
                .append(outPurpleHeader).append(arrow).append(outEnd).append(" 类路径 : ").append(log.getClassPath()).append("\n")
                .append(outPurpleHeader).append(arrow).append(outEnd).append(" 方法名称 : ").append(log.getMethodName()).append("\n")
                .append(outPurpleHeader).append(arrow).append(outEnd).append(" 方法参数 : ").append(ObjectUtils.isEmpty(log.getMethodParam()) ? "null":log.getMethodParam()).append("\n");
        if (ObjectUtils.isEmpty(log.getErrorMessage())){
            // 方法执成功
            sb.append(outPurpleHeader).append(arrow).append(outEnd).append(" 返回值 : ").append(log.getMethodReturn()).append("\n")
                    .append(outRadHeader).append("方法耗时 : ").append(log.getRunTime()).append(" ms").append(outEnd).append("\n");
        } else {
            //方法执行失败
            sb.append(outRadHeader).append("**********方法异常信息**********").append(outEnd).append("\n")
             .append(log.getErrorMessage()).append("\n");
        }
        sb.append(outGreenHeader).append(thisClassName).append("\t").append(LocalDateTime.now().format(df)).append("\t******\t自定义日志 end\t↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑").append(outEnd).append("\n");
        return sb;
    }

    /**
     * 通过request得到日志信息
     * @param request request
     * @author lvyx
     * @since 2021/12/3 17:17
     **/
    private Log getLog(HttpServletRequest request, ProceedingJoinPoint pjp){
        Log log = new Log();
        log.setId(UUID.randomUUID().toString());
        // 得到ip
        log.setIp(getIPAddress(request));
        // 得到请求的URL
        log.setUrl(request.getRequestURI());
        // 得到请求的类型
        log.setRequestType(request.getMethod());
        // 获取切入点所在目标对象
        log.setClassPath(pjp.getTarget().getClass().getName());
        // 注解中方法的注释
        String[] paramsDoc = null;
        String doc = null;
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        if (!Objects.isNull(method)){
			// 得到方法名
            log.setMethodName(method.getName());
            LLogger lLog = method.getAnnotation(LLogger.class);
            doc = lLog.description();
            paramsDoc = lLog.params();
        }
        log.setMethodDoc(doc);
        // 得到方法入参
        Object[] args = pjp.getArgs();
        StringBuilder paramsStr = new StringBuilder();
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
		// 得到方法的参数类型
        Type[] genericParameterTypes = method.getGenericParameterTypes();
		// 得到方法参数的名称
        String[] parameterNames = pnd.getParameterNames(method);
		// 拼接参数信息
        int len = Math.min(paramsDoc.length, args.length);
        for (int i = 0; i < len; i++) {
            if(args[i] == null){
                continue;
            }
            paramsStr.append(args[i].toString()).append(" (").append(genericParameterTypes[i]).append("---").append(parameterNames[i]).append("---").append(paramsDoc[i]).append(")");
            if (i < paramsDoc.length -1){
                paramsStr.append("\t | \t");
            }
        }
        log.setMethodParam(paramsStr.toString());
        return log;
    }

    /**
     * 保存log错误信息日志文件
     * @author lvyx
     * @since 2021/12/4 22:35
     **/
    private void saveError(StringBuilder stringBuilder) throws Exception {
        File file = new File("temp/error.log");
        System.out.println(file.exists());
        if (! file.exists()){
            // 创建日志文件
            FileUtils.forceMkdirParent(file);
        }
        FileWriter fileWriter = new FileWriter(file);
        StackTraceElement[] stackTrace = this.e.getStackTrace();
        Arrays.stream(stackTrace).forEach(stackTraceElement -> {
            stringBuilder.append(stackTraceElement.toString()).append("\n");
        });
        stringBuilder.append("\n\n");
        // 添加到日志文件中
        fileWriter.append(stringBuilder.toString());
    }


    /**
     * 过滤代理IP,得到用户真实IP
     * @param request request
     * @return java.lang.String
     * @author lvyx
     * @since 2021/12/6 17:55
     **/
    private static String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
