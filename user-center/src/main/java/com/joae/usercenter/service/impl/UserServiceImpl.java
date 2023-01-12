package com.joae.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joae.usercenter.common.ErrorCode;
import com.joae.usercenter.constant.UserConstant;
import com.joae.usercenter.exception.BusinessException;
import com.joae.usercenter.model.domain.User;
import com.joae.usercenter.service.UserService;
import com.joae.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
 *
 * @author joae
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-12-31 20:59:39
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    /**
     * 盐值，混淆密码
     */

    private static final String SALT = "joae";

    /**
     * 用户注册
     * @param userAccount 账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @param studentNumber 学号
     * @return
     */
    @Override

    public long userRegister(String userAccount, String userPassword, String checkPassword,String studentNumber) {
        //校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,studentNumber)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户小于4为");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码小于8位");
        }
        if(studentNumber.length()!=6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"学号不对");
        }

//        不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }

        //密码与校验密码相等
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }
        // 学号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentNumber", studentNumber);
        count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }

        //加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        user.setStudentNumber(studentNumber);
        boolean saveResult = this.save(user);
        if(!saveResult){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8 ) {
            return null;
        }

//        不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        // 校验密码
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword",newPassword);
        User user = this.getOne(queryWrapper);
        if(user==null){
            log.info("Login failed, account can't match password");
            return null;
        }
        User safeUser = getSaftyUser(user);

        //记录用户的登录态
        request.getSession().setAttribute(UserConstant.LOGIN_STATE,safeUser);
        return safeUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSaftyUser(User originUser){
        //用户信息脱敏
        if(originUser==null){
            return null;
        }
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setUserStatus(0);
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setStudentNumber(originUser.getStudentNumber());
        return safeUser;
    }

    /**
     * 用户退出
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.LOGIN_STATE);
        return 1;
    }
}




