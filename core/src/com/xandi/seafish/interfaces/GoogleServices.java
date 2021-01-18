package com.xandi.seafish.interfaces;

public interface GoogleServices {
    boolean hasVideoLoaded();
    void loadRewardedVideoAd();
    void showRewardedVideoAd();
    void setVideoEventListener(VideoEventListener listener);
}