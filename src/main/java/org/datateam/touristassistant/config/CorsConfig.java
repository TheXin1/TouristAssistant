package org.datateam.touristassistant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 匹配所有接口路径
                .allowedOrigins("*")  // 允许所有来源
                .allowedMethods("*")  // 允许所有 HTTP 方法（GET、POST 等）
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(false)  // 是否允许携带 Cookie（如果不需要则设为 false）
                .maxAge(3600);  // 预检请求缓存时间（秒）
    }
}