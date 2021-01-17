package com.xandi.seafish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xandi.seafish.activity.Constants;
import com.xandi.seafish.activity.Ranking;
import com.xandi.seafish.model.User;

import java.util.Arrays;
import java.util.UUID;

public class AndroidLauncher extends AndroidApplication implements AdService, GoogleServices, RewardedVideoAdListener, FirebaseAuthentication, FacebookAuth {

    private static final String TAG = "AndroidLauncher";
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    AdView bannerAd;
    private boolean is_video_ad_loaded;

    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private boolean isLoggedIn = false;
    private User user;
    private DatabaseReference mUserDatabaseRef;
    private FirebaseUser firebaseUser;
    private FacebookAuth facebookAuth;

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

    public void callFullScreen() {
        View v = this.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT < 19) { // lower api
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for new api versions.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            v.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callFullScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout layout = new RelativeLayout(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        facebookAuth = this;
        View gameView = initializeForView(new Seafish(this, this, this, facebookAuth), config);

        layout.addView(gameView);

        startFirebaseInstances();
        firebaseAuthListener = firebaseAuth -> {
            firebaseUser = firebaseAuth.getCurrentUser();
            isLoggedIn = firebaseUser != null;
        };
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);

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
        builder.addTestDevice("36EE88001735F5A5B7DB1D75A38FC19A");
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

    private void startFirebaseInstances() {
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = mFirebaseDatabase.getReference();
        mUserDatabaseRef = mDatabaseRef.child(Constants.DATABASE_REF_USER);
    }

    public void loadRewardedVideoAd() {
        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice("36EE88001735F5A5B7DB1D75A38FC19A");
        adRewardedVideoView.loadAd(REWARDED_VIDEO_AD_UNIT_ID, builder.build());
    }

    public void setupRewarded() {
        adRewardedVideoView = MobileAds.getRewardedVideoAdInstance(this);
        adRewardedVideoView.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    public boolean hasVideoLoaded() {
        if (is_video_ad_loaded) {
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

    public void showRewardedVideoAd() {
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
        builder.addTestDevice("36EE88001735F5A5B7DB1D75A38FC19A");
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
        if (vel != null) {
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
        if (vel != null) {
            vel.onRewardedVideoAdClosedEvent();
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        if (vel != null) {
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

    @Override
    public void callRanking() {
        startActivity(new Intent(getApplicationContext(), Ranking.class));
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public void login() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {

            }
        });
    }

    @Override
    public void userLoggedIn() {

    }

    @Override
    public void userLoggedOut() {

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            getUserDataFromFirebase(accessToken.getToken());
        });
    }

    private void getUserDataFromFirebase(String token) {
        mUserDatabaseRef.child(token).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserDatabaseRef.child(token).removeEventListener(this);
                mUserDatabaseRef.removeEventListener(this);
                user = dataSnapshot.getValue(User.class);
                if (user == null || user.getUid() == null) {
                    createUser(token);
                }
                facebookAuth.userLoggedIn();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createUser(String token) {
        String userPhotoPath = null;
        if (firebaseUser.getPhotoUrl() != null) {
            userPhotoPath = firebaseUser.getPhotoUrl().toString();
        }
        User user = new User(UUID.randomUUID().toString(), token, firebaseUser.getDisplayName(), userPhotoPath);
        mUserDatabaseRef.child(firebaseUser.getUid()).setValue(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}