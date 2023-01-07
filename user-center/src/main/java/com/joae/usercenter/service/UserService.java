package com.joae.usercenter.service;

import com.joae.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 13666
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-12-31 20:59:39
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    public long userRegister(String userAccount,String userPassword,String checkPassword);

}
