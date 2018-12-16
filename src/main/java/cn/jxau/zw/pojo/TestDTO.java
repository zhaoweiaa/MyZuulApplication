package cn.jxau.zw.pojo;

import cn.jxau.zw.annotation.ExcelConfig;

public class TestDTO {

    private Integer id;

    @ExcelConfig(exportName = "姓名",exportFieldWith = 25)
    private String name;

    @ExcelConfig(exportName = "性别",exportConvertSign = true)
    private Integer sex;

    @ExcelConfig(exportName = "学科")
    private String subject;

    @ExcelConfig(exportName = "成绩")
    private Integer score;


    public TestDTO() {
    }

    public TestDTO(Integer id, String name, Integer sex, String subject, Integer score) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.subject = subject;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * 性别转换字段方法
     * @return
     */
    public String getSexConvert(){
        switch (sex){
            case 0:
                return "未知";
            case 1:
                return "男";
            case 2:
                return "女";
            default:
                return "未知";
        }
    }

    @Override
    public String toString() {
        return "TestDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", subject='" + subject + '\'' +
                ", score=" + score +
                '}';
    }
}
