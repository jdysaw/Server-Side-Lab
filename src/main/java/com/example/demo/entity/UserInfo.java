package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户信息实体类
 * 对应数据库表 user_info
 */
@Data
@TableName("user_info")
public class UserInfo {
  /**
   * 主键自增
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 真实姓名
   */
  private String realName;

  /**
   * 手机号码
   */
  private String phone;

  /**
   * 联系地址
   */
  private String address;

  /**
   * 关联用户 ID
   */
  private Long userId;
}
