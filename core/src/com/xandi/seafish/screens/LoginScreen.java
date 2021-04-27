package com.xandi.seafish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xandi.seafish.SeafishGame;

public class LoginScreen extends BaseScreen {

    private final SeafishGame seafishGame;
    private Sprite menuSprite, loginSprite;
    private Texture background, menuButton, loginButton;
    private SpriteBatch batch;

    public LoginScreen(SeafishGame seafishGame) {
        super(seafishGame);
        this.seafishGame = seafishGame;
    }

    @Override
    public void show() {
        super.show();
        setTextures();
        setButtons();
        batch = new SpriteBatch();
    }

    private void setTextures() {
        background = new Texture("images/scenarios/telainicio.png");
        menuButton = new Texture("images/buttons/menu.png");
        loginButton = new Texture("images/buttons/login.png");
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

        batch.begin();
        batch.draw(background, 0, 0, seafishGame.width, seafishGame.height);
        batch.draw(loginButton, (float) (((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - ((loginButton.getWidth() * seafishGame.adjustWidth * 1.5) / 2)), (float) (((seafishGame.height * seafishGame.adjustWidth) / 2f) - ((loginSprite.getHeight() * 1.5 * seafishGame.adjustHeight))), (float) (loginButton.getWidth() * seafishGame.adjustWidth * 1.5), (float) (loginButton.getHeight() * seafishGame.adjustHeight * 1.5));
        batch.draw(menuButton, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (menuButton.getWidth() * seafishGame.adjustWidth / 2), (float) ((seafishGame.height / 3) - menuSprite.getHeight() * seafishGame.adjustHeight * 1.5), menuButton.getWidth() * seafishGame.adjustWidth, menuButton.getHeight() * seafishGame.adjustHeight);
        batch.end();
    }
}