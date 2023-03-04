package com.lightcurriculum.spider;

import com.lightcurriculum.pojo.ScoreEntity;
import com.lightcurriculum.utils.AESUtil;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StudentInfoSpider {

    private final static String LOGIN_URL = "http://cer.imu.edu.cn/authserver/login?service=http://yjs.imu.edu.cn/ssfw/login_cas.jsp";
    private final static String CURRICULUM_URL = "http://yjs.imu.edu.cn/ssfw/pygl/xkgl/xskb.do";
    private final static String QUERY_CURRICULUM_URL = "http://yjs.imu.edu.cn/ssfw/pygl/xkgl/xskb/query.do";
    private final static String SCORES_REPORT_URL = "http://yjs.imu.edu.cn/ssfw/pygl/cjgl/cjcx.do";
    private final JSessionRequest REQUEST;
    private final String STUDENT_ID;
    private final String PASSWORD; //not encrypted

    private static Map<String, String> semesters;       // 学生的所有可选学期
    private static Map<String, String> curriculumPages;        // 学生所有可选学期对应的页面
    private static final List<ScoreEntity> ALL_SCORES = new ArrayList<>();


    public StudentInfoSpider(String studentId, String studentPWD) {
        this.STUDENT_ID = studentId;
        this.PASSWORD = studentPWD;
        REQUEST = new JSessionRequest();
    }

    static class LoginForm {
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
        System.out.println(LOGIN_URL);
        try {
            Response response = REQUEST.get(LOGIN_URL);
            assert response.body() != null;
            String content = response.body().string();
//            System.out.println(content);
            Map<String, String> parameters = new HashMap<>();
            AESUtil aes = new AESUtil();

            //salt use for encrypt password
            String salt = getFieldValue(content, LoginForm.saltField);
            parameters.put("lt", getFieldValue(content, LoginForm.ltFiled));
            parameters.put("dllt", getFieldValue(content, LoginForm.dlltField));
            parameters.put("execution", getFieldValue(content, LoginForm.executionField));
            parameters.put("_eventId", getFieldValue(content, LoginForm.eventIdField));
            parameters.put("rmShown", getFieldValue(content, LoginForm.rmShownFiled));
            parameters.put("username", STUDENT_ID);
            parameters.put("password", aes.encrypt(PASSWORD, salt));
            Response res = REQUEST.post(LOGIN_URL, parameters);
            assert res.body() != null;
            System.out.println(res.body().string());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取所有可选学期
     *
     */
    private void getSemesters() throws IOException {
        semesters = new HashMap<>();
        Response response = REQUEST.get(CURRICULUM_URL);
        assert response.body() != null;
        String curriculumHtml = response.body().string();
        Document currParse = Jsoup.parse(curriculumHtml);
        Elements options = currParse.getElementsByTag("option");
        for (Element e : options) {
            if (e.attr("value").equals(""))
                continue;
            semesters.put(e.text(), e.attr("value"));
        }
    }

    /**
     * 获取所有学期课表界面
     *
     */
    private void getCurriculumPages() throws IOException {
        // 获取所有学期
        this.getSemesters();
        // POST请求参数
        Map<String, String> parameters = new HashMap<>();
        for (Map.Entry<String, String> stringStringEntry : semesters.entrySet()) {
            parameters.clear();
            parameters.put("xqdm", stringStringEntry.getValue());
            Response curriculum = REQUEST.post(QUERY_CURRICULUM_URL, parameters);
            assert curriculum.body() != null;
            curriculumPages.put(stringStringEntry.getKey(), curriculum.body().string());
        }
    }

    /**
     * 通过学期名称返回该学期的所有课表
     *
     * @param semesterName 学期名称
     */
    public void getAllCurriculumBySemesterName(String semesterName) {
        String page = curriculumPages.getOrDefault(semesterName, null);
        if (page == null) {
            System.out.println("Error, 请求的学期不存在");
            return;
        }
        Document pageParse = Jsoup.parse(page);
    }

    public void getScoreReport() throws IOException {
        Response response = REQUEST.get(SCORES_REPORT_URL);
        assert response.body() != null;
        String scoreReportHtml = response.body().string();
        Document scoreParse = Jsoup.parse(scoreReportHtml);
        for (Element t_con : scoreParse.getElementsByClass("t_con")) {
            ScoreEntity score;
            Map<Integer, String> scoreMap = new HashMap<>();
            for (int i = 0; i < t_con.childrenSize() - 1; i++) {
                scoreMap.put(i, t_con.child(i).text());
            }
            score = new ScoreEntity(scoreMap);
            ALL_SCORES.add(score);
        }

//        for (ScoreEntity allScore : ALL_SCORES) {
//            System.out.println(allScore);
//        }

    }

}
