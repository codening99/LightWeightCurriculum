package com.lightcurriculum.lightcurriculum;

import com.lightcurriculum.lightcurriculum.spider.StudentInfoSpider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class LightCurriculumApplicationTests {

    @Test
    void contextLoads() throws IOException {
        StudentInfoSpider s = new StudentInfoSpider("32209094", "180015");
        String login = s.login();
//        s.getCurriculum(login);
//        s.getSemesters();
//        s.getCurriculumPages();
        s.getScoreReport();
    }

}
