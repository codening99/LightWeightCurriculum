package com.lightcurriculum.pojo;

import java.util.HashMap;


public class ResponseData {
    private HashMap<String, Object> response = new HashMap<>();

    private ResponseData put(String key, Object value) {
        response.put(key, value);
        return this;
    }

    public ResponseData putStatus(HashMap.Entry<Integer, String> code) {
        put("code", code.getKey());
        put("msg", code.getValue());
        return this;
    }

    public ResponseData putData(Object data) {
        return put("data", data);
    }

    public HashMap<String, Object> data(){
        return response;
    }

}
