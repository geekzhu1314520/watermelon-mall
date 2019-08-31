package com.watermelon.mall.spring.boot.web;

import com.watermelon.common.framework.constant.MallConstants;
import com.watermelon.common.framework.servlet.CorsFilter;
import com.watermelon.mall.admin.sdk.interceptor.AdminSecurityInterceptor;
import com.watermelon.mall.spring.boot.web.handler.GlobalExceptionHandler;
import com.watermelon.mall.spring.boot.web.handler.GlobalResponseBodyHandler;
import com.watermelon.mall.spring.boot.web.interceptor.AccessLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({
        DispatcherServlet.class,
        WebMvcConfigurer.class,
        AccessLogInterceptor.class,
        AdminSecurityInterceptor.class
})
public class AdminMVCAutoConfiguration implements WebMvcConfigurer {

    @Bean
    public AccessLogInterceptor adminAccessLogInterceptor() {
        return new AccessLogInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(GlobalResponseBodyHandler.class)
    public GlobalResponseBodyHandler globalResponseBodyHandler() {
        return new GlobalResponseBodyHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AdminSecurityInterceptor.class)
    public AdminSecurityInterceptor adminSecurityInterceptor() {
        return new AdminSecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAccessLogInterceptor()).addPathPatterns(MallConstants.ROOT_PATH_ADMIN);
        registry.addInterceptor(adminSecurityInterceptor()).addPathPatterns(MallConstants.ROOT_PATH_ADMIN);
    }

    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
