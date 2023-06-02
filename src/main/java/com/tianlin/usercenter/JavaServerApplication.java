package com.tianlin.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tianlin.usercenter.mappers")
public class JavaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaServerApplication.class, args);
    }

}
