package com.tianlin.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianlin.usercenter.model.domain.User;
import com.tianlin.usercenter.service.UserService;
import com.tianlin.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 天琳
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-06-02 19:34:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




