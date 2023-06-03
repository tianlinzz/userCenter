package com.tianlin.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianlin.usercenter.model.domain.User;
import com.tianlin.usercenter.model.domain.request.UserLoginRequest;
import com.tianlin.usercenter.model.domain.request.UserRegisterRequest;
import com.tianlin.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.tianlin.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.tianlin.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * 用户接口
 *
 * @author 天琳
 */
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
        if (userAccount == null || userPassword == null || checkPassword == null) {
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
        if (userAccount == null || userPassword == null) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/list")
    public List<User> userList(String username, HttpServletRequest request) {
        // 鉴权
        if (isAdmin(request)) {
            return new ArrayList<>();
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        return userService.list(queryWrapper);
    }

    @PostMapping("/delete")
    public boolean userDelete(@RequestBody Long id,HttpServletRequest request) {
        // 鉴权
        if (isAdmin(request)) {
            return false;
        }

        if (id < 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 判断是否为管理员
     *
     * @param request 请求
     * @return 是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        System.out.println(userObj);
        User user = (User) userObj;
        return user == null || user.getUserRole() != ADMIN_ROLE; // 不是管理员且未登录
    }
}
