package com.example.demo.service;

import com.example.demo.common.Result;
import com.example.demo.dto.UserDTO;

/**
 * 用户业务逻辑接口
 * 定义注册和登录的业务方法
 */
public interface UserService {
    /**
     * 用户注册
     * @param userDTO 用户传输对象
     * @return 注册结果
     */
    Result<String> register(UserDTO userDTO);

    /**
     * 用户登录
     * @param userDTO 用户传输对象
     * @return 登录结果(包含 Token)
     */
    Result<String> login(UserDTO userDTO);

    /**
     * 根据 ID 获取用户信息
     * @param id 用户 ID
     * @return 用户信息
     */
    Result<String> getUserById(Long id);

    /**
     * 获取用户分页数据
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @return 用户分页数据
     */
    Result<Object> getUserPage(Integer pageNum, Integer pageSize);
}
