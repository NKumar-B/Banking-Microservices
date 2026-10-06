package com.banking.gateway.config;

import com.banking.gateway.security.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        FilterRegistrationBean<JwtAuthenticationFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(jwtAuthenticationFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(1);

        return registration;
    }
}