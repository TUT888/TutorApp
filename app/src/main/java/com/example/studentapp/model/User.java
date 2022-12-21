package com.example.studentapp.model;

import java.io.Serializable;

public class User implements Serializable {

    public static final int USER_STATUS_NOT_VERIFIED = 0;
    public static final int USER_STATUS_VERIFIED = 1;
    public static final int USER_STATUS_LOCKED = -1;

    private String phoneNumber;
    private String name;
    private int status;
    private String address;
    private int gender;
    private String birthday;
    private String email;
    private String avatar;
    private String password;

    public User(String phoneNumber, String name, int status, String address, int gender, String birthday, String email, String avatar, String password) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.status = status;
        this.address = address;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
    }

    public User(String phoneNumber, String name, int status, String address, int gender, String birthday, String email, String password) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.status = status;
        this.address = address;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.avatar = "avatar.jpg";
    }

    public User() {

    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
