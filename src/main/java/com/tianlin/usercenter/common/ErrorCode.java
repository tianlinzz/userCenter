package com.tianlin.usercenter.common;

/**
 * 全局错误码
 * @author 张添琳
 */
public enum ErrorCode {
    SUCCESS(0, "success", ""),
    PARAMS_ERROR(40000, "参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统错误", "");

    private final Integer code;

    /**
     * 状态码信息
     */
    private final String msg;

    /**
     * 状态码描述
     */
    private final String description;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }

    ErrorCode(Integer code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }
}
