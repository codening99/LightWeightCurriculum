package com.lightcurriculum.lightcurriculum.domain;

import java.util.Map;

/**
 * @ClassName ScoreEntity
 * @Description 成绩实体类
 * @Author codening
 * @Date 2023/3/2 21:54
 */
public class ScoreEntity {
    private String courseType;
    private String courseId;
    private String courseName;
    private String credit;
    private String examScore;
    private String examPass;
    private String examNature;
    private String examDate;

    @Override
    public String toString() {
        return "ScoreEntity{" +
                "courseType='" + courseType + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credit='" + credit + '\'' +
                ", examScore='" + examScore + '\'' +
                ", examPass='" + examPass + '\'' +
                ", examNature='" + examNature + '\'' +
                ", examDate='" + examDate + '\'' +
                '}';
    }

    public ScoreEntity(String courseType, String courseId, String courseName, String credit, String examScore, String examPass, String examNature, String examDate) {
        this.courseType = courseType;
        this.courseId = courseId;
        this.courseName = courseName;
        this.credit = credit;
        this.examScore = examScore;
        this.examPass = examPass;
        this.examNature = examNature;
        this.examDate = examDate;
    }

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

    public ScoreEntity() {
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getExamScore() {
        return examScore;
    }

    public void setExamScore(String examScore) {
        this.examScore = examScore;
    }

    public String getExamPass() {
        return examPass;
    }

    public void setExamPass(String examPass) {
        this.examPass = examPass;
    }

    public String getExamNature() {
        return examNature;
    }

    public void setExamNature(String examNature) {
        this.examNature = examNature;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }
}
