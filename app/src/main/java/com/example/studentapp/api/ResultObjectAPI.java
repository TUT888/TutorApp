package com.example.studentapp.api;

import com.google.gson.JsonObject;


public class ResultObjectAPI {
    private int code;
    private String message;
    private JsonObject data;
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public JsonObject getData() {
        return data;
    }

}
