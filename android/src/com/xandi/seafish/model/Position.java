package com.xandi.seafish.model;

public class Position {

    private String userUid;
    private Float score;

    public Position() {
    }

    public Position(String userUid, Float score) {
        this.userUid = userUid;
        this.score = score;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}