package com.joae.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joae.usercenter.constant.UserConstant;
import com.joae.usercenter.model.domain.User;
import com.joae.usercenter.model.domain.request.UserLoginRequest;
import com.joae.usercenter.model.domain.request.UserRegisterRequest;
import com.joae.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        boolean admin = isAdmin(request);
        if (!admin) {
            return new ArrayList<>();
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }
        List<User> users = userService.list(userQueryWrapper);
        return users.stream().map(user -> userService.getSaftyUser(user)).collect(Collectors.toList());

    }

    @PostMapping("/delete")
    public boolean deleteUsers(@RequestBody long id, HttpServletRequest request) {
        boolean admin = isAdmin(request);
        if (!admin) {
            return false;
        }

        if (id <= 0) {
            return false;
        }

        return userService.removeById(id);
    }

    /**
     * 判断是否为管理员
     *
     * @param request
     * @return 是或否
     */
    private boolean isAdmin(HttpServletRequest request) {
        //鉴权，只有管理员能够查询
        Object userObject = request.getSession().getAttribute(UserConstant.LOGIN_STATE);
        User user = (User) userObject;
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;

    }


}
