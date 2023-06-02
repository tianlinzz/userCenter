package com.tianlin.usercenter;

import com.tianlin.usercenter.mappers.UserMapper;
import com.tianlin.usercenter.models.User;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test; // 如果直接用junit得test注解，会报错，需要用junit.jupiter.api.Test的注解
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assertions.assertEquals(5, userList.size()); // 断言，如果不等于5，就会报错.
        userList.forEach(System.out::println);
    }

}