package com.lightcurriculum.lightcurriculum.spider;

import com.lightcurriculum.lightcurriculum.utils.AESUtil;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StudentInfoSpider {
    private final String LOGIN_URL = "http://cer.imu.edu.cn/authserver/login?service=http://yjs.imu.edu.cn/ssfw/login_cas.jsp";
    private JSessionRequest request;
    private final String studentId;
    private final String password; //not encrypted

    public StudentInfoSpider(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
        request = new JSessionRequest();
    }

    class LoginForm {
        public static final String ltFiled = "<input type=\"hidden\" name=\"lt\" value=\"";
        public static final String saltField = "<input type=\"hidden\" id=\"pwdDefaultEncryptSalt\" value=\"";
        public static final String rmShownFiled = "<input type=\"hidden\" name=\"rmShown\" value=\"";
        public static final String eventIdField = "<input type=\"hidden\" name=\"_eventId\" value=\"";
        public static final String dlltField = "<input type=\"hidden\" name=\"dllt\" value=\"";
        public static final String executionField = "<input type=\"hidden\" name=\"execution\" value=\"";
    }

    private static String getFieldValue(String html, String field) {
        int index = html.indexOf(field) + field.length();
        return html.substring(index, html.indexOf("\"", index));
    }

    public boolean login() {
        try {
            Response response = request.get(LOGIN_URL);
            String content = response.body().string();
            Map<String, String> parameters = new HashMap<>();
            AESUtil aes = new AESUtil();
            //salt use for encrypt password
            String salt = getFieldValue(content, LoginForm.saltField);
            parameters.put("lt", getFieldValue(content, LoginForm.ltFiled));
            parameters.put("dllt", getFieldValue(content, LoginForm.dlltField));
            parameters.put("execution", getFieldValue(content, LoginForm.executionField));
            parameters.put("_eventId", getFieldValue(content, LoginForm.eventIdField));
            parameters.put("rmShown", getFieldValue(content, LoginForm.rmShownFiled));
            parameters.put("username", studentId);
            parameters.put("password", aes.encrypt(password, salt));
            System.out.println(parameters);
            Response res = request.post(LOGIN_URL, parameters);
            System.out.println(res.body().string());
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
