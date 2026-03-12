package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 提供用户的增删改查接口
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * 1. 获取用户信息（查）
     * @param id 用户ID
     * @return 统一响应结果
     */
    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return Result.success("查询成功，正在返回 ID 为 " + id + " 的用户信息");
    }

    /**
     * 2. 新增用户（增）- 接收 JSON 格式数据
     * @param user 用户对象
     * @return 统一响应结果
     */
    @PostMapping
    public Result<String> createUser(@RequestBody User user) {
        return Result.success("新增成功，接收到用户：" + user.getName() + "，年龄：" + user.getAge());
    }

    /**
     * 3. 全量更新用户信息（改）
     * @param id 用户ID
     * @param user 用户对象
     * @return 统一响应结果
     */
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return Result.success("更新成功，ID " + id + " 的用户已修改为：" + user.getName());
    }

    /**
     * 4. 删除用户（删）
     * @param id 用户ID
     * @return 统一响应结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable("id") Long id) {
        return Result.success("删除成功，已移除 ID 为 " + id + " 的用户");
    }

    /**
     * 5. 测试异常捕获
     * @return 统一响应结果
     */
    @GetMapping("/error")
    public Result<String> testError() {
        int a = 1 / 0; // 故意触发算术异常
        return Result.success("这行代码不会执行");
    }
}
