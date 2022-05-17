package com.lvyx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * <p>
 * TODO(类描述信息)
 * </p>
 *
 * @author lvyx
 * @since 2022-03-04 23:14:15
 */
@Configuration
public class CORSConfiguration  {


    public CorsConfiguration addCorsMappings() {
        CorsConfiguration registry = new CorsConfiguration();
        registry.addAllowedOrigin("*");
        registry.addAllowedHeader("*");
        registry.addAllowedMethod("*");
        return registry;
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", addCorsMappings());
        return new CorsFilter(source);
    }
}
