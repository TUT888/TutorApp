package com.example.studentapp.model;

public class Student extends User{

    private String fields;

    public Student() {
        super();
    }

    public Student(String phoneNumber, String name, int status, String address, int gender, String birthday, String email, String avatar, String password, String fields) {
        super(phoneNumber, name, status, address, gender, birthday, email, avatar, password);
        this.fields = fields;
    }

    public Student(String phoneNumber, String name, int status, String address, int gender, String birthday, String email, String password, String fields) {
        super(phoneNumber, name, status, address, gender, birthday, email, password);
        this.fields = fields;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
