package com.xandi.seafish;

public interface VideoEventListener {
    void onRewardedEvent();
    void onRewardedVideoAdLoadedEvent();
    void onRewardedVideoAdClosedEvent();
}