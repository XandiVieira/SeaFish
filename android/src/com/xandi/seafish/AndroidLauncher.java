package com.xandi.seafish;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AndroidLauncher extends AndroidApplication implements AdService, GoogleServices, RewardedVideoAdListener {

	private static final String TAG = "AndroidLauncher";
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	AdView bannerAd;
	private boolean is_video_ad_loaded;

	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SHOW_ADS: {
					bannerAd.setVisibility(View.VISIBLE);
					break;
				}
				case HIDE_ADS: {
					bannerAd.setVisibility(View.GONE);
					break;
				}
			}
		}
	};

	private InterstitialAd mInterstitialAd;

	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-1676578761693318/2954296516";
	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-1676578761693318/2824156772";
	private static final String REWARDED_VIDEO_AD_UNIT_ID = "ca-app-pub-1676578761693318/4622601407";

	private RewardedVideoAd adRewardedVideoView;
	private VideoEventListener vel;

    public void fullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fullScreencall();
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//fullScreencall();

		RelativeLayout layout = new RelativeLayout(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new Seafish(this, this), config);

		layout.addView(gameView);

		bannerAd = new AdView(this);
		bannerAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.i(TAG, "Ad Loaded...");
			}
		});


		bannerAd.setAdSize(AdSize.SMART_BANNER);
		bannerAd.setAdUnitId(AD_UNIT_ID_BANNER);
		AdRequest.Builder builder = new AdRequest.Builder();
		builder.addTestDevice("3DF6979E4CCB56C2A91510C1A9BCC253");
		RelativeLayout.LayoutParams adParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParam.setMargins(0, 3, 0, 0);
		layout.addView(bannerAd, adParam);
		bannerAd.loadAd(builder.build());
		setContentView(layout);

		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId("ca-app-pub-1676578761693318/1635426123");
		mInterstitialAd.loadAd(new AdRequest.Builder().build());

		setupInterstitialAds();
		setupRewarded();
	}

	public void loadRewardedVideoAd() {
		AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice("3DF6979E4CCB56C2A91510C1A9BCC253");
		adRewardedVideoView.loadAd(REWARDED_VIDEO_AD_UNIT_ID, builder.build());
	}

	public void setupRewarded() {
		adRewardedVideoView = MobileAds.getRewardedVideoAdInstance(this);
		adRewardedVideoView.setRewardedVideoAdListener(this);
		loadRewardedVideoAd();
	}

	public boolean hasVideoLoaded(){
		if(is_video_ad_loaded) {
			return true;
		}
		runOnUiThread(new Runnable() {
			public void run() {
				if (!adRewardedVideoView.isLoaded()) {
					loadRewardedVideoAd();
				}
			}
		});
		return false;
	}

	public void showRewardedVideoAd(){
		runOnUiThread(new Runnable() {
			public void run() {
				if (adRewardedVideoView.isLoaded()) {
					adRewardedVideoView.show();
				} else {
					loadRewardedVideoAd();
				}
			}
		});
	}

	@Override
	public void setVideoEventListener(VideoEventListener listener) {
		this.vel = listener;
	}

	public void setupInterstitialAds() {

		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);

		AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice("3DF6979E4CCB56C2A91510C1A9BCC253");
		AdRequest ad = builder.build();
		mInterstitialAd.loadAd(ad);
	}

	@Override
	public void showBannerAd(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}

	@Override
	public void showinterstitialAd(final Runnable then) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (then != null) {
					mInterstitialAd.setAdListener(new AdListener() {
						@Override
						public void onAdClosed() {
							Gdx.app.postRunnable(then);
							AdRequest.Builder builder = new AdRequest.Builder();
                            builder.addTestDevice("3DF6979E4CCB56C2A91510C1A9BCC253");
							AdRequest ad = builder.build();
							mInterstitialAd.loadAd(ad);
						}
					});
				}
				mInterstitialAd.show();
			}
		});
	}

	@Override
	public void onRewardedVideoAdLoaded() {
		if(vel != null) {
			vel.onRewardedVideoAdLoadedEvent();
		}
		is_video_ad_loaded = true;
	}

	@Override
	public void onRewardedVideoAdOpened() {

	}

	@Override
	public void onRewardedVideoStarted() {

	}

	@Override
	public void onRewardedVideoAdClosed() {
		is_video_ad_loaded = false;
		loadRewardedVideoAd();
		if(vel != null) {
			vel.onRewardedVideoAdClosedEvent();
		}
	}

	@Override
	public void onRewarded(RewardItem rewardItem) {
		if(vel != null) {
			vel.onRewardedEvent();
		}
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {

	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {

	}

	@Override
	public void onRewardedVideoCompleted() {

	}
}
