package com.lightcurriculum.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName StudentEntity
 * @Description TODO
 * @Author codening
 * @Date 2023/3/3 21:18
 */
@Data
@TableName("student")
@AllArgsConstructor
@NoArgsConstructor
public class StudentEntity {
    @TableId("id")
    private Integer id;
    @TableField("student_id")
    private String StudentId;
    @TableField("student_pwd")
    private String StudentPWD;
    @TableField("student_name")
    private String StudentName;

    public StudentEntity(String id, String pwd, String name) {
        StudentId = id;
        StudentPWD = pwd;
        StudentName = name;
    }
}
