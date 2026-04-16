package com.example.demo.vo;

import lombok.Data;

/**
 * 用户详情视图对象
 * 用于承接多表联查结果，并作为接口返回对象
 */
@Data
public class UserDetailVO {
  /**
   * 用户 ID
   */
  private Long userId;

  /**
   * 用户名
   */
  private String username;

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
}
