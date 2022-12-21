package com.example.studentapp.model;

import java.io.Serializable;

public class Post implements Serializable {

    public static final int POST_STATUS_WAITING = 0;
    public static final int POST_STATUS_CREATED_CLASS = 1;
    public static final int POST_STATUS_CANCELLED = -1;
    public static final int POST_STATUS_EDITED = 2;
    public static final int POST_ROLE_TUTOR = 1;
    public static final int POST_ROLE_STUDENT = 0;

    private String id;
    private String title;
    private int status; /// 0: waiting, 1: class created, -1: cancelled, 2: edited
    private String idUser;
    //private Subject subject;
    private String subject;
    //private Field field;
    private String field;
    private String dateTimesLearning;
    private String learningPlaces;
    private String method; // online or offline
    private int tuition;
    private String description;
    private String postDate;
    private String hideFrom;


    public Post(String id, String title, int status, String idUser, String subject, String field, String dateTimesLearning, String learningPlaces, String method, int tuition, String description, String postDate, String hideFrom) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.idUser = idUser;
        this.subject = subject;
        this.field = field;
        this.dateTimesLearning = dateTimesLearning;
        this.learningPlaces = learningPlaces;
        this.method = method;
        this.tuition = tuition;
        this.description = description;
        this.postDate = postDate;
        this.hideFrom = hideFrom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getHideFrom() {
        return hideFrom;
    }

    public void setHideFrom(String hideFrom) {
        this.hideFrom = hideFrom;
    }

    public String getDateTimesLearning() {
        return dateTimesLearning;
    }

    public void setDateTimesLearning(String dateTimesLearning) {
        this.dateTimesLearning = dateTimesLearning;
    }

    public String getLearningPlaces() {
        return learningPlaces;
    }

    public void setLearningPlaces(String learningPlaces) {
        this.learningPlaces = learningPlaces;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getTuition() {
        return tuition;
    }

    public void setTuition(int tuition) {
        this.tuition = tuition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getHide() {
        return hideFrom;
    }

    public void addHide(String hidePhoneNumber) {
        if (hideFrom != null) {
            this.hideFrom = this.hideFrom + ", " + hidePhoneNumber;
        }
        else {
            this.hideFrom = hidePhoneNumber;
        }
    }
}
