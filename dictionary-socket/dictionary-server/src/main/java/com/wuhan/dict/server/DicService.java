package com.wuhan.dict.server;

import com.wuhan.dict.common.bean.ActionBean;
import com.wuhan.dict.common.bean.DicBean;
import com.wuhan.dict.common.bean.Result;
import com.wuhan.dict.common.constant.Constant;
import com.wuhan.dict.common.exception.DicException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

// Service class for dictionary operations
public class DicService {

    public static final String DIC_DAT = "dic.dat";

    // Constructor
    public DicService() {
        // Check if the data file exists; if not, create a new one
        File file = new File(DIC_DAT);
        if (!file.exists()) {
            try (FileWriter currentFile = new FileWriter(DIC_DAT)) {
                // If the file doesn't exist, create a new map and save it to the file
                Map<String, DicBean> map = new HashMap<>();
                DicTools.saveMapToFile(map, DIC_DAT);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error creating dic file...");
            }
        }
    }

    // Method to get all data from the dictionary
    public String getAllData() {
        Result<Object> result = null;
        try {
            Map<String, DicBean> dataMap = DicTools.loadMapFromFile(DIC_DAT);
            result = Result.ok(new ArrayList<>(dataMap.values()));
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Result.failed(500, ex.getMessage());
        }
        return new JSONObject(result).toString();
    }

    // Method to perform an action based on the input JSON string
    public String getActionResult(String actionJson) {
        Result<Object> result = null;
        try {
            DicBean data = null;
            JSONObject actionObj = new JSONObject(actionJson);
            String action = actionObj.getString(Constant.FIELD_ACTION);
            ActionBean actionBean = new ActionBean(action, actionObj.getString(Constant.FIELD_KEY),
                    actionObj.getString(Constant.FIELD_VALUE));
            if (Constant.ACTION_QUERY.equals(action)) {
                data = query(actionBean);
            } else if (Constant.ACTION_ADD.equals(action)) {
                data = add(actionBean);
            } else if (Constant.ACTION_UPDATE.equalsIgnoreCase(action)) {
                data = update(actionBean);
            } else if (Constant.ACTION_DELETE.equals(action)) {
                data = delete(actionBean);
            } else {
                throw new DicException("Invalid action!");
            }
            result = Result.ok(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Result.failed(500, ex.getMessage());
        }
        return new JSONObject(result).toString();
    }

    // Method to query a dictionary entry
    public DicBean query(ActionBean actionBean) throws IOException, ClassNotFoundException {
        Map<String, DicBean> dic = DicTools.loadMapFromFile(DIC_DAT);
        if (!dic.containsKey(actionBean.getKey())) {
            throw new DicException(Constant.MSG_ADD_NOTFOUND);
        }
        return dic.get(actionBean.getKey());
    }

    // Method to add a new dictionary entry
    public DicBean add(ActionBean actionBean) throws IOException, ClassNotFoundException {
        Map<String, DicBean> dic = DicTools.loadMapFromFile(DIC_DAT);
        if (dic.containsKey(actionBean.getKey())) {
            throw new DicException(Constant.MSG_ADD_REPEAT);
        }
        dic.put(actionBean.getKey(), new DicBean(actionBean.getKey(), actionBean.getValue()));
        DicTools.saveMapToFile(dic, DIC_DAT);
        return dic.get(actionBean.getKey());
    }

    // Method to update an existing dictionary entry
    public DicBean update(ActionBean actionBean) throws IOException, ClassNotFoundException {
        Map<String, DicBean> dic = DicTools.loadMapFromFile(DIC_DAT);
        if (!dic.containsKey(actionBean.getKey())) {
            throw new DicException(Constant.MSG_ADD_NOTFOUND);
        }
        List<String> newList = Arrays.asList(actionBean.getValue().split(","));
        List<String> oldList = Arrays.asList(dic.get(actionBean.getKey()).getValue().split(","));
        StringBuilder sb = new StringBuilder(dic.get(actionBean.getKey()).getValue());
        for (String newVal : newList) {
            if (!oldList.contains(newVal)) {
                sb.append(",").append(newVal);
            }
        }
        dic.get(actionBean.getKey()).setValue(sb.toString());
        DicTools.saveMapToFile(dic, DIC_DAT);
        return dic.get(actionBean.getKey());
    }

    // Method to delete a dictionary entry
    public DicBean delete(ActionBean actionBean) throws IOException, ClassNotFoundException {
        Map<String, DicBean> dic = DicTools.loadMapFromFile(DIC_DAT);
        if (!dic.containsKey(actionBean.getKey())) {
            throw new DicException(Constant.MSG_ADD_NOTFOUND);
        }
        DicBean bean = dic.get(actionBean.getKey());
        dic.remove(actionBean.getKey());
        DicTools.saveMapToFile(dic, DIC_DAT);
        return bean;
    }
}
