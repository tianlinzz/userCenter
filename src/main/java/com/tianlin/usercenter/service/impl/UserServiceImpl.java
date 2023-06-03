package com.tianlin.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianlin.usercenter.mapper.UserMapper;
import com.tianlin.usercenter.model.domain.User;
import com.tianlin.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tianlin.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
* @author 天琳
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-06-02 19:34:35
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * md5加密密码
     */
    private static final String md5Password = "tianlin";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            return -1;
        }
        if (userPassword.length() < 6 || userPassword.length() > 20) {
            return -1;
        }
        // 账号不能包含特殊字符,只能是字母数字下划线
        String reg = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(reg).matcher(userAccount);
        if (!matcher.find()) { // 如果包含特殊字符
           return -1;
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 判断账号是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        // 2.加密
        String newPassword = md5Password + userPassword + md5Password;
        String password = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        // 3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(password);
        this.save(user);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }


    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            return null;
        }
        if (userPassword.length() < 6 || userPassword.length() > 20) {
            return null;
        }
        // 账号不能包含特殊字符,只能是字母数字下划线
        String reg = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(reg).matcher(userAccount);
        if (!matcher.find()) { // 如果包含特殊字符
            return null;
        }
        // 2.加密
        String newPassword = md5Password + userPassword + md5Password;
        String password = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        // 3.查询数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", password);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在或密码错误
        if (user == null) {
            log.info("user longin fail, userAccount can not find or userPassword error");
            return null;
        }
        // 3.返回用户信息,不包含密码
        User safetyUser = new User();
        System.out.println(user);
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUpdateTime(user.getUpdateTime());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setIsDelete(user.getIsDelete());
        // 4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);
        // 5.返回用户信息
        return safetyUser;
    }
}




