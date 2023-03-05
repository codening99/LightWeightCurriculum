package com.lightcurriculum.spider;

import com.lightcurriculum.pojo.Curriculum;
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

    private static final String CURRICULUM_URL = "http://yjs.imu.edu.cn/ssfw/pygl/xkgl/xskb/query.do";
    private final static String SCORES_REPORT_URL = "http://yjs.imu.edu.cn/ssfw/pygl/cjgl/cjcx.do";
    private JSessionRequest request;

    private final String studentId;

    private final String password; //not encrypted

    private boolean isLogin = false;
    private static Map<String, String> semesters;       // 学生的所有可选学期
    private final List<ScoreEntity> all_scores = new ArrayList<>();


    public StudentInfoSpider(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
        request = new JSessionRequest();
    }

    class LoginForm {
        public static final String LT_FILED = "<input type=\"hidden\" name=\"lt\" value=\"";
        public static final String SALT_FIELD = "<input type=\"hidden\" id=\"pwdDefaultEncryptSalt\" value=\"";
        public static final String RMSHOWN_FILED = "<input type=\"hidden\" name=\"rmShown\" value=\"";
        public static final String EVENTID_FIELD = "<input type=\"hidden\" name=\"_eventId\" value=\"";
        public static final String DLLT_FIELD = "<input type=\"hidden\" name=\"dllt\" value=\"";
        public static final String EXECUTION_FIELD = "<input type=\"hidden\" name=\"execution\" value=\"";

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
            String salt = getFieldValue(content, LoginForm.SALT_FIELD);
            parameters.put("lt", getFieldValue(content, LoginForm.LT_FILED));
            parameters.put("dllt", getFieldValue(content, LoginForm.DLLT_FIELD));
            parameters.put("execution", getFieldValue(content, LoginForm.EXECUTION_FIELD));
            parameters.put("_eventId", getFieldValue(content, LoginForm.EVENTID_FIELD));
            parameters.put("rmShown", getFieldValue(content, LoginForm.RMSHOWN_FILED));
            parameters.put("username", studentId);
            parameters.put("password", aes.encrypt(password, salt));
            Response res = request.post(LOGIN_URL, parameters);
            if(res.body().string().contains("欢迎您")){
                isLogin = true;
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 获取所有可选学期
     *
     */
    private void getSemesters() throws IOException {
        assert(isLogin);
        semesters = new HashMap<>();
        Response response = request.get(CURRICULUM_URL);
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


    public void getScoreReport() throws IOException {
        assert(isLogin);
        Response response = request.get(SCORES_REPORT_URL);
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
            all_scores.add(score);
        }
    }

    //FIXME 动态SemesterID

    public List<Curriculum> getCurriculum(String semesterId) throws IOException {
        assert(isLogin);
        Map<String,String> parameters = new HashMap<>();
        parameters.put("xnxqdm", semesterId);
        parameters.put("excel","true");
        Response response = request.post(CURRICULUM_URL, parameters);
        String content = response.body().string();
        Document parse = Jsoup.parse(content);
        Elements trs = parse.getElementsByTag("tr");
        List<Curriculum> list = new ArrayList<>();
        for (org.jsoup.nodes.Element tr : trs) {
            Elements tds = tr.getElementsByTag("td");
            for(int i = 0; i < tds.size(); ++i){
                String text = tds.get(i).text();
                String[] split = text.split(" ");
                if(split.length == 5){
                    Curriculum curriculum = new Curriculum(split,i - 1);
                    list.add(curriculum);
                }
            }
        }
        return list;
    }

}
