package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserInfoUpdateDTO;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserDetailVO;
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
        return userService.getUserById(id);
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

    /**
     * 分页查询用户列表
     * 路径为 GET /api/users/page
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @return 用户分页列表
     */
    @GetMapping("/page")
    public Result<Object> getUserPage(@RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return userService.getUserPage(pageNum, pageSize);
    }

    /**
     * 查询用户详情（多表联查 + Redis 缓存）
     * 路径为 GET /api/users/{id}/detail
     *
     * @param userId 用户 ID
     * @return 用户详情
     */
    @GetMapping("/{id}/detail")
    public Result<UserDetailVO> getUserDetail(@PathVariable("id") Long userId) {
        return userService.getUserDetail(userId);
    }

    /**
     * 更新用户扩展信息
     * 路径为 PUT /api/users/{id}/info
     *
     * @param userId 用户 ID
     * @param dto    更新数据
     * @return 更新结果
     */
    @PutMapping("/{id}/info")
    public Result<String> updateUserInfo(@PathVariable("id") Long userId, @RequestBody UserInfoUpdateDTO dto) {
        return userService.updateUserInfo(userId, dto);
    }

    /**
     * 删除用户
     * 路径为 DELETE /api/users/{id}
     *
     * @param userId 用户 ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }
}
