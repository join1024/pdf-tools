package com.join.tools.pdf.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Join 2018-10-25
 */
public class Student {

    private String name;

    private Integer age;

    private Date birthday;

    private BigDecimal amount;

    private List<Course> courseList;

    public Student(String name, Integer age, Date birthday, BigDecimal amount,
        List<Course> courseList) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.amount = amount;
        this.courseList = courseList;
    }

    public Student(String name, Integer age, List<Course> courseList) {
        this.name = name;
        this.age = age;
        this.courseList = courseList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}
