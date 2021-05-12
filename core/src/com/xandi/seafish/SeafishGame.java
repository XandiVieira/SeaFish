package com.xandi.seafish;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.xandi.seafish.interfaces.AdService;
import com.xandi.seafish.interfaces.FacebookAuth;
import com.xandi.seafish.interfaces.GoogleServices;
import com.xandi.seafish.interfaces.PrivacyPolicyAndTerms;
import com.xandi.seafish.interfaces.RankingInterface;
import com.xandi.seafish.interfaces.VideoEventListener;
import com.xandi.seafish.screens.GameScreen;
import com.xandi.seafish.screens.LoginScreen;
import com.xandi.seafish.screens.MenuScreen;

public class SeafishGame extends Game implements VideoEventListener {

    //Mantém dados salvos
    public Preferences prefs;

    //Controla anúncios
    public AdService handler;
    public GoogleServices googleServices;

    public RankingInterface rankingInterface;
    public FacebookAuth facebookAuth;
    public PrivacyPolicyAndTerms privacyPolicyAndTerms;

    public float originalWidth;
    public float differenceBetweenWidth;
    public float width;
    public float height;
    public float adjustHeight;
    public float adjustWidth;
    public double heightStandard = 1080;
    public double widthStandard = 1920;

    //Score
    public Integer record;

    //counters
    private int numMinhocas;
    public int contaPartidas = 1;

    //Controllers
    private boolean isRewarded;
    private boolean isRewardedYet;

    //Numero do peixe
    public int fishNumber;

    private Viewport viewport;

    SeafishGame(AdService handler, GoogleServices googleServices, RankingInterface rankingInterface, FacebookAuth facebookAuth, PrivacyPolicyAndTerms privacyPolicyAndTerms) {
        this.handler = handler;
        this.googleServices = googleServices;
        this.googleServices.setVideoEventListener(this);
        this.rankingInterface = rankingInterface;
        this.facebookAuth = facebookAuth;
        //this.facebookAuth.setLoginCallback(this);
        this.privacyPolicyAndTerms = privacyPolicyAndTerms;
    }

    @Override
    public void create() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        adjustHeight = (float) (height / heightStandard);
        adjustWidth = (float) (width / widthStandard);
        prefs = Gdx.app.getPreferences("score");
        record = prefs.getInteger("score");
        if (originalWidth == 0) {
            originalWidth = width;
        }

        if (viewport == null) {
            OrthographicCamera camera = new OrthographicCamera();
            viewport = new StretchViewport(width, height, camera);
        }
        if (facebookAuth.isLoggedIn()) {
            setScreen(new MenuScreen(this));
        } else {
            setScreen(new LoginScreen(this));
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void onRewardedEvent() {
        isRewarded = true;
    }

    @Override
    public void onRewardedVideoAdLoadedEvent() {
    }

    @Override
    public void onRewardedVideoAdClosedEvent() {
        if (isRewarded) {
            numMinhocas = 4;
            isRewardedYet = true;
        } else {
            setScreen(new GameScreen(this));
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.width = width;
        this.height = height;
    }

    public void saveScores(int metersScore, String usedFish, String deathTackle, int caughtWarms, int caughtSpecialWarms, int turnedShark, int caughtBubbles, int caughtByHook) {
        rankingInterface.saveRecord((int) metersScore, usedFish, deathTackle, caughtWarms, caughtSpecialWarms, turnedShark, caughtBubbles, caughtByHook);
        if (metersScore > record) {
            prefs.putInteger("score", (int) metersScore);
            prefs.flush();
        }
    }
}