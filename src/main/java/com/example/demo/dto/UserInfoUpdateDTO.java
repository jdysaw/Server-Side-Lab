package com.example.demo.dto;

import lombok.Data;

/**
 * 用户信息更新数据传输对象
 * 用于接收前端传递的用户扩展信息更新数据
 */
@Data
public class UserInfoUpdateDTO {
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
