package com.wuhan.dict.common.exception;

// Custom exception class for dictionary-related exceptions
public class DicException extends RuntimeException {
    // Constructor with message
    public DicException(String msg) {
        super(msg);
    }

    // Constructor with message and cause
    public DicException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
