package com.example.demo.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 自定义鉴权拦截器
 * 用于在Controller接口执行前进行拦截鉴权
 */
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 前置拦截方法
     * 
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param handler  处理器对象
     * @return true 放行, false 拦截
     * @throws Exception 抛出异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 1.获取本次请求的HTTP动词和具体路径
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 2.手写细粒度放行规则
        // 规则 A:POST 请求且路径精确等于"/api/users"，放行（注册）
        boolean isCreateUser = "POST".equalsIgnoreCase(method) && "/api/users".equals(uri);
        // 规则 C:POST 请求且路径精确等于"/api/users/login"，放行（登录）
        boolean isLoginUser = "POST".equalsIgnoreCase(method) && "/api/users/login".equals(uri);
        // 规则 D:GET 请求且路径包含"/api/users/"且以"/detail"结尾，放行（查询用户详情）
        boolean isGetUserDetail = "GET".equalsIgnoreCase(method) && uri.matches(".*/api/users/\\d+/detail");
        // 规则 E:GET 请求且路径精确等于"/api/users/{id}"，放行（查询用户）
        boolean isGetUser = "GET".equalsIgnoreCase(method) && uri.matches(".*/api/users/\\d+");

        // 满足合法公开规则直接放行，无需查验 Token
        if (isCreateUser || isLoginUser || isGetUserDetail || isGetUser) {
            return true;
        }

        // 3.执行严格的Token校验（DELETE、PUT等敏感操作）
        // 尝试从HTTP请求头中截获名为"Authorization"的隐藏令牌信息
        String token = request.getHeader("Authorization");

        // 如果没有携带Token,直接拦截,不放行到Controller
        if (token == null || token.isEmpty()) {
            // 构造401报错的JSON字符串返回给前端
            response.setContentType("application/json;charset=UTF-8");
            String errorJson = "{\"code\": 401,\"msg\":\"非法操作:敏感动作[" + method + "]需登录授权\"}";
            response.getWriter().write(errorJson);
            return false; // 返回false表示拦截打回
        }
        return true; // 令牌存在,返回true予以放行
    }
}