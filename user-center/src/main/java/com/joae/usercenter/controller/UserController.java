package com.joae.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joae.usercenter.common.BaseResponse;
import com.joae.usercenter.common.ErrorCode;
import com.joae.usercenter.constant.UserConstant;
import com.joae.usercenter.exception.BusinessException;
import com.joae.usercenter.model.domain.User;
import com.joae.usercenter.model.domain.request.UserLoginRequest;
import com.joae.usercenter.model.domain.request.UserRegisterRequest;
import com.joae.usercenter.service.UserService;
import com.joae.usercenter.utils.ResultUtil;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String studentNumber = userRegisterRequest.getStudentNumber();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,studentNumber)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, studentNumber);
        return ResultUtil.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
//            return ResultUtil.error(ErrorCode.PARAM_NULL_ERROR);
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtil.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout( HttpServletRequest request) {
        if(request==null){
            return null;
        }
        int i = userService.userLogout(request);
        return ResultUtil.success(i);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object attribute = request.getSession().getAttribute(UserConstant.LOGIN_STATE);
        User currentUser = (User) attribute;
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        long userId = currentUser.getId();
        //todo 校验用户是否合法
        User user = userService.getById(userId);
        User saftyUser = userService.getSaftyUser(user);
        return ResultUtil.success(saftyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        boolean admin = isAdmin(request);
        if (!admin) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"非管理员");
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }
        List<User> users = userService.list(userQueryWrapper);
        List<User> userList = users.stream().map(user -> userService.getSaftyUser(user)).collect(Collectors.toList());
        return ResultUtil.success(userList);

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id, HttpServletRequest request) {
        boolean admin = isAdmin(request);
        if (!admin) {
            return null;
        }

        if (id <= 0) {
            return null;
        }

        boolean b = userService.removeById(id);
        return ResultUtil.success(b);
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
