package com.join.tools.pdf.demo;

import java.util.List;

/**
 * @author Join 2018-10-25
 */
public class Student {

    private String name;

    private Integer age;

    private List<Course> courseList;

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

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}
