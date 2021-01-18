package com.xandi.seafish;

public interface GoogleServices {
    boolean hasVideoLoaded();
    void loadRewardedVideoAd();
    void showRewardedVideoAd();
    void setVideoEventListener(VideoEventListener listener);
}