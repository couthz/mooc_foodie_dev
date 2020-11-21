package com.zhc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    public CorsConfig() {

    }

    @Bean
    public CorsFilter corsFilter() {
        // 1.添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        //config.addAllowedOrigin("http://192.168.202.132:80");
        config.addAllowedOrigin("*");
        //是否允许发送cookie
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        //2. 为url设置映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        //为所有路径都添加配置信息，前端能访问任意路径
        corsSource.registerCorsConfiguration("/**",config);

        return new CorsFilter(corsSource);

    }
}
