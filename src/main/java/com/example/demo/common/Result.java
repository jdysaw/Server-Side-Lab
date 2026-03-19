package com.example.demo.common;

/**
 * 统一返回前端的数据外壳
 * 泛型载荷
 * 
 * @param <T> 数据类型
 */
public class Result<T> {
    private String msg;
    private Integer code;
    private T data;

    /**
     * 静态工厂方法:成功回调
     * 
     * @param data 成功时返回的数据载荷
     * @param <T>  数据类型
     * @return 统一响应结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = ResultCode.SUCCESS.getCode();
        result.msg = ResultCode.SUCCESS.getMsg();
        result.data = data;
        return result;
    }

    /**
     * 静态工厂方法:失败回调
     * 
     * @param resultCode 业务错误枚举
     * @param <T>        数据类型
     * @return 统一响应结果
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.code = resultCode.getCode();
        result.msg = resultCode.getMsg();
        result.data = null;
        return result;
    }

    /**
     * 静态工厂方法:失败回调 (自定义错误码和信息)
     * 
     * @param code 状态码
     * @param msg  提示信息
     * @param <T>  数据类型
     * @return 统一响应结果
     */
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        result.data = null;
        return result;
    }

    /**
     * 获取提示信息
     * 
     * @return 提示信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置提示信息
     * 
     * @param msg 提示信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取状态码
     * 
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置状态码
     * 
     * @param code 状态码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取数据内容
     * 
     * @return 数据内容
     */
    public T getData() {
        return data;
    }

    /**
     * 设置数据内容
     * 
     * @param data 数据内容
     */
    public void setData(T data) {
        this.data = data;
    }
}