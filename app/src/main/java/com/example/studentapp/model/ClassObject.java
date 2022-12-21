package com.example.studentapp.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class ClassObject implements Serializable {

    public static final int CLASS_STATUS_REJECTED = -2;
    public static final int CLASS_STATUS_PENDING = -1;
    public static final int CLASS_STATUS_AVAILABLE = 0;
    public static final int CLASS_STATUS_ARCHIVED = 1;
    public static final int CLASS_STATUS_RATED = 2;

    String id;
    String className;
    String tutorPhone;
    String studentPhone;
    String place;
    // 0: Đang học, 1: Đã lưu trữ (đã hoàn thành), -1: Đã đánh giá
    int status;
    int fee;
    String dateTime;
    String startDate;
    String endDate;
    private String method; // online or offline
    private String subject;
    private String field;

    public ClassObject(){

    }

    public ClassObject(String id, String className, String tutorPhone, String studentPhone, String place, int status, int fee, String dateTime, String startDate, String endDate, String method, String subject, String field) {
        this.id = id;
        this.className = className;
        this.tutorPhone = tutorPhone;
        this.studentPhone = studentPhone;
        this.place = place;
        this.status = status;
        this.fee = fee;
        this.dateTime = dateTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.method = method;
        this.subject = subject;
        this.field = field;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTutorPhone() {
        return tutorPhone;
    }

    public void setTutorPhone(String tutorPhone) {
        this.tutorPhone = tutorPhone;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean equalsTo(ClassObject classObject) {
        return classObject.getId().equals(id);
    }
}
