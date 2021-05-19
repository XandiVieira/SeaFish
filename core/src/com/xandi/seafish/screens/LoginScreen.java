package com.xandi.seafish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.xandi.seafish.SeafishGame;
import com.xandi.seafish.interfaces.LoginCallback;

import java.util.Random;

public class LoginScreen extends BaseScreen implements LoginCallback {

    private final SeafishGame seafishGame;
    private Sprite menuSprite, loginSprite;
    private Texture background, menuButton, loginButton;
    private Texture[] movingBubble;

    private float[] bubbleHorizontalMovement;
    private float[] bubbleVerticalMovement;
    private boolean[] bubbleTouchedTop;
    private boolean[] BubbleTouchedSide;
    private Random bubblePosition;
    private boolean hasPassedByShow = false;

    public LoginScreen(SeafishGame seafishGame) {
        super(seafishGame);
        this.seafishGame = seafishGame;
    }

    @Override
    public void show() {
        super.show();
        seafishGame.facebookAuth.setLoginCallback(this);
        seafishGame.handler.showBannerAd(true);
        bubbleHorizontalMovement = new float[10];
        bubbleVerticalMovement = new float[10];
        bubbleTouchedTop = new boolean[10];
        BubbleTouchedSide = new boolean[10];
        movingBubble = new Texture[10];
        bubblePosition = new Random();
        for (int i = 0; i < 10; i++) {
            bubbleHorizontalMovement[i] = (float) bubblePosition.nextInt((int) seafishGame.width * 2);
            bubbleVerticalMovement[i] = (float) bubblePosition.nextInt((int) seafishGame.height * 2);
        }

        setTextures();
        setButtons();
        hasPassedByShow = true;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    private void setTextures() {
        background = new Texture("images/scenarios/background.jpeg");
        menuButton = new Texture("images/buttons/menu.png");
        loginButton = new Texture("images/buttons/login.png");
        setBubbles();
    }

    private void setBubbles() {
        movingBubble[0] = new Texture("images/ornaments/bubblefish1.png");
        movingBubble[1] = new Texture("images/ornaments/bubblefish2.png");
        movingBubble[2] = new Texture("images/ornaments/bubblefish3.png");
        if (seafishGame.record >= 2000) {
            movingBubble[3] = new Texture("images/ornaments/bubblefish4.png");
        } else {
            movingBubble[3] = new Texture("images/ornaments/bubblefishshadow4.png");
        }

        if (seafishGame.record >= 4000) {
            movingBubble[4] = new Texture("images/ornaments/bubblefish5.png");
        } else {
            movingBubble[4] = new Texture("images/ornaments/bubblefishshadow5.png");
        }

        if (seafishGame.record >= 6000) {
            movingBubble[5] = new Texture("images/ornaments/bubblefish6.png");
        } else {
            movingBubble[5] = new Texture("images/ornaments/bubblefishshadow6.png");
        }

        if (seafishGame.record >= 8000) {
            movingBubble[6] = new Texture("images/ornaments/bubblefish7.png");
        } else {
            movingBubble[6] = new Texture("images/ornaments/bubblefishshadow7.png");
        }
        movingBubble[7] = new Texture("images/ornaments/bolhainicio.png");
        movingBubble[8] = new Texture("images/ornaments/bolhainicio.png");
        movingBubble[9] = new Texture("images/ornaments/bolhainicio.png");
    }

    private void setButtons() {
        menuSprite = new Sprite(menuButton);
        menuSprite.setSize(menuSprite.getWidth() * seafishGame.adjustWidth, menuSprite.getHeight() * seafishGame.adjustHeight);
        menuSprite.setPosition(((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (menuButton.getWidth() * seafishGame.adjustWidth / 2), (float) ((seafishGame.height / 3) - menuSprite.getHeight() * seafishGame.adjustHeight * 1.5));

        loginSprite = new Sprite(loginButton);
        loginSprite.setSize((float) (loginButton.getWidth() * seafishGame.adjustWidth * 1.5), (float) (loginButton.getHeight() * seafishGame.adjustHeight * 1.5));
        loginSprite.setPosition((float) (((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - ((loginButton.getWidth() * seafishGame.adjustWidth * 1.5) / 2)), (float) (((seafishGame.height * seafishGame.adjustWidth) / 2f) - ((loginSprite.getHeight() * 1.5 * seafishGame.adjustHeight))));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!hasPassedByShow) {
            show();
        }

        if (Gdx.input.justTouched()) {
            if (loginSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (seafishGame.facebookAuth.isLoggedIn()) {
                    seafishGame.facebookAuth.logout();
                } else {
                    seafishGame.facebookAuth.login();
                }
            }

            if (menuSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                seafishGame.setScreen(new MenuScreen(seafishGame));
            }
        }

        seafishGame.batch.begin();
        seafishGame.batch.draw(background, 0, 0, seafishGame.width, seafishGame.height);
        seafishGame.batch.draw(loginButton, (float) (((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - ((loginButton.getWidth() * seafishGame.adjustWidth * 1.5) / 2)), (float) (((seafishGame.height * seafishGame.adjustWidth) / 2f) - ((loginSprite.getHeight() * 1.5 * seafishGame.adjustHeight))), (float) (loginButton.getWidth() * seafishGame.adjustWidth * 1.5), (float) (loginButton.getHeight() * seafishGame.adjustHeight * 1.5));
        seafishGame.batch.draw(menuButton, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (menuButton.getWidth() * seafishGame.adjustWidth / 2), (float) ((seafishGame.height / 3) - menuSprite.getHeight() * seafishGame.adjustHeight * 1.5), menuButton.getWidth() * seafishGame.adjustWidth, menuButton.getHeight() * seafishGame.adjustHeight);

        for (int i = 0; i < 10; i++) {
            if (bubbleHorizontalMovement[i] >= (seafishGame.width - seafishGame.differenceBetweenWidth) - movingBubble[i].getWidth() * seafishGame.adjustWidth) {
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

            if (bubbleVerticalMovement[i] >= seafishGame.height - movingBubble[i].getHeight() * seafishGame.adjustHeight) {
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
            seafishGame.batch.draw(movingBubble[i], bubbleHorizontalMovement[i], bubbleVerticalMovement[i], movingBubble[i].getWidth() * seafishGame.adjustWidth, movingBubble[i].getHeight() * seafishGame.adjustWidth);
        }

        seafishGame.batch.end();
    }

    @Override
    public void userLoggedIn(String userName, Long personalRecord) {
        seafishGame.setScreen(new MenuScreen(seafishGame));
    }

    @Override
    public void dispose() {
        super.dispose();
        seafishGame.batch.dispose();
    }

    @Override
    public void userLoggedOut() {

    }
}