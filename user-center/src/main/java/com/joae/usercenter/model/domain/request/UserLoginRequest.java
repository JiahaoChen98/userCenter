package com.joae.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author joae
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -5630944277599926124L;

    String userAccount;
    String userPassword;

}
