package com.wuhan.dict.common.bean;

import java.io.Serializable;

// Serializable indicates that objects of this class can be converted into a stream of bytes
public class OpLogBean implements Serializable {
    // Type of operation (e.g., "add", "update", "delete")
    private String opType;

    // Result of the operation
    private String opResult;

    // Constructor
    public OpLogBean(String opType, String opResult) {
        this.opType = opType;
        this.opResult = opResult;
    }

    // Getter for the operation type
    public String getOpType() {
        return opType;
    }

    // Setter for the operation type
    public void setOpType(String opType) {
        this.opType = opType;
    }

    // Getter for the operation result
    public String getOpResult() {
        return opResult;
    }

    // Setter for the operation result
    public void setOpResult(String opResult) {
        this.opResult = opResult;
    }
}
