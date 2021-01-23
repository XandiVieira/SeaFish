package com.xandi.seafish.interfaces;

public interface RankingInterface {

    void callRanking();

    void saveRecord(int score, String usedFish, String deathTackle, int caughtWarms, int caughtSpecialWarms, int turnedShark, int caughtBubbles, int caughtByHook);
}
