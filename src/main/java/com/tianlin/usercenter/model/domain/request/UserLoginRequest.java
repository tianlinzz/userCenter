package com.tianlin.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 用户登录请求
 *
 * @author 天琳
 */

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 21412312L;

    private String userAccount;

    private String userPassword;
}
