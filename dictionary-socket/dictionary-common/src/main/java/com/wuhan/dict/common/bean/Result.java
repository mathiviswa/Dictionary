package com.wuhan.dict.common.bean;

import java.io.Serializable;

// Serializable indicates that objects of this class can be converted into a stream of bytes
public class Result<T> implements Serializable {
    // HTTP status code
    private int code;

    // Message associated with the result
    private String msg;

    // Data associated with the result
    private T data;

    // Default constructor (protected to prevent direct instantiation)
    protected Result() {
    }

    // Parameterized constructor
    protected Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // Factory method to create a successful result with no message and no data
    public static <T> Result<T> ok() {
        return new Result<T>(200, "success", null);
    }

    // Factory method to create a successful result with a custom message and no data
    public static <T> Result<T> ok(String message) {
        return new Result<T>(200, message, null);
    }

    // Factory method to create a successful result with data and a default success message
    public static <T> Result<T> ok(T data) {
        return new Result<T>(200, "success", data);
    }

    // Factory method to create a failed result with a custom code and message
    public static <T> Result<T> failed(int code, String message) {
        return new Result<T>(code, message, null);
    }

    // Getter for the HTTP status code
    public int getCode() {
        return code;
    }

    // Setter for the HTTP status code
    public void setCode(int code) {
        this.code = code;
    }

    // Getter for the message
    public String getMsg() {
        return msg;
    }

    // Setter for the message
    public void setMsg(String msg) {
        this.msg = msg;
    }

    // Getter for the data
    public T getData() {
        return data;
    }

    // Setter for the data
    public void setData(T data) {
        this.data = data;
    }
}
