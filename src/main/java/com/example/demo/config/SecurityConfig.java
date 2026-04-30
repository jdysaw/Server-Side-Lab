package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.Arrays;

/**
 * Spring Security 配置类
 * 用于接管接口鉴权，替代原有的拦截器鉴权方式
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 配置 Spring Security 过滤链
     * 定义哪些接口放行，哪些接口需要认证
     *
     * @param http HttpSecurity 对象
     * @return SecurityFilterChain
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 开启全局 CORS 配置
            .csrf(AbstractHttpConfigurer::disable) // 关闭 CSRF 防护，前后端分离项目不需要
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态会话，不创建 Session
            )
            .authorizeHttpRequests(auth -> auth
                // 放行注册接口
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                // 放行登录接口
                .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                // 其他所有请求都必须先认证
                .anyRequest().authenticated()
            )
            .formLogin(AbstractHttpConfigurer::disable) // 关闭表单登录
            .httpBasic(AbstractHttpConfigurer::disable); // 关闭 httpBasic 认证
        return http.build();
    }

    /**
     * CORS 跨域配置
     * 允许前端跨域访问
     *
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许所有来源的跨域请求
        configuration.setAllowedOrigins(Arrays.asList("*"));
        // 允许所有 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许所有请求头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 暴露认证相关的响应头
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径应用 CORS 配置
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
