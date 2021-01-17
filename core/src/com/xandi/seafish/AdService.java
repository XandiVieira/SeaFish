package com.xandi.seafish;

public interface AdService {

    void showBannerAd(boolean show);
    void showinterstitialAd(Runnable then);
}