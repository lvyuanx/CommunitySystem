package com.lvyx.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.logging.log4j.util.Strings;

import java.util.Collections;

/**
 * <p>
 *  代码生成器
 * </p>
 *
 * @author lvyx
 * @since 2021/12/23 11:23
 **/
class SpringbootMybatisPlusGenerator {
    // 代码自动生成器
    public static void main(String[] args) {
        // 表名
        String[] tableNames = {"l_community_exception"};
        // 模块名
        String moduleName = "community";
        // 包名
        String xmlPakageName = "";
        // 父包名
        String parentPakageName = "com.lvyx.community";
        // 模块的绝对路径
        String projectPath=System.getProperty("user.dir") +"/"+ moduleName  ;
        System.out.println(projectPath);
        FastAutoGenerator.create("jdbc:mysql://119.23.188.80:3306/l_community?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8", "root", "7515bcc")
                // 全局配置 GlobalConfig
                .globalConfig(builder -> {
                             // 设置作者名
                    builder.author("lvyx")
                            //.fileOverride()  // 开启覆盖已生成文件，默认值false
                            .enableSwagger() // 开启 swagger 模式，默认值false
                            .dateType(DateType.TIME_PACK)
                            // 指定输出目录
                            .outputDir(projectPath+"/src/main/java");
                })
                // 包配置 PackageConfig
                .packageConfig(builder -> {
                    builder.parent(parentPakageName)        // 设置父包名
                            //.moduleName(moduleName)     // 父包模块名，默认值:无
                            // 上面两行代码加起来:com.IT.blog.xxx(entity、service、controller等）
                            .entity("entity")       // Entity包名
                            .service("service")     // Service包名
                            .serviceImpl("service.impl") // ServiceImpl包名
                            .controller("controller")   // Controller包名
                            .mapper("mapper")           // Mapper包名
                            .xml("mapper")              // Mapper XML包名
                            // 路径配置信息，设置mapperXml生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,projectPath+"/src/main/resources/mapper"+ (Strings.isBlank(xmlPakageName) ? "" : "/"+xmlPakageName )));
                })
                // 配置策略 StrategyConfig
                .strategyConfig(builder -> {
                    builder.addInclude(tableNames)                    // 增加表匹配，需要映射的数据库中的表名
                            .addTablePrefix("l_")                 // 增加过滤表前缀，生成时将数据库表的前缀"p_"去掉
                            // 1.service策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService")         // 格式化 service 接口文件名称
                            .formatServiceImplFileName("%sServiceImpl") // 格式化 service 实现类文件名称
                            // 2.实体策略配置
                            .entityBuilder()
                            .naming(NamingStrategy.underline_to_camel)  // 数据库表映射到实体的命名策略，下划线转驼峰命名
                            .enableLombok()                   // 开启 lombok 模型
                            .logicDeleteColumnName("is_delete") // 逻辑删除字段名(数据库)
                            .enableTableFieldAnnotation()     // 开启生成实体时生成字段注解
                            .idType(IdType.ASSIGN_UUID)              // 全局主键类型
                            .enableRemoveIsPrefix()         // 	开启 Boolean 类型字段移除 is 前缀

                            // 3.controller策略配置
                            .controllerBuilder()
                            .formatFileName("%sController")   // 格式化文件名称
                            .enableRestStyle()                // 开启生成@RestController 控制器
                            // 4.mapper策略配置
                            .mapperBuilder()
                            .superClass(BaseMapper.class)     // 设置父类
                            .enableMapperAnnotation()         // 开启 @Mapper 注解
                            .formatMapperFileName("%sMapper") // 格式化 mapper 文件名称
                            .formatXmlFileName("%sMapper");   // 格式化 xml 实现类文件名称
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
 
}