package com.lightcurriculum.pojo;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {
    public final static HashMap.Entry<Integer,String> NOT_BIND = new HashMap.SimpleEntry<>(10086,"该微信未绑定账号");
    public final static HashMap.Entry<Integer,String> ALREADY_BIND = new HashMap.SimpleEntry<>(10087,"该微信已绑定账号");
    public final static HashMap.Entry<Integer,String> BIND_ERROR = new HashMap.SimpleEntry<>(10088,"绑定失败");
    public final static HashMap.Entry<Integer,String> BIND_SUCCESS = new HashMap.SimpleEntry<>(10089,"绑定成功");
    public final static HashMap.Entry<Integer,String> REMOTE_LOGIN_ERROR = new HashMap.SimpleEntry<>(10090,"登录失败");

    public final static HashMap.Entry<Integer,String> SUCCESS_FETCH_COURSES = new HashMap.SimpleEntry<>(10091,"成功获取课表信息");
}
