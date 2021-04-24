package com.xandi.seafish.model;

public class User {

    private String uid;
    private String name;
    private String token;
    private String photoPath;
    private Long personalRecord;

    public User() {
    }

    public User(String uid, String name, String token, String photoPath) {
        this.uid = uid;
        this.name = name;
        this.token = token;
        this.photoPath = photoPath;
        this.personalRecord = 0L;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Long getPersonalRecord() {
        return personalRecord;
    }

    public void setPersonalRecord(Long personalRecord) {
        this.personalRecord = personalRecord;
    }
}