package com.wuhan.dict.common.bean;

import java.io.Serializable;

// Serializable indicates that objects of this class can be converted into a stream of bytes
public class DicBean implements Serializable {
    // Key of the dictionary entry
    private String key;

    // Value associated with the key
    private String value;

    // Constructor
    public DicBean(String key, String value) {
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
}
