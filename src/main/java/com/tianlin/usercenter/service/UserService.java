package com.tianlin.usercenter.service;

import com.tianlin.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 天琳
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-06-02 19:34:35
*/
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取安全用户信息
     *
     * @param originUser 原始用户信息
     * @return 安全用户信息
     */
    User getSafetUser(User originUser);
}
