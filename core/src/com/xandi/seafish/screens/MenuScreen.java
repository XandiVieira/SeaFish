package com.xandi.seafish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xandi.seafish.SeafishGame;

import java.util.Random;

public class MenuScreen extends BaseScreen {

    private final SeafishGame seafishGame;
    private Texture background, loginButton, ranking, startButton, terms, privacyPolicy, next, back, bolhaInicio;
    private Sprite playSprite, loginSprite, rankingSprite, termsSprite, privacyPolicySprite, nextSprite, backSprite;
    private SpriteBatch batch;
    private Sprite[] algas;
    private Sprite[][] peixes;
    private float ALTURA_SELECT_PEIXE;
    private float variacaoAlgaAux;
    private BitmapFont userNameFont;
    private String userName;
    private int variacaoAlga;
    //Numero do peixe
    private int fishNumber;
    private int variacaoPeixe;
    private float variacaoPeixeAux;
    private float[] movimentoBolhaHorizontal;
    private float[] movimentoBolhaVertical;
    private boolean[] bolhaTocouTopo;
    private boolean[] bolhaTocouLado;
    private Random posicaoBolha;

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
        peixes = new Sprite[7][5];
        movimentoBolhaHorizontal = new float[10];
        movimentoBolhaVertical = new float[10];
        bolhaTocouTopo = new boolean[10];
        bolhaTocouLado = new boolean[10];
        posicaoBolha = new Random();
        for (int i = 0; i < 10; i++) {
            movimentoBolhaHorizontal[i] = (float) posicaoBolha.nextInt((int) seafishGame.width * 2);
            movimentoBolhaVertical[i] = (float) posicaoBolha.nextInt((int) seafishGame.height * 2);
        }
        setTextures();
        setButtons();
        ALTURA_SELECT_PEIXE = ((seafishGame.height) - (startButton.getHeight() * seafishGame.adjustHeight * 5));
        setSeaweed();
        setFishes();
        variacaoPeixe = 0;
        variacaoPeixeAux = 0;
    }

    private void setTextures() {
        background = new Texture("images/scenarios/telainicio.png");
        loginButton = new Texture("images/buttons/login.png");
        startButton = new Texture("images/buttons/startgame.png");
        ranking = new Texture("images/buttons/ranking.png");
        terms = new Texture("images/buttons/terms.png");
        privacyPolicy = new Texture("images/buttons/privacypolicy.png");
        next = new Texture("images/buttons/next.png");
        back = new Texture("images/buttons/back.png");
        bolhaInicio = new Texture("images/ornaments/bolhainicio.png");
    }

    private void setButtons() {
        playSprite = new Sprite(startButton);
        playSprite.setSize(startButton.getWidth() * seafishGame.adjustWidth, startButton.getHeight() * seafishGame.adjustHeight);
        playSprite.setPosition(((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (startButton.getWidth() * seafishGame.adjustWidth / 2), ((seafishGame.height) - (startButton.getHeight() * seafishGame.adjustHeight * 3)));

        loginSprite = new Sprite(loginButton);
        loginSprite.setSize((loginButton.getWidth() / 2f) * seafishGame.adjustWidth, loginButton.getHeight() * seafishGame.adjustHeight);
        loginSprite.setPosition((loginButton.getWidth() * seafishGame.adjustWidth) / 10f, (seafishGame.height / 2f) - (ranking.getHeight() * seafishGame.adjustHeight));

        rankingSprite = new Sprite(ranking);
        rankingSprite.setSize((ranking.getWidth()) * seafishGame.adjustWidth, ranking.getHeight() * seafishGame.adjustHeight);
        rankingSprite.setPosition((ranking.getWidth() * seafishGame.adjustWidth) / 2.5f, (seafishGame.height / 2f));

        termsSprite = new Sprite(terms);
        termsSprite.setSize((terms.getWidth() / 2f) * seafishGame.adjustWidth, terms.getHeight() * seafishGame.adjustHeight);
        termsSprite.setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * seafishGame.adjustWidth), ((privacyPolicy.getHeight() / 1.5f) * seafishGame.adjustHeight) + (privacyPolicy.getHeight() * seafishGame.adjustHeight * 1.2f));

        privacyPolicySprite = new Sprite(privacyPolicy);
        privacyPolicySprite.setSize((privacyPolicy.getWidth() / 2f) * seafishGame.adjustWidth, privacyPolicy.getHeight() * seafishGame.adjustHeight);
        privacyPolicySprite.setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * seafishGame.adjustWidth), ((privacyPolicy.getHeight() / 1.5f) * seafishGame.adjustHeight));

        nextSprite = new Sprite(next);
        nextSprite.setSize(next.getWidth() * seafishGame.adjustWidth, next.getHeight() * seafishGame.adjustHeight);
        nextSprite.setPosition(((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) + (next.getWidth() * seafishGame.adjustWidth), ALTURA_SELECT_PEIXE + ((next.getHeight() * 2) * seafishGame.adjustHeight));

        backSprite = new Sprite(back);
        backSprite.setSize(back.getWidth() * seafishGame.adjustWidth, back.getHeight() * seafishGame.adjustHeight);
        backSprite.setPosition(((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (back.getWidth() * 1.85f * seafishGame.adjustWidth), ALTURA_SELECT_PEIXE + ((back.getHeight() * 2) * seafishGame.adjustHeight));
    }

    private void setFishes() {
        peixes[0][0] = new Sprite(new Texture("images/characters/peixe1.png"));
        peixes[0][1] = new Sprite(new Texture("images/characters/peixe12.png"));
        peixes[0][2] = new Sprite(new Texture("images/characters/peixe1red.png"));
        peixes[0][3] = new Sprite(new Texture("images/characters/tubarao1.png"));
        peixes[0][4] = new Sprite(new Texture("images/characters/tubarao12.png"));
        peixes[1][0] = new Sprite(new Texture("images/characters/peixe2.png"));
        peixes[1][1] = new Sprite(new Texture("images/characters/peixe22.png"));
        peixes[1][2] = new Sprite(new Texture("images/characters/peixe2red.png"));
        peixes[1][3] = new Sprite(new Texture("images/characters/tubarao2.png"));
        peixes[1][4] = new Sprite(new Texture("images/characters/tubarao22.png"));
        peixes[2][0] = new Sprite(new Texture("images/characters/peixe3.png"));
        peixes[2][1] = new Sprite(new Texture("images/characters/peixe32.png"));
        peixes[2][2] = new Sprite(new Texture("images/characters/peixe3red.png"));
        peixes[2][3] = new Sprite(new Texture("images/characters/tubarao3.png"));
        peixes[2][4] = new Sprite(new Texture("images/characters/tubarao32.png"));

        if (seafishGame.record < 2000) {
            peixes[3][0] = new Sprite(new Texture("images/characters/sombra1.png"));
            peixes[3][1] = new Sprite(new Texture("images/characters/sombra1.png"));
        } else {
            peixes[3][0] = new Sprite(new Texture("images/characters/peixe4.png"));
            peixes[3][1] = new Sprite(new Texture("images/characters/peixe42.png"));
            peixes[3][2] = new Sprite(new Texture("images/characters/peixe4red.png"));
            peixes[3][3] = new Sprite(new Texture("images/characters/tubarao4.png"));
            peixes[3][4] = new Sprite(new Texture("images/characters/tubarao42.png"));
        }

        if (seafishGame.record < 4000) {
            peixes[4][0] = new Sprite(new Texture("images/characters/sombra2.png"));
            peixes[4][1] = new Sprite(new Texture("images/characters/sombra2.png"));
        } else {
            peixes[4][0] = new Sprite(new Texture("images/characters/peixe5.png"));
            peixes[4][1] = new Sprite(new Texture("images/characters/peixe52.png"));
            peixes[4][2] = new Sprite(new Texture("images/characters/peixe5red.png"));
            peixes[4][3] = new Sprite(new Texture("images/characters/tubarao5.png"));
            peixes[4][4] = new Sprite(new Texture("images/characters/tubarao52.png"));
        }

        if (seafishGame.record < 6000) {
            peixes[5][0] = new Sprite(new Texture("images/characters/sombra3.png"));
            peixes[5][1] = new Sprite(new Texture("images/characters/sombra3.png"));
        } else {
            peixes[5][0] = new Sprite(new Texture("images/characters/peixe6.png"));
            peixes[5][1] = new Sprite(new Texture("images/characters/peixe62.png"));
            peixes[5][2] = new Sprite(new Texture("images/characters/peixe6red.png"));
            peixes[5][3] = new Sprite(new Texture("images/characters/tubarao6.png"));
            peixes[5][4] = new Sprite(new Texture("images/characters/tubarao62.png"));
        }

        if (seafishGame.record < 8000) {
            peixes[6][0] = new Sprite(new Texture("images/characters/sombra4.png"));
            peixes[6][1] = new Sprite(new Texture("images/characters/sombra4.png"));
        } else {
            peixes[6][0] = new Sprite(new Texture("images/characters/peixe7.png"));
            peixes[6][1] = new Sprite(new Texture("images/characters/peixe72.png"));
            peixes[6][2] = new Sprite(new Texture("images/characters/peixe7red.png"));
            peixes[6][3] = new Sprite(new Texture("images/characters/tubarao7.png"));
            peixes[6][4] = new Sprite(new Texture("images/characters/tubarao72.png"));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (Gdx.input.justTouched()) {
            if (Gdx.input.justTouched()) {
                if (playSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    if (fishNumber == 0 || fishNumber == 1 || fishNumber == 2 || (fishNumber == 3 && seafishGame.record >= 2000) || (fishNumber == 4 && seafishGame.record >= 4000) || (fishNumber == 5 && seafishGame.record >= 6000) || (fishNumber == 6 && seafishGame.record >= 8000)) {
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

        batch.draw(terms, (seafishGame.width - seafishGame.differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * seafishGame.adjustWidth), ((privacyPolicy.getHeight() / 1.5f) * seafishGame.adjustHeight) + (privacyPolicy.getHeight() * seafishGame.adjustHeight * 1.2f), (terms.getWidth() / 2f) * seafishGame.adjustWidth, terms.getHeight() * seafishGame.adjustHeight);
        batch.draw(privacyPolicy, (seafishGame.width - seafishGame.differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * seafishGame.adjustWidth), ((privacyPolicy.getHeight() / 1.5f) * seafishGame.adjustHeight), (privacyPolicy.getWidth() / 2f) * seafishGame.adjustWidth, privacyPolicy.getHeight() * seafishGame.adjustHeight);

        batch.draw(ranking, (ranking.getWidth() * seafishGame.adjustWidth) / 2.5f, (seafishGame.height / 2f), ranking.getWidth() * seafishGame.adjustWidth, ranking.getHeight() * seafishGame.adjustHeight);
        batch.draw(loginButton, (loginButton.getWidth() * seafishGame.adjustWidth) / 10f, (seafishGame.height / 2f) - (ranking.getHeight() * seafishGame.adjustHeight), (loginButton.getWidth() * seafishGame.adjustWidth / 2f), loginButton.getHeight() * seafishGame.adjustHeight);

        if (userName != null) {
            userNameFont.draw(batch, userName, (loginButton.getWidth() * seafishGame.adjustWidth) / 10f, (seafishGame.height / 2f) - (loginButton.getHeight() * seafishGame.adjustHeight * 2f) - (loginButton.getHeight() * seafishGame.adjustHeight) / 10f);
        }

        algas[variacaoAlga].draw(batch);
        algas[2].draw(batch);
        batch.draw(back, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (back.getWidth() * 1.85f * seafishGame.adjustWidth), ALTURA_SELECT_PEIXE + (back.getHeight() * seafishGame.adjustHeight), back.getWidth() * seafishGame.adjustWidth, back.getHeight() * seafishGame.adjustHeight);
        batch.draw(next, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) + (next.getWidth() * seafishGame.adjustWidth), ALTURA_SELECT_PEIXE + (next.getHeight() * seafishGame.adjustHeight), next.getWidth() * seafishGame.adjustWidth, next.getHeight() * seafishGame.adjustHeight);
        batch.draw(peixes[fishNumber][variacaoPeixe], ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (peixes[fishNumber][0].getWidth() * seafishGame.adjustWidth / 2), ALTURA_SELECT_PEIXE + 70, peixes[fishNumber][0].getWidth() * seafishGame.adjustWidth, peixes[fishNumber][0].getHeight() * seafishGame.adjustHeight);

        for (int i = 0; i < 10; i++) {
            if (movimentoBolhaHorizontal[i] >= (seafishGame.width - seafishGame.differenceBetweenWidth) - bolhaInicio.getWidth() * seafishGame.adjustWidth) {
                bolhaTocouLado[i] = true;
            }

            if (movimentoBolhaHorizontal[i] <= 0) {
                bolhaTocouLado[i] = false;
            }

            if (bolhaTocouLado[i]) {
                movimentoBolhaHorizontal[i] -= (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(50) + 1) * seafishGame.adjustWidth;
            } else {
                movimentoBolhaHorizontal[i] += (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(200) + 1) * seafishGame.adjustWidth;
            }

            if (movimentoBolhaVertical[i] >= seafishGame.height - bolhaInicio.getHeight() * seafishGame.adjustHeight) {
                bolhaTocouTopo[i] = true;
            }

            if (movimentoBolhaVertical[i] <= 0) {
                bolhaTocouTopo[i] = false;
            }

            if (bolhaTocouTopo[i]) {
                movimentoBolhaVertical[i] -= (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(100) + 1) * seafishGame.adjustHeight;
            } else {
                movimentoBolhaVertical[i] += (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(150) + 1) * seafishGame.adjustHeight;
            }
            batch.draw(bolhaInicio, movimentoBolhaHorizontal[i], movimentoBolhaVertical[i], bolhaInicio.getWidth() * seafishGame.adjustWidth, bolhaInicio.getHeight() * seafishGame.adjustWidth);
        }
        batch.end();

        controlSeaweedRotation();
        varySeaweed();
        varyFish();
        selectFish();
    }

    private void varyFish() {
        if (variacaoPeixeAux > 2) {
            variacaoPeixeAux = 0;
        } else if (variacaoPeixeAux >= 1) {
            variacaoPeixe = 0;
        } else {
            variacaoPeixe = 1;
        }
    }

    public void controlSeaweedRotation() {
        if (controlRotation) {
            if (algas[2].getRotation() <= -35 * (seafishGame.adjustWidth)) {
                controlRotation = false;
            }
            algas[2].rotate((float) -0.4 * seafishGame.adjustWidth);
        } else {
            if (algas[2].getRotation() >= 20 * (seafishGame.adjustWidth)) {
                controlRotation = true;
            }
            algas[2].rotate((float) 0.4 * seafishGame.adjustWidth);
        }
    }

    private void varySeaweed() {
        if (variacaoAlgaAux > 1) {
            variacaoAlgaAux = 0;
        }
        if (variacaoAlgaAux >= 0.5) {
            variacaoAlga = 0;
        } else {
            variacaoAlga = 1;
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
        algas = new Sprite[3];
        algas[0] = new Sprite(new Texture("images/ornaments/alga1.png"));
        algas[0].setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) / 10, -algas[0].getHeight() / 5);
        algas[0].setSize(algas[0].getWidth() * seafishGame.adjustWidth, algas[0].getHeight() * seafishGame.adjustHeight);

        algas[1] = new Sprite(new Texture("images/ornaments/algaVaria.png"));
        algas[1].setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) / 10, -algas[1].getHeight() / 5);
        algas[1].setSize(algas[1].getWidth() * seafishGame.adjustWidth, algas[1].getHeight() * seafishGame.adjustHeight);

        algas[2] = new Sprite(new Texture("images/ornaments/alga2.png"));
        algas[2].setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) / 2, -algas[2].getHeight() / 5);
        algas[2].setSize(algas[2].getWidth() * seafishGame.adjustWidth, algas[2].getHeight() * seafishGame.adjustHeight);
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
}