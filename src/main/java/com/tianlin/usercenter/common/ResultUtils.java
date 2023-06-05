package com.tianlin.usercenter.common;

/**
 * 返回结果工具类
 *
 * @author 张添琳
 */

public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, "success", data);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param <T>
     * @return
     */

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String msg, String description) {
        return new BaseResponse<>(errorCode, msg, null , description);
    }

    public static <T> BaseResponse<T> error(Integer errorCode, String msg, String description) {
        return new BaseResponse<>(errorCode, msg, null, description);
    }
}
