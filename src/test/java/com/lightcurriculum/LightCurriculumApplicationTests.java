package com.lightcurriculum;

import com.lightcurriculum.spider.StudentInfoSpider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class LightCurriculumApplicationTests {


    @Test
    void contextLoads() {
        StudentInfoSpider s = new StudentInfoSpider("32209094", "180015");
//        StudentInfoSpider s = new StudentInfoSpider();
//        System.out.println(port);
        s.login();
//        s.getCurriculum(login);
//        s.getSemesters();
//        s.getCurriculumPages();
//        s.getScoreReport();
    }

}
