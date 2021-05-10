package com.xandi.seafish;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.xandi.seafish.interfaces.AdService;
import com.xandi.seafish.interfaces.FacebookAuth;
import com.xandi.seafish.interfaces.GoogleServices;
import com.xandi.seafish.interfaces.LoginCallback;
import com.xandi.seafish.interfaces.PrivacyPolicyAndTerms;
import com.xandi.seafish.interfaces.RankingInterface;
import com.xandi.seafish.interfaces.VideoEventListener;
import com.xandi.seafish.screens.GameScreen;
import com.xandi.seafish.screens.LoginScreen;
import com.xandi.seafish.screens.MenuScreen;

import java.util.Random;

public class SeafishGame extends Game implements VideoEventListener, LoginCallback {

    //Mantém dados salvos
    private Preferences prefs;

    //Controla anúncios
    public AdService handler;
    public GoogleServices googleServices;

    public RankingInterface rankingInterface;
    public FacebookAuth facebookAuth;
    public PrivacyPolicyAndTerms privacyPolicyAndTerms;
    //batch
    private SpriteBatch batch;

    public float originalWidth;
    public float differenceBetweenWidth;
    public float width;
    public float height;
    public float adjustHeight;
    public float adjustWidth;
    public double heightStandard = 1080;
    public double widthStandard = 1920;

    //Score
    private int metragem, metersSpeed;
    private BitmapFont metersLabel;
    private static GlyphLayout metersLayout;
    private float metersScore;
    public Integer record;
    private BitmapFont recordLabel;
    private static GlyphLayout recordLayout;

    private BitmapFont tap, userNameFont;
    private String userName;

    //Metros enquanto tubarão
    private float countSharkMeters;

    //Formas para sobrepor botoes
    private Sprite peixe, menuSprite, playSprite, loginSprite, rankingSprite, termsSprite, privacyPolicySprite, replaySprite, nextSprite, backSprite, simSprite, naoSprite, pauseSprite, musicSprite;
    private Sprite[] algas;

    //imagens
    private Texture telaInicial, gameOverText, reload, startGame, loginFb, ranking, terms, privacyPolicy, next, back, menuBotao, simBotao, naoBotao, continueText, videoIcon, bolhaInicio, pause, music;
    private Texture[] fundo, enfeite;
    private Sprite[][] peixes;
    private Sprite[] tubaroes;
    private Sprite[] cardumes;
    private Sprite[] poluicoes;
    private Sprite[] anzois;
    private Sprite[] minhocasScore;
    private Sprite minhocaBonus;
    private Sprite bolha;
    private Sprite[] obstaculos;

    //posições
    private float[] posicaoMovimentoObstaculoHorizontal;
    private float minhocaBonusHorizontal;
    private float bolhaHorizontal;
    private int[] distanciaMinhoca, alturaMinhoca;
    private float velocidade;
    private float velocidadeQueda;
    private float posicaoInicialVertical;
    private float[] movimentoBolhaHorizontal;
    private float[] movimentoBolhaVertical;
    private float[] movimentoAnzolVertical;
    private float movimentoEnfeiteHorizontal;

    //counters
    private int estado; //0=menu - 1=iniciado
    private int numMinhocas;
    private int contaPartidas = 1;
    private int contaFundo;
    private int contaMetrosSetor;
    private int obstaculoAtualSetor;
    private int toques;
    private int toquesParaSoltar;
    private int contaEnfeite;

    //Controllers
    private boolean iniciado; //Começar os movimentos depois do primeiro toque
    private boolean setor;
    private boolean colide;
    private boolean gameOver;
    private boolean colidiuObstaculo;
    private boolean isColiding;
    private boolean[] colidiuMinhoca;
    private boolean pausa;
    private boolean isSlowShark;
    private boolean isShark;
    private boolean mostraTelaSegueJogo;
    private boolean isRewarded;
    private boolean isRewardedYet;
    private boolean mostraMinhocaBonus;
    private boolean mostraBolha;
    private boolean[] minhocaColidiu;
    private boolean voltando;
    private boolean vidaExtra;
    private boolean[] bolhaTocouTopo;
    private boolean[] anzolTocouTopo;
    private boolean[] bolhaTocouLado;

