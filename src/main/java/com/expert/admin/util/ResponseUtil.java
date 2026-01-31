package com.expert.admin.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    
    public static <T> Map<String, Object> success(T data) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        return result;
    }
    
    public static Map<String, Object> success() {
        return success(null);
    }
    
    public static Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", message);
        result.put("data", null);
        return result;
    }
    
    public static Map<String, Object> error(int code, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        result.put("data", null);
        return result;
    }
}
