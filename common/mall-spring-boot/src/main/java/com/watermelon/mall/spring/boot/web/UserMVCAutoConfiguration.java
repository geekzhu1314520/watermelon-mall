package com.watermelon.mall.spring.boot.web;

import com.watermelon.common.framework.constant.MallConstants;
import com.watermelon.common.framework.servlet.CorsFilter;
import com.watermelon.mall.spring.boot.web.handler.GlobalExceptionHandler;
import com.watermelon.mall.spring.boot.web.handler.GlobalResponseBodyHandler;
import com.watermelon.mall.spring.boot.web.interceptor.AccessLogInterceptor;
import com.watermelon.mall.user.sdk.interceptor.UserSecurityInterceptor;
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
        UserSecurityInterceptor.class
})
public class UserMVCAutoConfiguration implements WebMvcConfigurer {

    @Bean
    public AccessLogInterceptor userAccessLogInterceptor() {
        return new AccessLogInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(UserSecurityInterceptor.class)
    public UserSecurityInterceptor userSecurityInterceptor() {
        return new UserSecurityInterceptor();
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAccessLogInterceptor()).addPathPatterns(MallConstants.ROOT_PATH_USER);
        registry.addInterceptor(userSecurityInterceptor()).addPathPatterns(MallConstants.ROOT_PATH_USER);
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
