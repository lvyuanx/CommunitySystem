package com.lvyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * 社区疫情健康管理系统启动类
 * </p>
 *
 * @author lvyx
 * @since 2021-12-23 11:15:02
 */
@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CommunitySystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(CommunitySystemApplication.class, args);
        printSystemInfo(run);
    }


    private static void printSystemInfo(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String port = environment.getProperty("server.port");
        String context = environment.getProperty("server.servlet.context-path");
        String ip = "localhost";
        String systemIndexUrl = "http://" + ip + ":" + port + context;
        String swaggerBootStrapApiUrl = systemIndexUrl + "doc.html";
        System.out.printf(">>>>>>>>>>>>>>>>>>>>接口文档地址(bootstrap美化过，推荐使用)：%s >>>>>>>>>\n", swaggerBootStrapApiUrl);
    }
}
