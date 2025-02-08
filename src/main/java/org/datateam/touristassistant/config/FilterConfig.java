package org.datateam.touristassistant.config;

import org.datateam.touristassistant.Filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter>jwtAuthenticationFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter());

        // 这里设置要过滤的路径，例如微信小程序接口
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
