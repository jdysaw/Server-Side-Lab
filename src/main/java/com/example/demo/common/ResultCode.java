package com.example.demo.common;

/**
 * 全局状态码枚举类
 * 用于定义统一定义的业务状态码
 */
public enum ResultCode {
  // 基础状态码
  SUCCESS(200, "操作成功"),
  ERROR(500, "系统繁忙,请稍后再试"),
  // 权限相关
  TOKEN_INVALID(401, "登录凭证已缺失或过期,请重新登录"), // 模拟未授权状态码
  
  // 用户相关
  USER_HAS_EXISTED(4001, "该用户名已被注册"),
  USER_NOT_EXIST(4002, "该用户不存在"),
  PASSWORD_ERROR(4003, "账号或密码错误");

  private final Integer code;
  private final String msg;

  /**
   * 枚举构造函数
   * 
   * @param code 状态码
   * @param msg  提示信息
   */
  ResultCode(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  /**
   * 获取提示信息
   * 
   * @return 提示信息
   */
  public String getMsg() {
    return msg;
  }

  /**
   * 获取状态码
   * 
   * @return 状态码
   */
  public Integer getCode() {
    return code;
  }
}