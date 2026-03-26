package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 提供用户的注册、登录及查询接口
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 1. 新增用户（注册）
     * 路径为 POST /api/users
     * 
     * @param userDTO 用户传输对象
     * @return 注册结果
     */
    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    /**
     * 2. 用户登录
     * 路径为 POST /api/users/login
     * 
     * @param userDTO 用户传输对象
     * @return 登录结果(包含 Token)
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    /**
     * 3. 获取用户信息（查）
     * 用于测试拦截器放行
     * 
     * @param id 用户ID
     * @return 统一响应结果
     */
    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return Result.success("查询成功，正在返回 ID 为 " + id + " 的用户信息");
    }

    /**
     * 测试异常捕获
     * 
     * @return 统一响应结果
     */
    @GetMapping("/error")
    public Result<String> testError() {
        int a = 1 / 0; // 故意触发算术异常
        return Result.success("这行代码不会执行");
    }
}
