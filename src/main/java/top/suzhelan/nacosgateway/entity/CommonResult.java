package top.suzhelan.nacosgateway.entity;

import com.alibaba.fastjson2.JSON;

public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>().setCode(200).setMessage("success").setData(data);
    }

    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<T>().setCode(200).setMessage(message).setData(data);
    }

    public static <T> CommonResult<T> error(String msg) {
        return new CommonResult<T>().setCode(500).setMessage(msg);
    }

    public static <T> CommonResult<T> error(Integer code, String msg) {
        return new CommonResult<T>().setCode(code).setMessage(msg);
    }

    public Integer getCode() {
        return code;
    }

    public CommonResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public CommonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String toJSON() {
        return JSON.toJSONString(this);
    }
}