    //Sorteios
    private Random obstaculoRandom;
    private int[] numObstaculo;
    private Random alturaObstaculoRandom;
    private float[] alturaObstaculo;
    private Random distanciaMinhocaRandom, alturaMinhocaRandom;
    private Random sorteioMetrosPorSetor;
    private int metrosPorSetor;
    private Random numeroPoluicao;
    private Random posicaoBolha;

    //Formas para colisões
    private Circle peixeCircle, bolhaCircle;
    private Rectangle minhocaBonusRect;
    private Rectangle[] minhocasRect, piranhasVerticalRect, piranhasHorizontalRect, tubaroesRect;
    private Circle[] anzoisCircle, poluicoesCircle;

    //Numero do peixe
    public int fishNumber;

    //Rotação algas
    private boolean controlRotation;

    //Sons
    private Sound eatSound, hitSound, gameOverSound, bubbleSound, blowBubbleSound;
    private Music backgroundMusic;

    //Variações
    private int variacaoPeixe;
    private float variacaoPeixeAux;
    private float variacaoTubarao;
    private int variacaoAlga;
    private float variacaoAlgaAux;
    private float variacaoCardume;

    //Constantes
    private float AUMENTA_VELOCIDADE;
    private float ALTURA_SALTO;
    private float POSICAO_HORIZONTAL_PEIXE;
    private float ALTURA_TOPO;
    private float VELOCIDADE_QUEDA;
    private float VELOCIDADE_OBSTACULO;
    private float TAP_X;
    private float TAP_Y;
    private float VELOCIDADE_INICIAL;
    private int VELOCIDADE_METROS_INICIAL;
    private int TOQUES_ANZOL1;
    private int TOQUES_ANZOL2;
    private int TOQUES_ANZOL3;
    private int TOQUES_ANZOL4;
    private float ALTURA_SELECT_PEIXE;

    private Viewport viewport;

    private int caughtWarms;
    private int caughtSpecialWarms;
    private int caughtBubbles;
    private int caughtByHook;
    private int turnedShark;
    private String deathTackle;

