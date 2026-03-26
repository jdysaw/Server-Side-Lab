package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper 接口
 * 继承 MyBatis-Plus 的 BaseMapper，获得单表增删改查能力
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
