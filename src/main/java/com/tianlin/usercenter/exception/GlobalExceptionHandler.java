package com.tianlin.usercenter.exception;

import com.tianlin.usercenter.common.BaseResponse;
import com.tianlin.usercenter.common.ErrorCode;
import com.tianlin.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * Spring AOP, 拦截所有controller
 *
 * @author 张添琳
 */
@RestControllerAdvice // 全局异常处理 Spring AOP, 拦截所有controller
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public <T> BaseResponse<T> businessExceptionHandler(BusinessException e) {
        log.error("businessException" + e.getMessage(), e); // 通过日志，打印异常信息
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public <T> BaseResponse<T> businessExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e); // 通过日志，打印异常信息
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "系统异常");
    }
}
