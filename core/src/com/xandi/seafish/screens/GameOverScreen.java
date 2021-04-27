package com.xandi.seafish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.xandi.seafish.SeafishGame;

public class GameOverScreen extends BaseScreen {

    private SeafishGame seafishGame;
    private boolean gameOver;
    //Formas para sobrepor botoes
    private Sprite menuSprite, replaySprite;
    //Numero do peixe
    private int fishNumber;
    private float metersScore;
    private int contaPartidas = 1;

    public GameOverScreen(SeafishGame seafishGame) {
        super(seafishGame);
        this.seafishGame = seafishGame;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        //Botões game over
        if (Gdx.input.justTouched()) {
            if (gameOver) {
                if (replaySprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    gameOver = false;
                    int peixe = fishNumber;
                    System.gc();
                    seafishGame.setScreen(new GameScreen(seafishGame));
                    if (metersScore < 300) {
                        contaPartidas++;
                    }
                    fishNumber = peixe;
                }
                if (menuSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    System.gc();
                    seafishGame.setScreen(new MenuScreen(seafishGame));
                    if (metersScore < 300) {
                        contaPartidas++;
                    }
                }
            }
        }
    }
}