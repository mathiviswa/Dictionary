package com.wuhan.dict.common.bean;

import java.io.Serializable;

// Serializable indicates that objects of this class can be converted into a stream of bytes
public class ActionBean implements Serializable {
    // Action to be performed (e.g., "add", "update", "delete")
    private String action;

    // Key associated with the action
    private String key;

    // Value associated with the action
    private String value;

    // Constructor
    public ActionBean(String action, String key, String value) {
        this.action = action;
        this.key = key;
        this.value = value;
    }

    // Getter for the key
    public String getKey() {
        return key;
    }

    // Setter for the key
    public void setKey(String key) {
        this.key = key;
    }

    // Getter for the value
    public String getValue() {
        return value;
    }

    // Setter for the value
    public void setValue(String value) {
        this.value = value;
    }

    // Getter for the action
    public String getAction() {
        return action;
    }

    // Setter for the action
    public void setAction(String action) {
        this.action = action;
    }
}
