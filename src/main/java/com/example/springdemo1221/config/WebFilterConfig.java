package com.example.springdemo1221.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/** 
 * 
 * Description: 拦截器
 * @author huangweicheng
 * @date 2020/12/28   
*/
@Configuration
public class WebFilterConfig implements WebMvcConfigurer {
    //防止redis报空，在context前初始化
    @Bean
    public LoginInterceptor getLoginHandlerInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration registration = registry.addInterceptor(getLoginHandlerInterceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/**/login");
    }
}
