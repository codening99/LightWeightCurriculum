package com.lightcurriculum.pojo;

public class Curriculum {
    //位压缩的方式存储课程的周数
    private Integer weekBits = 0;
    private String courseName;
    private String teacherName;
    private String classroom;
    private Integer day;//那一天的课
    private Integer courseStart;//第几节课开始
    private Integer courseEnd;//第几节课结束

    //通过description解析出来的课程信息
    public Curriculum(String[] courseInfo,int day) {
        this.day = day;
        //c[0] name
        //c[1] unused
        //c[2] teacher
        //c[3] room
        //c[4] time
        //parseCourse
        String name = courseInfo[0];
        int index = name.length() - 1;
        while (index >= 0 && !Character.isDigit(name.charAt(index))) {
            index--;
        }
        this.courseName = name.substring(index + 1);
        index = courseInfo[2].indexOf("：");
        this.teacherName = courseInfo[2].substring(index + 1, courseInfo[2].length() - 1);
        index = courseInfo[3].indexOf("：");
        this.classroom = courseInfo[3].substring(index + 1, courseInfo[3].length() - 1);
        //parse to bits
        String weeks = "";
        boolean oddWeek = false, evenWeek = false;
        for (int i = 0; i < courseInfo[4].length(); ++i) {
            char c = courseInfo[4].charAt(i);
            if (c == '双') {
                evenWeek = true;
            } else if (c == '单') {
                oddWeek = true;
            }
            if (c == '周') {
                weeks = courseInfo[4].substring(0, i);
                break;
            }
        }
        String[] be = weeks.split("-");
        assert(be.length == 2);
        int begin = Integer.parseInt(be[0]);
        int end = Integer.parseInt(be[1]);
        for(int i = begin; i <= end; ++i){
            if(oddWeek && (i & 1) == 1){
                this.weekBits |= (1 << i);
            }else if(evenWeek && (i & 1) == 0) {
                this.weekBits |= (1 << i);
            }else{
                this.weekBits |= (1 << i);
            }
        }
        assert((this.weekBits & 1) == 0);
        //parse courseStart and courseEnd
        index = courseInfo[4].lastIndexOf('-');
        this.courseEnd = Integer.parseInt(courseInfo[4].substring(index + 1, courseInfo[4].length() - 1));
        for (int i = index - 1; i >= 0; --i) {
            var c = courseInfo[4].charAt(i);
            if (!Character.isDigit(c)) {
                this.courseStart = Integer.parseInt(courseInfo[4].substring(i + 1, index));
                break;
            }
        }
    }

    public boolean haveCousre(int week){
        return (this.weekBits & (1 << week)) != 0;
    }

    public Integer getWeekBits() {
        return weekBits;
    }

    public void setWeekBits(Integer weekBits) {
        this.weekBits = weekBits;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(Integer courseStart) {
        this.courseStart = courseStart;
    }

    public Integer getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(Integer courseEnd) {
        this.courseEnd = courseEnd;
    }

    @Override
    public String toString() {
        return "Curriculum{" +
                "weekBits=" + weekBits +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", classroom='" + classroom + '\'' +
                ", day=" + day +
                ", courseStart=" + courseStart +
                ", courseEnd=" + courseEnd +
                '}';
    }
}
