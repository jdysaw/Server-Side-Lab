package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

  ble
   c class SecurityConfig {
   
   A
   rivate JwtAuthenticationFilte
   
   **
   * 
   * 定义
   *
    para
        rn SecurityFilterChain
        ws Exception 配置异常
         
                                                                                                     // 
        e
        
            s(cors ->
            f(AbstractHttpConfigurer::disable) // 关闭 CSRF 防护，前后端分离项目不需要
            sionManag
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
            horizeHttpReque
            // 放行注册接口 
            // 放行登录接口
            .requestMatchers(HttpMethod.POST, "/api/users/login").permi
            // 其他所有请求都必须
   

     
          .for
          .ht
    
   
   
  /**
   * CORS 跨域配置
    许前端跨域访问
    
    return CorsConfigurationSource
    
    n
    ic CorsCon
    CorsConfiguration configuration = new CorsConfigurat
    // 允许所有来源的跨域请
    configuration.setAllowedOrigins(Arrays.asList("*"));

    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "
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
