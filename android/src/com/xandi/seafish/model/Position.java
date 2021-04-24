package com.xandi.seafish.model;

public class Position {

    private String userUid;
    private int score;
    private String usedFish;
    private int caughtWarms;
    private int turnedShark;
    private int caughtSpecialWarms;
    private int caughtBubbles;
    private int caughtByHook;
    private String deathTackle;

    public Position() {
    }

    public Position(String userUid, int score, String usedFish, String deathTackle, int caughtWarms, int caughtSpecialWarms, int turnedShark, int caughtBubbles, int caughtByHook) {
        this.userUid = userUid;
        this.score = score;
        this.usedFish = usedFish;
        this.deathTackle = deathTackle;
        this.caughtWarms = caughtWarms;
        this.caughtSpecialWarms = caughtSpecialWarms;
        this.turnedShark = turnedShark;
        this.caughtBubbles = caughtBubbles;
        this.caughtByHook = caughtByHook;
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

    public String getUsedFish() {
        return usedFish;
    }

    public void setUsedFish(String usedFish) {
        this.usedFish = usedFish;
    }

    public int getCaughtWarms() {
        return caughtWarms;
    }

    public void setCaughtWarms(int caughtWarms) {
        this.caughtWarms = caughtWarms;
    }

    public int getTurnedShark() {
        return turnedShark;
    }

    public void setTurnedShark(int turnedShark) {
        this.turnedShark = turnedShark;
    }

    public int getCaughtSpecialWarms() {
        return caughtSpecialWarms;
    }

    public void setCaughtSpecialWarms(int caughtSpecialWarms) {
        this.caughtSpecialWarms = caughtSpecialWarms;
    }

    public int getCaughtBubbles() {
        return caughtBubbles;
    }

    public void setCaughtBubbles(int caughtBubbles) {
        this.caughtBubbles = caughtBubbles;
    }

    public int getCaughtByHook() {
        return caughtByHook;
    }

    public void setCaughtByHook(int caughtByHook) {
        this.caughtByHook = caughtByHook;
    }

    public String getDeathTackle() {
        return deathTackle;
    }

    public void setDeathTackle(String deathTackle) {
        this.deathTackle = deathTackle;
    }
}