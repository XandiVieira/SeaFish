package com.xandi.seafish.screens;

import com.badlogic.gdx.Screen;
import com.xandi.seafish.SeafishGame;

public abstract class BaseScreen implements Screen {

    private SeafishGame seafishGame;

    public BaseScreen(SeafishGame seafishGame) {
        this.seafishGame = seafishGame;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}