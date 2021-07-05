package com.lzc.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Liang Zhancheng
 * @description 该类为 JSON 辅助类，返回格式化后的 JSON 串
 */
@Component
public final class JsonUtil {

    private static final String STATUE_KEY = "status";
    private static final String MESSAGE_KEY = "message";
    private static final String DATA_KEY = "data";

    /**
     * @description 判断是不是数字
     * @param str 输入字符串
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * @description 当不需要返回data时用该方法
     * @param status
     * @param message
     * @return JSONString
     */
    public static String toJsonString(Integer status, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(STATUE_KEY, status);
        jsonObject.put(MESSAGE_KEY, message);

        return jsonObject.toJSONString();
    }

    /**
     * @param data    JSONObject
     * @param status  状态码
     * @param message 信息 success/fail
     * @return Json -> String
     */
    public static String toJsonString(Integer status, String message, JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(STATUE_KEY, status);
        jsonObject.put(MESSAGE_KEY, message);
        jsonObject.put(DATA_KEY, data);

        return jsonObject.toJSONString();
    }

    /**
     * @param data: Map
     * @return Json -> String
     */
    public static String toJsonString(Integer status, String message, Map<String, Object> data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(STATUE_KEY, status);
        jsonObject.put(MESSAGE_KEY, message);
        jsonObject.put(DATA_KEY, new JSONObject(data));

        return jsonObject.toJSONString();
    }

    /**
     * @param data: List
     * @return Json -> String
     */
    public static String toJsonString(Integer status, String message, List data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(STATUE_KEY, status);
        jsonObject.put(MESSAGE_KEY, message);
        jsonObject.put(DATA_KEY, data);

        return jsonObject.toJSONString();
    }

    /**
     * @return JSONObject
     */
    public static JSONObject toJson(Integer status, String message, JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(STATUE_KEY, status);
        jsonObject.put(MESSAGE_KEY, message);
        jsonObject.put(DATA_KEY, data);

        return jsonObject;
    }
}
