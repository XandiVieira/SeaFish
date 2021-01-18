package com.xandi.seafish.model;

public class Position {

    private String userUid;
    private int score;

    public Position() {
    }

    public Position(String userUid, int score) {
        this.userUid = userUid;
        this.score = score;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}