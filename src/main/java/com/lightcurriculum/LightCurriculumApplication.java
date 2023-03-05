package com.lightcurriculum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.lightcurriculum.mapper")
public class LightCurriculumApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightCurriculumApplication.class, args);
    }

}
