package com.xandi.seafish.interfaces;

public interface AdService {

    void showBannerAd(boolean show);
    void showInterstitialAd(Runnable then);
}