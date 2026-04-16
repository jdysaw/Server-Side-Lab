package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.UserInfo;
import com.example.demo.vo.UserDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户信息 Mapper 接口
 * 继承 BaseMapper 获得单表操作能力，同时提供自定义多表联查方法
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

  /**
   * 多表联查获取用户详情
   * 通过 LEFT JOIN 关联 sys_user 和 user_info 表
   * 
   * @param userId 用户 ID
   * @return 用户详情视图对象
   */
  @Select("SELECT u.id AS userId, u.username, i.real_name AS realName, i.phone, i.address FROM sys_user u LEFT JOIN user_info i ON u.id = i.user_id WHERE u.id = #{userId}")
  UserDetailVO getUserDetail(@Param("userId") Long userId);
}
