package com.wuhan.dict.common.constant;

import com.wuhan.dict.common.bean.DicBean;

import java.util.ArrayList;
import java.util.List;

// This class holds constant values used throughout the application
public class Constant {

    // Messages for add operation
    public static final String MSG_ADD_SUCCESS  = "Successfully Added!";
    public static final String MSG_ADD_REPEAT   = "The Word already exists.";
    public static final String MSG_ADD_NOTFOUND = "The Word does not exist.";

    // Actions
    public static final String ACTION_QUERY  = "query";
    public static final String ACTION_ADD    = "add";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";

    // Fields for JSON objects
    public static final String FIELD_KEY    = "key";
    public static final String FIELD_VALUE  = "value";
    public static final String FIELD_ACTION = "action";

    // Fields for operation log
    public static final String FIELD_OP_TYPE    = "op_type";
    public static final String FIELD_OP_RESULT  = "op_result";

    // Fields for result JSON objects
    public static final String RESULT_CODE = "code";
    public static final String RESULT_MSG = "msg";
    public static final String RESULT_DATA = "data";
}
