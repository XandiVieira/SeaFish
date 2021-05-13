package com.xandi.seafish.model;

public class Position implements Comparable<Position> {

    private String userUid;
    private Integer score;
    private String usedFish;
    private int caughtWarms;
    private int turnedShark;
    private int caughtSpecialWarms;
    private int caughtBubbles;
    private int caughtByHook;
    private String deathTackle;

    public Position() {
    }

    public Position(String userUid, Integer score, String usedFish, String deathTackle, int caughtWarms, int caughtSpecialWarms, int turnedShark, int caughtBubbles, int caughtByHook) {
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return userUid.equals(position.userUid);
    }

    @Override
    public int compareTo(Position o) {
        return this.getScore().compareTo(o.getScore());
    }
}