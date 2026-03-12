package com.example.demo.common;

/**
 * 统一响应结果类
 * 用于封装API接口的返回数据
 * @param <T> 数据类型
 */
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据内容
     */
    private T data;

    /**
     * 无参构造方法
     */
    public Result() {
    }

    /**
     * 全参构造方法
     * @param code 状态码
     * @param msg 提示信息
     * @param data 数据内容
     */
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功响应
     * @param data 返回的数据
     * @param <T> 数据类型
     * @return 成功的结果对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 成功响应（无数据）
     * @return 成功的结果对象
     */
    public static Result<Void> success() {
        return new Result<>(200, "操作成功", null);
    }

    /**
     * 失败响应
     * @param code 错误码
     * @param msg 错误信息
     * @return 失败的结果对象
     */
    public static Result<Void> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * 获取状态码
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置状态码
     * @param code 状态码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取提示信息
     * @return 提示信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置提示信息
     * @param msg 提示信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取数据内容
     * @return 数据内容
     */
    public T getData() {
        return data;
    }

    /**
     * 设置数据内容
     * @param data 数据内容
     */
    public void setData(T data) {
        this.data = data;
    }
}
