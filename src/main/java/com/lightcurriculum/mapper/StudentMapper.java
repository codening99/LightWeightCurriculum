package com.lightcurriculum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lightcurriculum.pojo.StudentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<StudentEntity> {
}
