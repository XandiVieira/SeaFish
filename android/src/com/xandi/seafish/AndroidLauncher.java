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
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.xandi.seafish.activity.PrivacyPolicyActivity;
import com.xandi.seafish.activity.RankingActivity;
import com.xandi.seafish.activity.TermsActivity;
import com.xandi.seafish.interfaces.AdService;
import com.xandi.seafish.interfaces.FacebookAuth;
import com.xandi.seafish.interfaces.GoogleServices;
import com.xandi.seafish.interfaces.LoginCallback;
import com.xandi.seafish.interfaces.PrivacyPolicyAndTerms;
import com.xandi.seafish.interfaces.RankingInterface;
import com.xandi.seafish.interfaces.VideoEventListener;
import com.xandi.seafish.model.Position;
import com.xandi.seafish.model.User;
import com.xandi.seafish.screens.LoginScreen;
import com.xandi.seafish.screens.MenuScreen;
import com.xandi.seafish.util.Constants;
import com.xandi.seafish.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AndroidLauncher extends AndroidApplication implements AdService, GoogleServices, RewardedVideoAdListener, RankingInterface, FacebookAuth, PrivacyPolicyAndTerms {

    private static final String TAG = "AndroidLauncher";
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    private AdView bannerAd;
    private boolean is_video_ad_loaded;

    private SeafishGame game;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private boolean isLoggedIn = false;
    private User user;
    private DatabaseReference mRankingDatabaseRef;
    private DatabaseReference mUserDatabaseRef;
    private FirebaseUser firebaseUser;
    private String firebaseInstanceId;
    private RelativeLayout layout;
    private View gameView;
    private AdRequest request;
    private RelativeLayout.LayoutParams adParam;
    private AndroidApplicationConfiguration config;

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
    private VideoEventListener videoEventListener;
    private LoginCallback loginCallback;
    private final GoogleServices googleServices = this;
    private final RankingInterface rankingInterface = this;
    private final FacebookAuth facebookAuth = this;
    private final PrivacyPolicyAndTerms privacyPolicyAndTerms = this;
    private final AdService adService = this;

    public void callFullScreen() {
        View v = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        v.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callFullScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        layout = new RelativeLayout(this);
        config = new AndroidApplicationConfiguration();
        game = new SeafishGame(this, this, this, this, this);
        gameView = initializeForView(game, config);

        layout.addView(gameView);

        startFirebaseInstances();
        firebaseAuthListener = firebaseAuth -> {
            firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                getUserDataFromFirebase(firebaseUser.getUid());
            }
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

        bannerAd.setAdSize(AdSize.BANNER);
        bannerAd.setAdUnitId(AD_UNIT_ID_BANNER);

        List<String> testDeviceIds = Collections.singletonList("50D0F6A4C150377413FF5750D8289E5F");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        request = new AdRequest.Builder().build();
        adParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParam.setMargins(0, 0, 0, 0);
        addAdView();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1676578761693318/1635426123");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        setupInterstitialAds();
        setupRewarded();
    }

    private void addAdView() {
        layout.addView(bannerAd, adParam);
        bannerAd.loadAd(request);
        setContentView(layout);
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
        runOnUiThread(() -> {
            if (!adRewardedVideoView.isLoaded()) {
                loadRewardedVideoAd();
            }
        });
        return false;
    }

    public void showRewardedVideoAd() {
        runOnUiThread(() -> {
            if (adRewardedVideoView.isLoaded()) {
                adRewardedVideoView.show();
            } else {
                loadRewardedVideoAd();
            }
        });
    }

    @Override
    public void setVideoEventListener(VideoEventListener listener) {
        this.videoEventListener = listener;
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
    public void showInterstitialAd(final Runnable then) {
        runOnUiThread(() -> {
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
        });
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (videoEventListener != null) {
            videoEventListener.onRewardedVideoAdLoadedEvent();
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
        if (videoEventListener != null) {
            videoEventListener.onRewardedVideoAdClosedEvent();
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        if (videoEventListener != null) {
            videoEventListener.onRewardedEvent();
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
        startActivity(new Intent(getApplicationContext(), RankingActivity.class).putExtra("firebaseId", firebaseUser != null ? firebaseUser.getUid() : null));
    }

    @Override
    public void saveRecord(int score, String usedFish, String deathTackle, int caughtWarms, int caughtSpecialWarms, int turnedShark, int caughtBubbles, int caughtByHook) {
        if (firebaseUser != null) {
            Util.mDatabaseUserRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Util.mDatabaseUserRef.child(firebaseUser.getUid()).removeEventListener(this);
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        if (score > user.getPersonalRecord()) {
                            Util.mDatabaseUserRef.child(firebaseUser.getUid()).child(Constants.DATABASE_REF_PERSONAL_RECORD).setValue(score);
                        }
                        Util.mDatabaseRankingRef.orderByChild(Constants.DATABASE_REF_SCORE).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Util.mDatabaseRankingRef.orderByChild(Constants.DATABASE_REF_SCORE).limitToLast(Constants.RANKING_SIZE).removeEventListener(this);
                                HashMap<DatabaseReference, Position> ranking = new HashMap<>();
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    ranking.put(snap.getRef(), snap.getValue(Position.class));
                                }
                                Map<DatabaseReference, Position> oldPositions = ranking.entrySet().stream().filter(p -> p.getValue().getUserUid().equals(user.getUid())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                                //user already in the ranking
                                if (ranking.values().stream().map(Position::getUserUid).collect(Collectors.toList()).contains(user.getUid())) {
                                    if (!oldPositions.values().isEmpty()) {
                                        if (score > new ArrayList<>(oldPositions.values()).get(0).getScore()) {
                                            Util.mDatabaseRankingRef.push().setValue(new Position(user.getUid(), score, usedFish, deathTackle, caughtWarms, caughtSpecialWarms, turnedShark, caughtBubbles, caughtByHook));
                                            for (DatabaseReference ref : oldPositions.keySet()) {
                                                ref.removeValue();
                                            }
                                        }
                                    }
                                } else {
                                    if (ranking.size() >= Constants.RANKING_SIZE) {
                                        //Find lowest record owner
                                        Position lowestPosition = ranking.values().stream().min(Comparator.comparing(Position::getScore)).orElse(null);
                                        if (lowestPosition != null && score > lowestPosition.getScore()) {
                                            Util.mDatabaseRankingRef.push().setValue(new Position(user.getUid(), score, usedFish, deathTackle, caughtWarms, caughtSpecialWarms, turnedShark, caughtBubbles, caughtByHook));
                                            for (Map.Entry<DatabaseReference, Position> entry : oldPositions.entrySet()) {
                                                DatabaseReference ref = entry.getKey();
                                                Position pos = entry.getValue();
                                                if (pos.equals(lowestPosition)) {
                                                    ref.removeValue();
                                                }
                                            }
                                        }
                                    } else {
                                        Util.mDatabaseRankingRef.push().setValue(new Position(user.getUid(), score, usedFish, deathTackle, caughtWarms, caughtSpecialWarms, turnedShark, caughtBubbles, caughtByHook));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        loginCallback.userLoggedOut();
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
                //Error
            }
        });
    }

    @Override
    public void setLoginCallback(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            getUserDataFromFirebase(firebaseUser.getUid());
        });
    }

    private void getUserDataFromFirebase(String uid) {
        mUserDatabaseRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserDatabaseRef.child(uid).removeEventListener(this);
                mUserDatabaseRef.removeEventListener(this);
                user = dataSnapshot.getValue(User.class);
                if (user == null || user.getUid() == null) {
                    createUser(uid);
                } else {
                    updateUser(user);
                }
                if (loginCallback != null) {
                    loginCallback.userLoggedIn(user.getName(), user.getPersonalRecord());
                }
                if (game.getScreen() instanceof LoginScreen) {
                    game.dispose();
                    game.setScreen(new MenuScreen(game));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUser(User user) {
        if (!user.getName().equals(firebaseUser.getDisplayName())) {
            user.setName(firebaseUser.getDisplayName());
        }
        UserInfo userInfo = firebaseUser.getProviderData().get(0);
        String currentPhotoUrl = null;
        if (userInfo != null && userInfo.getPhotoUrl() != null) {
            currentPhotoUrl = userInfo.getPhotoUrl().toString().concat("?type=large");
        }
        if (currentPhotoUrl != null && !user.getPhotoPath().equals(currentPhotoUrl)) {
            user.setPhotoPath(currentPhotoUrl);
            mUserDatabaseRef.child(firebaseUser.getUid()).setValue(user);
        }
    }

    private void createUser(String uid) {
        String userPhotoPath = null;
        if (firebaseUser.getPhotoUrl() != null) {
            userPhotoPath = firebaseUser.getPhotoUrl().toString().concat("?type=large");
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> firebaseInstanceId = instanceIdResult.getToken());
        User user = new User(uid, firebaseUser.getDisplayName(), firebaseInstanceId, userPhotoPath);
        mUserDatabaseRef.child(firebaseUser.getUid()).setValue(user);
    }

    private void startFirebaseInstances() {
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = mFirebaseDatabase.getReference();
        mRankingDatabaseRef = mDatabaseRef.child(Constants.DATABASE_REF_RANKING);
        mUserDatabaseRef = mDatabaseRef.child(Constants.DATABASE_REF_USER);
        Util.setmDatabaseRankingRef(mRankingDatabaseRef);
        Util.setmDatabaseUserRef(mUserDatabaseRef);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void callPrivacyPolicy() {
        startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
    }

    @Override
    public void callTerms() {
        startActivity(new Intent(getApplicationContext(), TermsActivity.class));
    }
}