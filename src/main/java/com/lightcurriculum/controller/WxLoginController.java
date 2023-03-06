package com.lightcurriculum.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lightcurriculum.pojo.ErrorCode;
import com.lightcurriculum.pojo.ResponseData;
import com.lightcurriculum.pojo.StudentEntity;
import com.lightcurriculum.service.StudentService;
import com.lightcurriculum.spider.JSessionRequest;
import com.lightcurriculum.spider.StudentInfoSpider;
import com.lightcurriculum.spider.StudentSpiderCache;
import jakarta.annotation.Resource;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WxLoginController {
    //FIXME 抽离为配置文件
    private final static String APPID = "wx38aee858af4c6b7e";
    private final static String SECRET = "c0f12ed0016428a1062b9dc6ac4b004e";
    private final static String GRANT_TYPE = "authorization_code";
    private final static String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    //the request only for wx login
    private final JSessionRequest request = new JSessionRequest();

    private static Logger logger = LoggerFactory.getLogger(StudentInfoSpider.class);

    @Resource
    StudentService studentService;
    @Resource
    StudentSpiderCache studentSpiderCache;

    @RequestMapping("/wxLogin")
    public String wxLogin(@RequestParam("code") String code) throws IOException {
        String url = WX_LOGIN_URL + "?appid=" + APPID + "&secret=" + SECRET + "&js_code=" + code + "&grant_type=" + GRANT_TYPE;
        Response response = request.get(url);
        return response.body().string();
    }

    //TODO 与数据库进行交互
    @RequestMapping("/bindAccount")
    public Map<String,Object> bindAccount(@RequestParam("openid") String openid, @RequestParam("username") String username, @RequestParam("password") String password) {
        ResponseData responseData = new ResponseData();
        StudentInfoSpider spider = new StudentInfoSpider();
        if(spider.login(username, password)){
            StudentEntity studentEntity = new StudentEntity(username,password,openid);
            //要考虑到用户重新绑定
            studentService.saveOrUpdate(studentEntity, new QueryWrapper<StudentEntity>().eq("openid", openid));
            studentSpiderCache.addSpider(studentEntity,spider);
            logger.info("bind success, openid: " + openid + " username: " + username);
            responseData.putStatus(ErrorCode.BIND_SUCCESS);
        }else{
            logger.info("bind failed, openid: " + openid + " username: " + username);
            responseData.putStatus(ErrorCode.BIND_ERROR);
        }
        return responseData.data();
    }

    @RequestMapping("/checkBind")
    public Map<String,Object> bindAccount(@RequestParam("openid") String openid){
        System.out.println(openid);
        ResponseData responseData = new ResponseData();
        StudentInfoSpider spider = studentSpiderCache.getStudentInfoSpider(openid);
        if(spider == null) {
            responseData.putStatus(ErrorCode.NOT_BIND);
        }else{
            responseData.putStatus(ErrorCode.ALREADY_BIND);
        }
        return responseData.data();
    }

    @RequestMapping("/queryCourse")
    public Map<String,Object> queryCourse(@RequestParam("openid") String openid) throws IOException {
        ResponseData responseData = new ResponseData();
        StudentInfoSpider spider = studentSpiderCache.getStudentInfoSpider(openid);
        //这种情况不应该出现
        if(spider == null) {
            logger.error("bad operation, the user has not bind the account but call the queryCourse api");
            responseData.putStatus(ErrorCode.REMOTE_LOGIN_ERROR);
        }else{
            //FIXME 按照指定课程Id进行检索
            responseData.putStatus(ErrorCode.SUCCESS_FETCH_COURSES);
            responseData.putData(spider.getCurriculum("43"));
        }
        return responseData.data();
    }
}
