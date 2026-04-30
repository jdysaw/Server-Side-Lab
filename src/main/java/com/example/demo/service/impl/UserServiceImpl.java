package com.example.demo.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.common.ResultCode;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserInfoUpdateDTO;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 用户业务逻辑实现类
 * 处理注册、登录等核心业务
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Redis 缓存 Key 前缀
     */
    private static final String CACHE_KEY_PREFIX = "user:detail:";

    /**
     * 缓存过期时间（分钟）
     */
    private static final long CACHE_EXPIRE_MINUTES = 10;

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

        // 使用 JWT 生成 Token
        String jwt = jwtUtil.generateToken(userDTO.getUsername());
        return Result.success(jwt);
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
        // 1. 创建分页对象（参数 1：当前页码，参数 2：每页显示条数）
        Page<User> pageParam = new Page<>(pageNum, pageSize);
        // 2. 执行分页查询（参数 1：分页对象，参数 2：查询条件 Wrapper，这里传 null 代表全表）
        // 框架会自动执行一条 COUNT 语句查总数，再拼接 LIMIT 执行分页
        Page<User> resultPage = userMapper.selectPage(pageParam, null);
        // 3. 返回结果（resultPage 中包含了 records 数据列表、total 总条数、pages 总页数）
        return Result.success(resultPage);
    }

    /**
     * 获取用户详情（多表联查 + Redis 缓存）
     * 采用 Cache-Aside 策略：先查缓存，查不到再查 DB 并回写缓存
     * 
     * @param userId 用户 ID
     * @return 用户详情结果
     */
    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;

        // 1. 先查缓存
        String json = redisTemplate.opsForValue().get(key);
        if (json != null && !json.isEmpty()) {
            try {
                UserDetailVO cacheVO = JSONUtil.toBean(json, UserDetailVO.class);
                System.out.println("=== 从 Redis 缓存中获取用户详情：" + userId + " ===");
                return Result.success(cacheVO);
            } catch (Exception e) {
                // 缓存数据异常，删掉脏缓存，继续查数据库
                System.out.println("=== 缓存数据异常，删除脏缓存 ===");
                redisTemplate.delete(key);
            }
        }

        // 2. 缓存未命中，查数据库
        System.out.println("=== 缓存未命中，查询数据库获取用户详情：" + userId + " ===");
        UserDetailVO detail = userInfoMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 3. 写缓存（设置过期时间）
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(detail), CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        System.out.println("=== 用户详情已写入 Redis 缓存，过期时间：" + CACHE_EXPIRE_MINUTES + "分钟 ===");
        return Result.success(detail);
    }

    /**
     * 更新用户扩展信息
     * 先操作 DB，成功后删除旧缓存（保证缓存一致性）
     * 
     * @param userId 用户 ID
     * @param dto    更新数据
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateUserInfo(Long userId, UserInfoUpdateDTO dto) {
        // 1. 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 2. 更新或插入 user_info 表
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId);
        UserInfo existingInfo = userInfoMapper.selectOne(queryWrapper);

        if (existingInfo == null) {
            // 不存在则插入
            UserInfo newInfo = new UserInfo();
            newInfo.setUserId(userId);
            newInfo.setRealName(dto.getRealName());
            newInfo.setPhone(dto.getPhone());
            newInfo.setAddress(dto.getAddress());
            userInfoMapper.insert(newInfo);
        } else {
            // 存在则更新
            existingInfo.setRealName(dto.getRealName());
            existingInfo.setPhone(dto.getPhone());
            existingInfo.setAddress(dto.getAddress());
            userInfoMapper.updateById(existingInfo);
        }

        // 3. 删除旧缓存（保证下次查询能获取最新数据）
        String cacheKey = CACHE_KEY_PREFIX + userId;
        redisTemplate.delete(cacheKey);
        System.out.println("=== 用户信息更新成功，已删除缓存：" + cacheKey + " ===");

        return Result.success("更新成功!");
    }

    /**
     * 删除用户
     * 先操作 DB，成功后删除旧缓存
     * 
     * @param userId 用户 ID
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteUser(Long userId) {
        // 1. 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 2. 删除 user_info 表中的关联数据（如果有）
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserId, userId);
        userInfoMapper.delete(queryWrapper);

        // 3. 删除 sys_user 表中的用户数据
        userMapper.deleteById(userId);

        // 4. 删除缓存
        String cacheKey = CACHE_KEY_PREFIX + userId;
        redisTemplate.delete(cacheKey);
        System.out.println("=== 用户删除成功，已删除缓存：" + cacheKey + " ===");

        return Result.success("删除成功!");
    }
}
