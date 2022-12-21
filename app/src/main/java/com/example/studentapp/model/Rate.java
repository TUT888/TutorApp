package com.example.studentapp.model;

import java.io.Serializable;

public class Rate implements Serializable {
    String classId;
    float rate;
    String comment;
    String date;

    public Rate (String classId, float rate, String comment, String date) {
        this.classId = classId;
        this.rate = rate;
        this.comment = comment;
        this.date = date;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
