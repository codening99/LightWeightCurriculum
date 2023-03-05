package com.lightcurriculum.controller;

import com.lightcurriculum.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName StudentController
 * @Description TODO
 * @Author codening
 * @Date 2023/3/3 21:49
 */
@RestController
public class StudentController {

    @Autowired(required = false)
    private StudentService studentService;

    @RequestMapping("/login")
    public void insertInfo(String id, String pwd) {
//        StudentEntity student = new StudentEntity(id, pwd);
//        studentService.save(student);
    }
}
