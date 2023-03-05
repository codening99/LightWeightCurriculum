package com.lightcurriculum.lightcurriculum.Controller;

import com.lightcurriculum.lightcurriculum.ErrorCode;
import com.lightcurriculum.lightcurriculum.ResponseData;
import com.lightcurriculum.lightcurriculum.spider.JSessionRequest;
import com.lightcurriculum.lightcurriculum.spider.StudentInfoSpider;
import okhttp3.Response;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final JSessionRequest request = new JSessionRequest();

    private Map<String,String> users = new HashMap<>();
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
        //FIXME 检索数据库 判断用户是否已经绑定过
        if(users.containsKey(openid)){
            responseData.putStatus(ErrorCode.ALREADY_BIND);
        }
        else{
            StudentInfoSpider spider = new StudentInfoSpider(username,password);
            if(!spider.login()){
                responseData.putStatus(ErrorCode.BIND_ERROR);
            }else{
                users.put(openid,username + " " + password);
                responseData.putStatus(ErrorCode.BIND_SUCCESS);
            }
        }
        return responseData.data();
    }

    @RequestMapping("/checkBind")
    public Map<String,Object> bindAccount(@RequestParam("openid") String openid){
        ResponseData responseData = new ResponseData();
        //FIXME 去数据库查询完成判断
        if(users.containsKey(openid)) {
            responseData.putStatus(ErrorCode.ALREADY_BIND);
        }else{
            responseData.putStatus(ErrorCode.NOT_BIND);
        }
        return responseData.data();
    }

    //FIXME use openid or account ?
    @RequestMapping("/queryCourse")
    public Map<String,Object> queryCourse(@RequestParam("username") String username,@RequestParam("password")String password) throws IOException {
        //FIXME 检验
        ResponseData responseData = new ResponseData();
        StudentInfoSpider spider = new StudentInfoSpider(username,password);
        if(!spider.login()) {
            responseData.putStatus(ErrorCode.REMOTE_LOGIN_ERROR);
        }else{
            responseData.putStatus(ErrorCode.SUCCESS_FETCH_COURSES);
            responseData.putData(spider.getCurriculum());
        }
        return responseData.data();
    }

}
