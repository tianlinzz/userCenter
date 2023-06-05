package com.tianlin.usercenter.exception;


import com.tianlin.usercenter.common.ErrorCode;

/**
 * 自定义异常
 *
 * @author 张添琳
 */
public class BusinessException extends RuntimeException {
    private final Integer code; // final修饰的变量，只能赋值一次，不能修改

    private final String description;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
