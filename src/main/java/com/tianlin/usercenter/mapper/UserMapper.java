package com.tianlin.usercenter.mapper;

import com.tianlin.usercenter.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 天琳
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2023-06-02 19:34:35
* @Entity generator.domain.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {

}