    SeafishGame(AdService handler, GoogleServices googleServices, RankingInterface rankingInterface, FacebookAuth facebookAuth, PrivacyPolicyAndTerms privacyPolicyAndTerms) {
        this.handler = handler;
        this.googleServices = googleServices;
        this.googleServices.setVideoEventListener(this);
        this.rankingInterface = rankingInterface;
        this.facebookAuth = facebookAuth;
        this.facebookAuth.setLoginCallback(this);
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

    private void menuState() {
        handler.showBannerAd(true);
        //Se o botão iniciar jogo for clicado
        if (Gdx.input.justTouched()) {
            if (playSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (fishNumber == 0 || fishNumber == 1 || fishNumber == 2 || (fishNumber == 3 && record >= 2000) || (fishNumber == 4 && record >= 4000) || (fishNumber == 5 && record >= 6000) || (fishNumber == 6 && record >= 8000)) {
                    estado = 1;
                    gameOver = false;
                }
            }

            if (loginSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (facebookAuth.isLoggedIn()) {
                    facebookAuth.logout();
                } else {
                    facebookAuth.login();
                }
            }

            if (rankingSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                rankingInterface.callRanking();
            }

            if (termsSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                privacyPolicyAndTerms.callTerms();
            }

            if (privacyPolicySprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                privacyPolicyAndTerms.callPrivacyPolicy();
            }
        }

        batch.begin();

        batch.draw(telaInicial, 0, 0, width, height);
        batch.draw(startGame, ((width - differenceBetweenWidth) / 2) - (startGame.getWidth() * adjustWidth / 2), ((height) - (startGame.getHeight() * adjustHeight * 3)), startGame.getWidth() * adjustWidth, startGame.getHeight() * adjustHeight);

        batch.draw(terms, (width - differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * adjustWidth), ((privacyPolicy.getHeight() / 1.5f) * adjustHeight) + (privacyPolicy.getHeight() * adjustHeight * 1.2f), (terms.getWidth() / 2f) * adjustWidth, terms.getHeight() * adjustHeight);
        batch.draw(privacyPolicy, (width - differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * adjustWidth), ((privacyPolicy.getHeight() / 1.5f) * adjustHeight), (privacyPolicy.getWidth() / 2f) * adjustWidth, privacyPolicy.getHeight() * adjustHeight);

        batch.draw(ranking, (ranking.getWidth() * adjustWidth) / 2.5f, (height / 2f), ranking.getWidth() * adjustWidth, ranking.getHeight() * adjustHeight);
        batch.draw(loginFb, (loginFb.getWidth() * adjustWidth) / 10f, (height / 2f) - (ranking.getHeight() * adjustHeight), (loginFb.getWidth() * adjustWidth / 2f), loginFb.getHeight() * adjustHeight);

        if (userName != null) {
            userNameFont.draw(batch, userName, (loginFb.getWidth() * adjustWidth) / 10f, (height / 2f) - (loginFb.getHeight() * adjustHeight * 2f) - (loginFb.getHeight() * adjustHeight) / 10f);
        }

        algas[variacaoAlga].draw(batch);
        algas[2].draw(batch);
        batch.draw(back, ((width - differenceBetweenWidth) / 2) - (back.getWidth() * 1.85f * adjustWidth), ALTURA_SELECT_PEIXE + (back.getHeight() * adjustHeight), back.getWidth() * adjustWidth, back.getHeight() * adjustHeight);
        batch.draw(next, ((width - differenceBetweenWidth) / 2) + (next.getWidth() * adjustWidth), ALTURA_SELECT_PEIXE + (next.getHeight() * adjustHeight), next.getWidth() * adjustWidth, next.getHeight() * adjustHeight);
        batch.draw(peixes[fishNumber][variacaoPeixe], ((width - differenceBetweenWidth) / 2) - (peixes[fishNumber][0].getWidth() * adjustWidth / 2), ALTURA_SELECT_PEIXE + 70, peixes[fishNumber][0].getWidth() * adjustWidth, peixes[fishNumber][0].getHeight() * adjustHeight);

        for (int i = 0; i < 10; i++) {
            if (movimentoBolhaHorizontal[i] >= (width - differenceBetweenWidth) - bolhaInicio.getWidth() * adjustWidth) {
                bolhaTocouLado[i] = true;
            }

            if (movimentoBolhaHorizontal[i] <= 0) {
                bolhaTocouLado[i] = false;
            }

            if (bolhaTocouLado[i]) {
                movimentoBolhaHorizontal[i] -= (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(50) + 1) * adjustWidth;
            } else {
                movimentoBolhaHorizontal[i] += (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(200) + 1) * adjustWidth;
            }

            if (movimentoBolhaVertical[i] >= height - bolhaInicio.getHeight() * adjustHeight) {
                bolhaTocouTopo[i] = true;
            }

            if (movimentoBolhaVertical[i] <= 0) {
                bolhaTocouTopo[i] = false;
            }

            if (bolhaTocouTopo[i]) {
                movimentoBolhaVertical[i] -= (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(100) + 1) * adjustHeight;
            } else {
                movimentoBolhaVertical[i] += (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(150) + 1) * adjustHeight;
            }
            batch.draw(bolhaInicio, movimentoBolhaHorizontal[i], movimentoBolhaVertical[i], bolhaInicio.getWidth() * adjustWidth, bolhaInicio.getHeight() * adjustWidth);
        }

        batch.end();

        controlSeaweedRotation(false);
        varySeaweed();
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

    public void controlSeaweedRotation(boolean changePosition) {
        if (controlRotation) {
            if (algas[2].getRotation() <= -35 * (adjustWidth)) {
                controlRotation = false;
            }
            algas[2].rotate((float) -0.4 * adjustWidth);
        } else {
            if (algas[2].getRotation() >= 20 * (adjustWidth)) {
                controlRotation = true;
            }
            algas[2].rotate((float) 0.4 * adjustWidth);
        }
        if (changePosition) {
            algas[variacaoAlga].setPosition(posicaoMovimentoObstaculoHorizontal[variacaoAlga], ((-algas[variacaoAlga].getTexture().getHeight()) * adjustHeight) / 5);
            algas[2].setPosition(posicaoMovimentoObstaculoHorizontal[2], ((-algas[2].getTexture().getHeight()) * adjustHeight) / 5);
        }
    }


    private void setButtons() {
        //Sobrepor Botões
        menuSprite = new Sprite(menuBotao);
        menuSprite.setSize(menuSprite.getWidth() * adjustWidth, menuSprite.getHeight() * adjustHeight);
        menuSprite.setPosition(((width - differenceBetweenWidth) / 2) - (menuBotao.getWidth() * adjustWidth / 2), (float) ((height / 3) - menuSprite.getHeight() * adjustHeight * 1.5));

        simSprite = new Sprite(simBotao);
        simSprite.setSize(simSprite.getWidth() * adjustWidth, simSprite.getHeight() * adjustHeight);
        simSprite.setPosition((float) (((width - differenceBetweenWidth) / 2) - (simBotao.getWidth() * adjustWidth * 1.5)), (float) ((height / 3) - simSprite.getHeight() * adjustHeight * 1.5));

        naoSprite = new Sprite(naoBotao);
        naoSprite.setSize(naoSprite.getWidth() * adjustWidth, naoSprite.getHeight() * adjustHeight);
        naoSprite.setPosition((((width - differenceBetweenWidth) / 2) + (naoBotao.getWidth() * adjustWidth)), (float) ((height / 3) - naoSprite.getHeight() * adjustHeight * 1.5));

        playSprite = new Sprite(startGame);
        playSprite.setSize(startGame.getWidth() * adjustWidth, startGame.getHeight() * adjustHeight);
        playSprite.setPosition(((width - differenceBetweenWidth) / 2) - (startGame.getWidth() * adjustWidth / 2), ((height) - (startGame.getHeight() * adjustHeight * 3)));

        loginSprite = new Sprite(loginFb);
        loginSprite.setSize((loginFb.getWidth() / 2f) * adjustWidth, loginFb.getHeight() * adjustHeight);
        loginSprite.setPosition((loginFb.getWidth() * adjustWidth) / 10f, (height / 2f) - (ranking.getHeight() * adjustHeight));

        rankingSprite = new Sprite(ranking);
        rankingSprite.setSize((ranking.getWidth()) * adjustWidth, ranking.getHeight() * adjustHeight);
        rankingSprite.setPosition((ranking.getWidth() * adjustWidth) / 2.5f, (height / 2f));

        termsSprite = new Sprite(terms);
        termsSprite.setSize((terms.getWidth() / 2f) * adjustWidth, terms.getHeight() * adjustHeight);
        termsSprite.setPosition((width - differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * adjustWidth), ((privacyPolicy.getHeight() / 1.5f) * adjustHeight) + (privacyPolicy.getHeight() * adjustHeight * 1.2f));

        privacyPolicySprite = new Sprite(privacyPolicy);
        privacyPolicySprite.setSize((privacyPolicy.getWidth() / 2f) * adjustWidth, privacyPolicy.getHeight() * adjustHeight);
        privacyPolicySprite.setPosition((width - differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * adjustWidth), ((privacyPolicy.getHeight() / 1.5f) * adjustHeight));

        replaySprite = new Sprite(reload);
        replaySprite.setSize(150 * adjustWidth, 150 * adjustHeight);
        replaySprite.setPosition(((width - differenceBetweenWidth) / 2) - (reload.getWidth() * adjustWidth), height / 2);

        nextSprite = new Sprite(next);
        nextSprite.setSize(next.getWidth() * adjustWidth, next.getHeight() * adjustHeight);
        nextSprite.setPosition(((width - differenceBetweenWidth) / 2) + (next.getWidth() * adjustWidth * 1.5f), ALTURA_SELECT_PEIXE + (next.getHeight() * adjustHeight));

        backSprite = new Sprite(back);
        backSprite.setSize(back.getWidth() * adjustWidth, back.getHeight() * adjustHeight);
        backSprite.setPosition(((width - differenceBetweenWidth) / 2) - (back.getWidth() * 1.5f * adjustWidth), ALTURA_SELECT_PEIXE + (back.getHeight() * adjustHeight));
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

    public void changeLoginButton(String name) {
        userName = name;
        if (facebookAuth.isLoggedIn()) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    loginFb = new Texture("images/buttons/logout.png");
                }
            });
        } else {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    loginFb = new Texture("images/buttons/login.png");
                }
            });
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void userLoggedIn(String name, Long personalRecord) {
        changeLoginButton(name);
        if (personalRecord != null && personalRecord > record) {
            prefs.putInteger("score", personalRecord.intValue());
            prefs.flush();
            record = personalRecord.intValue();
            recordLayout = new GlyphLayout();
            recordLayout.setText(recordLabel, "Record: " + record + "m");
        }
    }

    @Override
    public void userLoggedOut() {
        changeLoginButton(null);
    }
}