package com.example.studentapp.model;

import java.io.Serializable;

public class Tutor extends User implements Serializable {
    private String school;
    private String academicLevel;
    private String fields;
    private String areas;

    public Tutor() {
    }

    public Tutor(String phoneNumber, String name, int status, String address, int gender, String birthday, String email, String avatar, String password, String school, String academicLevel, String fields, String areas) {
        super(phoneNumber, name, status, address, gender, birthday, email, avatar, password);
        this.school = school;
        this.academicLevel = academicLevel;
        this.fields = fields;
        this.areas = areas;
    }

    public Tutor(String phoneNumber, String name, int status, String address, int gender, String birthday, String email, String password, String school, String academicLevel, String fields, String areas) {
        super(phoneNumber, name, status, address, gender, birthday, email, password);
        this.school = school;
        this.academicLevel = academicLevel;
        this.fields = fields;
        this.areas = areas;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }
}
