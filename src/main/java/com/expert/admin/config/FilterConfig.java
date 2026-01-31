package com.expert.admin.config;

import com.expert.admin.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtAuthenticationFilter);
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);
        return registration;
    }
}
