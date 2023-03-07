package com.lightcurriculum.spider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lightcurriculum.pojo.StudentEntity;
import com.lightcurriculum.service.StudentService;
import com.lightcurriculum.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

@Component
public class StudentSpiderCache {
    private Map<String, StudentInfoSpider> spiderMap = new Hashtable<>();
    @Resource
    private StudentService studentService;

    @Resource
    RedisUtil redisUtil;

    public void addSpider(StudentEntity entity,StudentInfoSpider spider){
        spiderMap.put(entity.getOpenid(), spider);
        redisUtil.set(entity.getOpenid(),entity.getStudentId()+":"+entity.getStudentPassword(),60 * 10);
    }

    public StudentInfoSpider getStudentInfoSpider(String openid) {
        String idAndPassword = (String) redisUtil.get(openid);
        if(idAndPassword == null){
            spiderMap.remove(openid);
            StudentEntity student = studentService.getOne(new QueryWrapper<StudentEntity>().eq("openid", openid));
            if(student == null){
                return null;
            }
            StudentInfoSpider spider = new StudentInfoSpider();
            if(spider.login(student.getStudentId(), student.getStudentPassword())){
                addSpider(student,spider);
                return spider;
            }
        }
        return spiderMap.get(openid);
    }
}
