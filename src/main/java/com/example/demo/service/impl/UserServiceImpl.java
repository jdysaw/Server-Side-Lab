package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.common.ResultCode;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 用户业务逻辑实现类
 * 处理注册、登录等核心业务
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     * 
     * @param userDTO 用户传输对象
     * @return 注册结果
     */
    @Override
    public Result<String> register(UserDTO userDTO) {
        // 1. 校验用户是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }

        // 2. 存入真实数据库
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        userMapper.insert(user);

        return Result.success("注册成功!");
    }

    /**
     * 用户登录
     * 
     * @param userDTO 用户传输对象
     * @return 登录结果
     */
    @Override
    public Result<String> login(UserDTO userDTO) {
        // 1. 校验用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 2. 校验密码是否正确
        if (!dbUser.getPassword().equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }

        // 模拟生成 Token
        String token = "Bearer " + UUID.randomUUID().toString().replace("-", "");
        return Result.success(token);
    }

    /**
     * 根据 ID 获取用户信息
     * 
     * @param id 用户 ID
     * @return 用户信息结果
     */
    @Override
    public Result<String> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        return Result.success("查询成功，用户名为: " + user.getUsername());
    }

    /**
     * 获取用户分页数据
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @return 用户分页数据
     */
    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        // 1. 创建分页对象（参数1：当前页码，参数2：每页显示条数）
        Page<User> pageParam = new Page<>(pageNum, pageSize);
        // 2. 执行分页查询（参数1：分页对象，参数2：查询条件Wrapper，这里传null代表全表）
        // 框架会自动执行一条COUNT语句查总数，再拼接LIMIT执行分页
        Page<User> resultPage = userMapper.selectPage(pageParam, null);
        // 3. 返回结果（resultPage中包含了records数据列表、total总条数、pages总页数）
        return Result.success(resultPage);
    }
}
