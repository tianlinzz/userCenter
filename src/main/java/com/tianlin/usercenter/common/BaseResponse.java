package com.tianlin.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础返回类
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable { // implements  实现接口，接口可以实现多个 而extends是继续类，类只可以继承一个
    private static final long serialVersionUID = 1940150484L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 错误描述信息
     */
    private String description;

    /**
     * 返回数据
     */
    private T data; // 泛型,可以是任意类型,提高代码的复用性

    // 构造函数，下面是构造函数的重载，构造函数的重载是指一个类中可以有多个构造函数，它们具有不同的参数列表，这样就可以通过不同的参数列表来区分到底要使用哪个构造函数。

    /**
     * 成功类 基础的构造函数，成功的 其它类直接调用这个构造函数
     * @param code
     * @param msg
     * @param data
     */
    public BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 错误类 基础的构造函数，失败的 其它类直接调用这个构造函数
     * @param code
     * @param msg
     * @param data
     * @param description
     */

    public BaseResponse(Integer code, String msg, T data, String description) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.description = description;
    }


    public BaseResponse(ErrorCode errorCode, String msg, T data ,String description) {
        this(errorCode.getCode(), msg, data, description);
    }

}
