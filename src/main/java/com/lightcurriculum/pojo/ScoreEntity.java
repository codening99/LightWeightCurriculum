package com.lightcurriculum.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @ClassName ScoreEntity
 * @Description 成绩实体类
 * @Author codening
 * @Date 2023/3/2 21:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreEntity {
    private String courseType;
    private String courseId;
    private String courseName;
    private String credit;
    private String examScore;
    private String examPass;
    private String examNature;
    private String examDate;


    public ScoreEntity(Map<Integer, String> map) {
        this.courseType = map.get(0);
        this.courseId = map.get(1);
        this.courseName = map.get(2);
        this.credit = map.get(3);
        this.examScore = map.get(4);
        this.examPass = map.get(5);
        this.examNature = map.get(6);
        this.examDate = map.get(7);
    }


}
