package com.lzc.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Liang Zhancheng
 * @description 该类为 JSON 辅助类，返回格式化后的 JSON 串
 */
@Component
public final class JsonUtils {

    private static final String STATUE_KEY = "status";
    private static final String MESSAGE_KEY = "message";
    private static final String DATA_KEY = "data";
    private static final Pattern PATTERN = Pattern.compile("[0-9]+");

    public static final String SUCCESS_STRING = "success";
    public static final String FAIL_STRING = "fail";
    public static final String TRUE_STRING = "true";
    public static final String FALSE_STRING = "false";

    /**
     * @param str 输入字符串
     * @return boolean
     * @description 判断是不是数字
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        Matcher isNum = PATTERN.matcher(str);
        return !isNum.matches();
    }

    /**
     * @param status statusCode
     * @param message 信息
     * @return JSONString
     * @description 当不需要返回data时用该方法
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
