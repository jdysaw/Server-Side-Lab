package com.example.demo.dto;

import lombok.Data;

/**
 * 用户数据传输对象
 * 用于接收前端传递的 JSON 数据
 */
@Data
public class UserDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
