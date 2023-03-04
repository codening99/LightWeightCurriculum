package com.lightcurriculum.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 将Map转为Json
 *
 * @ClassName JsonPack
 * @Description TODO
 * @Author codening
 * @Date 2023/3/4 12:56
 */
@Data
public class JsonPack {
    Map<String, Object> jsonMap;
    public JsonPack() {
        this.jsonMap = new LinkedHashMap<>();
    }

    public void put(String name, Object obj) {
        jsonMap.put(name, obj);
    }

    public static String pack(Map<String, Object> map){
        JSONObject json = new JSONObject();
        for (String s : map.keySet()) {
            Object obj = map.get(s);
            json.put(s, obj);
        }
        return json.toString();
    }

    public String toJson(){
        return JsonPack.pack(jsonMap);
    }

}
