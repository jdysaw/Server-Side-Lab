package com.example.demo.config;

import com.example.demo.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web全局配置类
 * 用于接管并注册MVC相关的组件(如拦截器等)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 注册自定义拦截器
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加自定义的AuthInterceptor鉴权拦截器
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/api/**") // 拦截 /api 下的所有请求路径
                .excludePathPatterns("/api/users/login"); // 仅放行登录接口
    }
}