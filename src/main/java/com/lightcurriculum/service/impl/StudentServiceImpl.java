package com.lightcurriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lightcurriculum.pojo.StudentEntity;
import com.lightcurriculum.service.StudentService;
import com.lightcurriculum.mapper.StudentMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName StudentServiceImpl
 * @Description TODO
 * @Author codening
 * @Date 2023/3/3 22:19
 */
@Service()
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentEntity> implements StudentService {
}
