package com.join.tools.pdf.model;

/**
 * @author Join 2018-10-25
 */
public class Course {

    private String courseName;

    private float score;

    public Course(String courseName, float score) {
        this.courseName = courseName;
        this.score = score;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

}
