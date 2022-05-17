package com.lvyx.config;

import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    private static final ApiInfo API_INFO = new CreateDocker()
            .buildApiInfo("社区健康疫情管理系统",
                    "社区健康疫情管理系统",
                    "v1.0",
                    "lvyx",
                    "http://localhost:8848",
                    "testlv@foxmail.com");

    /**
     * 用户管理
     * @author lvyx
     * @since 2021/12/28 17:20
     **/
    @Bean
    public Docket authorApi() {
        return new CreateDocker().create("用户管理",  API_INFO,"/author/**");
    }

    /**
     * 公共模块
     * @author lvyx
     * @since 2021/12/28 17:20
     **/
    @Bean
    public Docket commonsApi() {
        return new CreateDocker().create("公共模块",  API_INFO,"/commons/**");
    }

    /**
     * 社区管理模块
     * @return springfox.documentation.spring.web.plugins.Docket
     * @author lvyx
     * @since 2022/2/1 17:22
     **/
    @Bean
    public Docket communityApi() {
        return new CreateDocker().create("社区管理模块",  API_INFO,"/community/**");
    }

    /**
     * 用户管理
     * @author lvyx
     * @since 2021/12/28 17:20
     **/
    @Bean
    public Docket mailApi() {
        return new CreateDocker().create("邮件管理模块",  API_INFO,"/mail/**");
    }





    /**
     * 构建Docker
     * @author lvyx
     * @since 2021/12/28 17:19
     **/
    private static class CreateDocker{
        /**
         * 创建
         * @param groupName 分组名称
         * @param paths 路径
         * @return springfox.documentation.spring.web.plugins.Docket
         * @author lvyx
         * @since 2021/12/28 17:20
         **/
        public Docket create(String groupName, ApiInfo apiInfo, String... paths){
            List<ResponseMessage> responseMessageList = initResponseMessages();
            Docket docket=new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo)
                    .globalResponseMessage(RequestMethod.GET, responseMessageList)
                    .globalResponseMessage(RequestMethod.POST, responseMessageList)
                    .directModelSubstitute(LocalDateTime.class, Date.class)
                    .directModelSubstitute(LocalDate.class, String.class)
                    .directModelSubstitute(LocalTime.class, String.class)
                    .directModelSubstitute(ZonedDateTime.class, String.class)
                    //分组名称
                    .groupName(groupName)
                    .globalOperationParameters(getPubParam())
                    .select()
                    .build();
            ApiSelectorBuilder apiSelectorBuilder = docket.select()
                    .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class));
            if (Objects.nonNull(paths) && paths.length > 0) {
                Predicate<String> predicate = PathSelectors.ant(paths[0]);
                for (String s : paths) {
                    String path = s.trim();
                    if (Strings.isBlank(path)) {
                        continue;
                    }
                    predicate = predicate.or(PathSelectors.ant(path));
                }

                apiSelectorBuilder = apiSelectorBuilder
                        .paths(predicate);

            } else {
                apiSelectorBuilder = apiSelectorBuilder
                        .paths(PathSelectors.any());
            }
            return apiSelectorBuilder.build();
        }

        private List<ResponseMessage> initResponseMessages() {
            List<ResponseMessage> responseMessageList = new ArrayList<>();
            responseMessageList.add(new ResponseMessageBuilder().code(200).message("成功").build());
            responseMessageList.add(new ResponseMessageBuilder().code(401).message("没有登录").build());
            responseMessageList.add(new ResponseMessageBuilder().code(403).message("没有权限访问").build());
            responseMessageList.add(new ResponseMessageBuilder().code(404).message("资源不存在").build());
            responseMessageList.add(new ResponseMessageBuilder().code(500).message("系统内部错误").build());
            return responseMessageList;
        }

        private ApiInfo buildApiInfo(String title, String description, String version, String name, String url, String email) {
            return new ApiInfoBuilder()
                    .title(title)
                    .description(description)
                    .version(version)
                    .contact(new Contact(name, url, email))
                    .build();
        }

        private List<Parameter> getPubParam() {
            ParameterBuilder tokenPar = new ParameterBuilder();
            List<Parameter> pars = new ArrayList<>();
            tokenPar.name("L-TOKEN").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
            pars.add(tokenPar.build());
            return pars;
        }
    }

}