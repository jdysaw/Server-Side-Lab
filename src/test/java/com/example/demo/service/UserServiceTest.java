package com.example.demo.service;

import com.example.demo.common.Result;
import com.example.demo.common.ResultCode;
import com.example.demo.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testRegisterAndLogin() {
        // 1. 准备测试数据
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("123456");

        // 2. 测试注册
        Result<String> registerResult = userService.register(userDTO);
        assertEquals(200, registerResult.getCode());
        assertEquals("注册成功", registerResult.getData());

        // 3. 测试重复注册
        Result<String> duplicateRegisterResult = userService.register(userDTO);
        assertEquals(ResultCode.USER_HAS_EXISTED.getCode(), duplicateRegisterResult.getCode());

        // 4. 测试登录成功
        Result<String> loginResult = userService.login(userDTO);
        assertEquals(200, loginResult.getCode());
        assertNotNull(loginResult.getData());
        assertTrue(loginResult.getData().startsWith("Bearer "));

        // 5. 测试密码错误
        UserDTO wrongPwdUser = new UserDTO();
        wrongPwdUser.setUsername("testuser");
        wrongPwdUser.setPassword("wrongpwd");
        Result<String> wrongPwdResult = userService.login(wrongPwdUser);
        assertEquals(ResultCode.PASSWORD_ERROR.getCode(), wrongPwdResult.getCode());

        // 6. 测试用户不存在
        UserDTO nonExistUser = new UserDTO();
        nonExistUser.setUsername("nonexist");
        nonExistUser.setPassword("123456");
        Result<String> nonExistResult = userService.login(nonExistUser);
        assertEquals(ResultCode.USER_NOT_EXIST.getCode(), nonExistResult.getCode());
    }
}
