package com.joae.usercenter.service;
import java.util.Date;

import com.joae.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        User user = new User();
        user.setUsername("testJoae");
        user.setUserAccount("123");
        user.setAvatarUrl("https://cn.bing.com/images/search?q=%e5%a4%b4%e5%83%8f&id=025D46BCC4A58A565044247A6AF33B853D3D89AA&FORM=IQFRBA");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.save(user);

        System.out.println(user.getId());
        assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "mike";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String studentNumber = "210840";
        long result = userService.userRegister(userAccount, userPassword, checkPassword,studentNumber);
        Assertions.assertTrue(result>0);
    }
}