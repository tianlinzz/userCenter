package com.tianlin.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianlin.usercenter.common.ErrorCode;
import com.tianlin.usercenter.exception.BusinessException;
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

    /**
     * @description 获取安全用户信息,不包含密码
     * @param originUser 原始用户信息
     * @return User
     */
    @Override
    public User getSafetUser(User originUser) {
        User safetyUser = new User();
        if (originUser == null) {
            return null;
        }
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserCode(originUser.getUserCode());
        return safetyUser;
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String userCode) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码不能为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度必须在4-16位之间");
        }
        if (userPassword.length() < 6 || userPassword.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度必须在6-20位之间");
        }
        // 用户编码转化成整数只能大于零
        int userCodeInt = Integer.parseInt(userCode);
        if (userCodeInt < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户编码必须大于零");
        }
        // 账号不能包含特殊字符,只能是字母数字下划线
        String reg = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(reg).matcher(userAccount);
        if (!matcher.find()) { // 如果包含特殊字符
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
        // 用户编码是能是数字得组合
        reg = "^[0-9]+$";
        matcher = Pattern.compile(reg).matcher(userCode);
        if (!matcher.find()) { // 如果包含不是数字的字符
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户编码只能是数字组合");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }
        // 判断账号是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        // 判断用户编码是否也存在了
        queryWrapper.clear();  // 清空条件
        queryWrapper.eq("userCode", userCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户编码已存在");
        }
        // 2.加密
        String newPassword = md5Password + userPassword + md5Password;
        String password = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        // 3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(password);
        user.setUserCode(userCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败");
        }
        return user.getId();
    }


    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码不能为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度必须在4-16位之间");
        }
        if (userPassword.length() < 6 || userPassword.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度必须在6-20位之间");
        }
        // 账号不能包含特殊字符,只能是字母数字下划线
        String reg = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(reg).matcher(userAccount);
        if (!matcher.find()) { // 如果包含特殊字符
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }
        // 3.返回用户信息,不包含密码
        User safetyUser = getSafetUser(user);
        // 4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);
        // 5.返回用户信息
        return getSafetUser(user);
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        // 1.清除session中的用户登录状态
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }

}




