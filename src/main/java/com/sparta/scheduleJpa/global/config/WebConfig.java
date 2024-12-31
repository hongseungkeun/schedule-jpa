package com.sparta.scheduleJpa.global.config;

import com.sparta.scheduleJpa.global.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<LoginFilter> customFilter() {
        // LoginFilter를 등록하여 요청에 대해 필터링 적용
        FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<>();

        // 필터 객체 설정 (LoginFilter는 로그인 세션 검증을 위한 필터)
        filterRegistrationBean.setFilter(new LoginFilter());

        // 필터의 실행 순서를 설정
        filterRegistrationBean.setOrder(1);

        // 모든 요청 경로에 필터를 적용
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
