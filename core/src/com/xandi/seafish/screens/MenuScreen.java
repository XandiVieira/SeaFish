package com.xandi.seafish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xandi.seafish.SeafishGame;
import com.xandi.seafish.interfaces.LoginCallback;

import java.util.Random;

public class MenuScreen extends BaseScreen implements LoginCallback {

    private final SeafishGame seafishGame;
    private Texture background, loginButton, rankingButton, startButton, termsButton, privacyPolicyButton, nextButton, backButton, movingBubble;
    private Sprite playSprite, loginSprite, rankingSprite, termsSprite, privacyPolicySprite, nextSprite, backSprite;
    private SpriteBatch batch;
    private Sprite[] seaweed;
    private Sprite[][] fishes;
    private float HEIGHT_SELECT_FISH;
    private int seaweedVariation;
    private float seaweedVariationAux;
    private BitmapFont userNameFont;
    private String userName;

    //Numero do peixe
    private int fishNumber;
    private int fishVariation;
    private float fishVariationAux;

    private float[] bubbleHorizontalMovement;
    private float[] bubbleVerticalMovement;
    private boolean[] bubbleTouchedTop;
    private boolean[] BubbleTouchedSide;
    private Random bubblePosition;

    //Rotação algas
    private boolean controlRotation;

    public MenuScreen(SeafishGame seafishGame) {
        super(seafishGame);
        this.seafishGame = seafishGame;
    }

    @Override
    public void show() {
        super.show();
        userNameFont = new BitmapFont();
        userNameFont.setColor(Color.YELLOW);
        userNameFont.getData().setScale(3 * seafishGame.adjustWidth);
        batch = new SpriteBatch();
        fishes = new Sprite[7][5];
        bubbleHorizontalMovement = new float[10];
        bubbleVerticalMovement = new float[10];
        bubbleTouchedTop = new boolean[10];
        BubbleTouchedSide = new boolean[10];
        bubblePosition = new Random();
        for (int i = 0; i < 10; i++) {
            bubbleHorizontalMovement[i] = (float) bubblePosition.nextInt((int) seafishGame.width * 2);
            bubbleVerticalMovement[i] = (float) bubblePosition.nextInt((int) seafishGame.height * 2);
        }
        setTextures();
        setButtons();
        HEIGHT_SELECT_FISH = ((seafishGame.height) - (startButton.getHeight() * seafishGame.adjustHeight * 5));
        setSeaweed();
        setFishes();
        fishVariation = 0;
        fishVariationAux = 0;
    }

    private void setTextures() {
        background = new Texture("images/scenarios/telainicio.png");
        loginButton = new Texture("images/buttons/login.png");
        startButton = new Texture("images/buttons/startgame.png");
        rankingButton = new Texture("images/buttons/ranking.png");
        termsButton = new Texture("images/buttons/terms.png");
        privacyPolicyButton = new Texture("images/buttons/privacypolicy.png");
        nextButton = new Texture("images/buttons/next.png");
        backButton = new Texture("images/buttons/back.png");
        movingBubble = new Texture("images/ornaments/bolhainicio.png");
    }

