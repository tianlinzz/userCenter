package com.tianlin.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tianlin.usercenter.common.BaseResponse;
import com.tianlin.usercenter.common.ErrorCode;
import com.tianlin.usercenter.common.ResultUtils;
import com.tianlin.usercenter.exception.BusinessException;
import com.tianlin.usercenter.model.domain.User;
import com.tianlin.usercenter.model.domain.request.UserLoginRequest;
import com.tianlin.usercenter.model.domain.request.UserRegisterRequest;
import com.tianlin.usercenter.service.UserService;
import com.tianlin.usercenter.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.tianlin.usercenter.constant.UserConstant.ADMIN_ROLE;


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

    /**
     * 判断是否为管理员
     *
     * @param user 当前用户
     * @return 是否为管理员
     */
    private boolean isAdmin(User user) {
        return user == null || user.getUserRole() != ADMIN_ROLE; // 不是管理员且未登录
    }

    /**
     * 获取当前用户
     *
     * @param request 请求
     * @return 当前用户
     */
    private User getCurrentUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token) || JwtUtil.isTokenExpired(token)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录过期");
        }
        Long id = JwtUtil.parseToken(token);
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        return user;
    }

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userCode = userRegisterRequest.getUserCode();

        if (userAccount == null || userPassword == null || checkPassword == null || userCode == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, userCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<String> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (userAccount == null || userPassword == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String result = userService.userLogin(userAccount, userPassword);
        return ResultUtils.success(result);
    }


    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> userCurrent(HttpServletRequest request) {
        User user = getCurrentUser(request);
        User result = userService.getSafetUser(user);
        return ResultUtils.success(result);
    }

    @GetMapping("/list")
    public BaseResponse<List<User>> userList(String username, HttpServletRequest request) {
        // 鉴权
        if (isAdmin(getCurrentUser(request))) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> result = userList.stream().map(user -> userService.getSafetUser(user)).collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(@RequestBody User body,HttpServletRequest request) {
        // 鉴权
        if (isAdmin(getCurrentUser(request))) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        Long id = body.getId();
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> userUpdate(@RequestBody User body,HttpServletRequest request) {
        // 鉴权
        if (isAdmin(getCurrentUser(request))) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        Long id = body.getId();
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        User userInfo = userService.checkUserInfo(body);
        boolean result = userService.updateById(userInfo);
        return ResultUtils.success(result);
    }

}
