package com.lightcurriculum.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
    private String studentId;
    @TableField("student_pwd")
    private String studentPassword;
    @TableField("student_name")
    private String studentName;

    @TableField("openid")
    private String openid;

    public StudentEntity(String studentId, String studentPwd, String openid) {
        this.studentId = studentId;
        this.studentPassword = studentPwd;
        this.openid = openid;
    }
}