    private void setButtons() {
        playSprite = new Sprite(startButton);
        playSprite.setSize(startButton.getWidth() * seafishGame.adjustWidth, startButton.getHeight() * seafishGame.adjustHeight);
        playSprite.setPosition(((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (startButton.getWidth() * seafishGame.adjustWidth / 2), ((seafishGame.height) - (startButton.getHeight() * seafishGame.adjustHeight * 3)));

        loginSprite = new Sprite(loginButton);
        loginSprite.setSize((loginButton.getWidth() / 2f) * seafishGame.adjustWidth, loginButton.getHeight() * seafishGame.adjustHeight);
        loginSprite.setPosition((loginButton.getWidth() * seafishGame.adjustWidth) / 10f, (seafishGame.height / 2f) - (rankingButton.getHeight() * seafishGame.adjustHeight));

        rankingSprite = new Sprite(rankingButton);
        rankingSprite.setSize((rankingButton.getWidth()) * seafishGame.adjustWidth, rankingButton.getHeight() * seafishGame.adjustHeight);
        rankingSprite.setPosition((rankingButton.getWidth() * seafishGame.adjustWidth) / 2.5f, (seafishGame.height / 2f));

        termsSprite = new Sprite(termsButton);
        termsSprite.setSize((termsButton.getWidth() / 2f) * seafishGame.adjustWidth, termsButton.getHeight() * seafishGame.adjustHeight);
        termsSprite.setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) - ((privacyPolicyButton.getWidth() / 2f) * seafishGame.adjustWidth), ((privacyPolicyButton.getHeight() / 1.5f) * seafishGame.adjustHeight) + (privacyPolicyButton.getHeight() * seafishGame.adjustHeight * 1.2f));

        privacyPolicySprite = new Sprite(privacyPolicyButton);
        privacyPolicySprite.setSize((privacyPolicyButton.getWidth() / 2f) * seafishGame.adjustWidth, privacyPolicyButton.getHeight() * seafishGame.adjustHeight);
        privacyPolicySprite.setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) - ((privacyPolicyButton.getWidth() / 2f) * seafishGame.adjustWidth), ((privacyPolicyButton.getHeight() / 1.5f) * seafishGame.adjustHeight));

        nextSprite = new Sprite(nextButton);
        nextSprite.setSize(nextButton.getWidth() * seafishGame.adjustWidth, nextButton.getHeight() * seafishGame.adjustHeight);
        nextSprite.setPosition(((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) + (nextButton.getWidth() * seafishGame.adjustWidth), HEIGHT_SELECT_FISH + ((nextButton.getHeight() * 2) * seafishGame.adjustHeight));

        backSprite = new Sprite(backButton);
        backSprite.setSize(backButton.getWidth() * seafishGame.adjustWidth, backButton.getHeight() * seafishGame.adjustHeight);
        backSprite.setPosition(((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (backButton.getWidth() * 1.85f * seafishGame.adjustWidth), HEIGHT_SELECT_FISH + ((backButton.getHeight() * 2) * seafishGame.adjustHeight));
    }

    private void setFishes() {
        fishes[0][0] = new Sprite(new Texture("images/characters/peixe1.png"));
        fishes[0][1] = new Sprite(new Texture("images/characters/peixe12.png"));
        fishes[0][2] = new Sprite(new Texture("images/characters/peixe1red.png"));
        fishes[0][3] = new Sprite(new Texture("images/characters/tubarao1.png"));
        fishes[0][4] = new Sprite(new Texture("images/characters/tubarao12.png"));
        fishes[1][0] = new Sprite(new Texture("images/characters/peixe2.png"));
        fishes[1][1] = new Sprite(new Texture("images/characters/peixe22.png"));
        fishes[1][2] = new Sprite(new Texture("images/characters/peixe2red.png"));
        fishes[1][3] = new Sprite(new Texture("images/characters/tubarao2.png"));
        fishes[1][4] = new Sprite(new Texture("images/characters/tubarao22.png"));
        fishes[2][0] = new Sprite(new Texture("images/characters/peixe3.png"));
        fishes[2][1] = new Sprite(new Texture("images/characters/peixe32.png"));
        fishes[2][2] = new Sprite(new Texture("images/characters/peixe3red.png"));
        fishes[2][3] = new Sprite(new Texture("images/characters/tubarao3.png"));
        fishes[2][4] = new Sprite(new Texture("images/characters/tubarao32.png"));

        if (seafishGame.record < 2000) {
            fishes[3][0] = new Sprite(new Texture("images/characters/sombra1.png"));
            fishes[3][1] = new Sprite(new Texture("images/characters/sombra1.png"));
        } else {
            fishes[3][0] = new Sprite(new Texture("images/characters/peixe4.png"));
            fishes[3][1] = new Sprite(new Texture("images/characters/peixe42.png"));
            fishes[3][2] = new Sprite(new Texture("images/characters/peixe4red.png"));
            fishes[3][3] = new Sprite(new Texture("images/characters/tubarao4.png"));
            fishes[3][4] = new Sprite(new Texture("images/characters/tubarao42.png"));
        }

        if (seafishGame.record < 4000) {
            fishes[4][0] = new Sprite(new Texture("images/characters/sombra2.png"));
            fishes[4][1] = new Sprite(new Texture("images/characters/sombra2.png"));
        } else {
            fishes[4][0] = new Sprite(new Texture("images/characters/peixe5.png"));
            fishes[4][1] = new Sprite(new Texture("images/characters/peixe52.png"));
            fishes[4][2] = new Sprite(new Texture("images/characters/peixe5red.png"));
            fishes[4][3] = new Sprite(new Texture("images/characters/tubarao5.png"));
            fishes[4][4] = new Sprite(new Texture("images/characters/tubarao52.png"));
        }

        if (seafishGame.record < 6000) {
            fishes[5][0] = new Sprite(new Texture("images/characters/sombra3.png"));
            fishes[5][1] = new Sprite(new Texture("images/characters/sombra3.png"));
        } else {
            fishes[5][0] = new Sprite(new Texture("images/characters/peixe6.png"));
            fishes[5][1] = new Sprite(new Texture("images/characters/peixe62.png"));
            fishes[5][2] = new Sprite(new Texture("images/characters/peixe6red.png"));
            fishes[5][3] = new Sprite(new Texture("images/characters/tubarao6.png"));
            fishes[5][4] = new Sprite(new Texture("images/characters/tubarao62.png"));
        }

        if (seafishGame.record < 8000) {
            fishes[6][0] = new Sprite(new Texture("images/characters/sombra4.png"));
            fishes[6][1] = new Sprite(new Texture("images/characters/sombra4.png"));
        } else {
            fishes[6][0] = new Sprite(new Texture("images/characters/peixe7.png"));
            fishes[6][1] = new Sprite(new Texture("images/characters/peixe72.png"));
            fishes[6][2] = new Sprite(new Texture("images/characters/peixe7red.png"));
            fishes[6][3] = new Sprite(new Texture("images/characters/tubarao7.png"));
            fishes[6][4] = new Sprite(new Texture("images/characters/tubarao72.png"));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (Gdx.input.justTouched()) {
            if (Gdx.input.justTouched()) {
                if (playSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    if (fishNumber == 0 || fishNumber == 1 || fishNumber == 2 || (fishNumber == 3 && seafishGame.record >= 2000) || (fishNumber == 4 && seafishGame.record >= 4000) || (fishNumber == 5 && seafishGame.record >= 6000) || (fishNumber == 6 && seafishGame.record >= 8000)) {
                        seafishGame.fishNumber = fishNumber;
                        seafishGame.setScreen(new GameScreen(seafishGame));
                    }
                }

                if (loginSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    if (seafishGame.facebookAuth.isLoggedIn()) {
                        seafishGame.facebookAuth.logout();
                    } else {
                        seafishGame.facebookAuth.login();
                    }
                }

                if (rankingSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    seafishGame.rankingInterface.callRanking();
                }

                if (termsSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    seafishGame.privacyPolicyAndTerms.callTerms();
                }

                if (privacyPolicySprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    seafishGame.privacyPolicyAndTerms.callPrivacyPolicy();
                }
            }
        }

        batch.begin();

        batch.draw(background, 0, 0, seafishGame.width, seafishGame.height);
        batch.draw(startButton, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (startButton.getWidth() * seafishGame.adjustWidth / 2), ((seafishGame.height) - (startButton.getHeight() * seafishGame.adjustHeight * 3)), startButton.getWidth() * seafishGame.adjustWidth, startButton.getHeight() * seafishGame.adjustHeight);

        batch.draw(termsButton, (seafishGame.width - seafishGame.differenceBetweenWidth) - ((privacyPolicyButton.getWidth() / 2f) * seafishGame.adjustWidth), ((privacyPolicyButton.getHeight() / 1.5f) * seafishGame.adjustHeight) + (privacyPolicyButton.getHeight() * seafishGame.adjustHeight * 1.2f), (termsButton.getWidth() / 2f) * seafishGame.adjustWidth, termsButton.getHeight() * seafishGame.adjustHeight);
        batch.draw(privacyPolicyButton, (seafishGame.width - seafishGame.differenceBetweenWidth) - ((privacyPolicyButton.getWidth() / 2f) * seafishGame.adjustWidth), ((privacyPolicyButton.getHeight() / 1.5f) * seafishGame.adjustHeight), (privacyPolicyButton.getWidth() / 2f) * seafishGame.adjustWidth, privacyPolicyButton.getHeight() * seafishGame.adjustHeight);

        batch.draw(rankingButton, (rankingButton.getWidth() * seafishGame.adjustWidth) / 2.5f, (seafishGame.height / 2f), rankingButton.getWidth() * seafishGame.adjustWidth, rankingButton.getHeight() * seafishGame.adjustHeight);
        batch.draw(loginButton, (loginButton.getWidth() * seafishGame.adjustWidth) / 10f, (seafishGame.height / 2f) - (rankingButton.getHeight() * seafishGame.adjustHeight), (loginButton.getWidth() * seafishGame.adjustWidth / 2f), loginButton.getHeight() * seafishGame.adjustHeight);

        if (userName != null) {
            userNameFont.draw(batch, userName, (loginButton.getWidth() * seafishGame.adjustWidth) / 10f, (seafishGame.height / 2f) - (loginButton.getHeight() * seafishGame.adjustHeight * 2f) - (loginButton.getHeight() * seafishGame.adjustHeight) / 10f);
        }

        seaweed[seaweedVariation].draw(batch);
        seaweed[2].draw(batch);
        batch.draw(backButton, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (backButton.getWidth() * 1.85f * seafishGame.adjustWidth), HEIGHT_SELECT_FISH + (backButton.getHeight() * seafishGame.adjustHeight), backButton.getWidth() * seafishGame.adjustWidth, backButton.getHeight() * seafishGame.adjustHeight);
        batch.draw(nextButton, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) + (nextButton.getWidth() * seafishGame.adjustWidth), HEIGHT_SELECT_FISH + (nextButton.getHeight() * seafishGame.adjustHeight), nextButton.getWidth() * seafishGame.adjustWidth, nextButton.getHeight() * seafishGame.adjustHeight);
        batch.draw(fishes[fishNumber][fishVariation], ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (fishes[fishNumber][0].getWidth() * seafishGame.adjustWidth / 2), HEIGHT_SELECT_FISH + 70, fishes[fishNumber][0].getWidth() * seafishGame.adjustWidth, fishes[fishNumber][0].getHeight() * seafishGame.adjustHeight);

        for (int i = 0; i < 10; i++) {
            if (bubbleHorizontalMovement[i] >= (seafishGame.width - seafishGame.differenceBetweenWidth) - movingBubble.getWidth() * seafishGame.adjustWidth) {
                BubbleTouchedSide[i] = true;
            }

            if (bubbleHorizontalMovement[i] <= 0) {
                BubbleTouchedSide[i] = false;
            }

            if (BubbleTouchedSide[i]) {
                bubbleHorizontalMovement[i] -= (Gdx.graphics.getDeltaTime() * bubblePosition.nextInt(50) + 1) * seafishGame.adjustWidth;
            } else {
                bubbleHorizontalMovement[i] += (Gdx.graphics.getDeltaTime() * bubblePosition.nextInt(200) + 1) * seafishGame.adjustWidth;
            }

            if (bubbleVerticalMovement[i] >= seafishGame.height - movingBubble.getHeight() * seafishGame.adjustHeight) {
                bubbleTouchedTop[i] = true;
            }

            if (bubbleVerticalMovement[i] <= 0) {
                bubbleTouchedTop[i] = false;
            }

            if (bubbleTouchedTop[i]) {
                bubbleVerticalMovement[i] -= (Gdx.graphics.getDeltaTime() * bubblePosition.nextInt(100) + 1) * seafishGame.adjustHeight;
            } else {
                bubbleVerticalMovement[i] += (Gdx.graphics.getDeltaTime() * bubblePosition.nextInt(150) + 1) * seafishGame.adjustHeight;
            }
            batch.draw(movingBubble, bubbleHorizontalMovement[i], bubbleVerticalMovement[i], movingBubble.getWidth() * seafishGame.adjustWidth, movingBubble.getHeight() * seafishGame.adjustWidth);
        }
        batch.end();

        controlSeaweedRotation();
        fishVariationAux += (float) 0.05;
        seaweedVariationAux += (float) 0.009;
        varySeaweed();
        varyFish();
        selectFish();
    }

    private void varyFish() {
        if (fishVariationAux > 2) {
            fishVariationAux = 0;
        } else if (fishVariationAux >= 1) {
            fishVariation = 0;
        } else {
            fishVariation = 1;
        }
    }

    public void controlSeaweedRotation() {
        if (controlRotation) {
            if (seaweed[2].getRotation() <= -35 * (seafishGame.adjustWidth)) {
                controlRotation = false;
            }
            seaweed[2].rotate((float) -0.4 * seafishGame.adjustWidth);
        } else {
            if (seaweed[2].getRotation() >= 20 * (seafishGame.adjustWidth)) {
                controlRotation = true;
            }
            seaweed[2].rotate((float) 0.4 * seafishGame.adjustWidth);
        }
    }

    private void varySeaweed() {
        if (seaweedVariationAux > 1) {
            seaweedVariationAux = 0;
        }
        if (seaweedVariationAux >= 0.5) {
            seaweedVariation = 0;
        } else {
            seaweedVariation = 1;
        }
    }

    private void selectFish() {
        if (Gdx.input.justTouched()) {
            if (backSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (fishNumber == 0) {
                    fishNumber = 6;
                } else {
                    fishNumber--;
                }
            }
        }
        if (Gdx.input.justTouched()) {
            if (nextSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (fishNumber == 6) {
                    fishNumber = 0;
                } else {
                    fishNumber++;
                }
            }
        }
    }

    private void setSeaweed() {
        seaweed = new Sprite[3];
        seaweed[0] = new Sprite(new Texture("images/ornaments/alga1.png"));
        seaweed[0].setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) / 10, -seaweed[0].getHeight() / 5);
        seaweed[0].setSize(seaweed[0].getWidth() * seafishGame.adjustWidth, seaweed[0].getHeight() * seafishGame.adjustHeight);

        seaweed[1] = new Sprite(new Texture("images/ornaments/algaVaria.png"));
        seaweed[1].setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) / 10, -seaweed[1].getHeight() / 5);
        seaweed[1].setSize(seaweed[1].getWidth() * seafishGame.adjustWidth, seaweed[1].getHeight() * seafishGame.adjustHeight);

        seaweed[2] = new Sprite(new Texture("images/ornaments/alga2.png"));
        seaweed[2].setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) / 2, -seaweed[2].getHeight() / 5);
        seaweed[2].setSize(seaweed[2].getWidth() * seafishGame.adjustWidth, seaweed[2].getHeight() * seafishGame.adjustHeight);
    }

    public void changeLoginButton(String name) {
        userName = name;
        if (seafishGame.facebookAuth.isLoggedIn()) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    loginButton = new Texture("images/buttons/logout.png");
                }
            });
        } else {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    loginButton = new Texture("images/buttons/login.png");
                }
            });
        }
    }

    @Override
    public void userLoggedIn(String name, Long personalRecord) {
        changeLoginButton(name);
        if (personalRecord != null && personalRecord > seafishGame.record) {
            seafishGame.prefs.putInteger("score", personalRecord.intValue());
            seafishGame.prefs.flush();
            seafishGame.record = personalRecord.intValue();
        }
    }

    @Override
    public void userLoggedOut() {
        changeLoginButton(null);
    }
}