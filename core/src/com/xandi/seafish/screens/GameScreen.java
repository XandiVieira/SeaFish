package com.xandi.seafish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.xandi.seafish.SeafishGame;
import com.xandi.seafish.interfaces.AdService;
import com.xandi.seafish.interfaces.FacebookAuth;
import com.xandi.seafish.interfaces.GoogleServices;
import com.xandi.seafish.interfaces.PrivacyPolicyAndTerms;
import com.xandi.seafish.interfaces.RankingInterface;
import com.xandi.seafish.interfaces.VideoEventListener;

import java.util.Arrays;
import java.util.Random;

public class GameScreen extends BaseScreen implements VideoEventListener {

    private SeafishGame seafishGame;
    //Mantém dados salvos
    private Preferences prefs;

    //Controla anúncios
    private AdService handler;
    private GoogleServices googleServices;

    private RankingInterface rankingInterface;
    private FacebookAuth facebookAuth;
    private PrivacyPolicyAndTerms privacyPolicyAndTerms;
    //batch
    private SpriteBatch batch;

    private float originalWidth;
    private float differenceBetweenWidth;
    private float width;
    private float height;
    private float adjustHeight;
    private float adjustWidth;
    private double heightStandard = 1080;
    private double widthStandard = 1920;

    //Score
    private int metragem, metersSpeed;
    private BitmapFont metersLabel;
    private static GlyphLayout metersLayout;
    private float metersScore;
    private Integer record;
    private BitmapFont recordLabel;
    private static GlyphLayout recordLayout;

    private BitmapFont tap, userNameFont;

    //Metros enquanto tubarão
    private float countSharkMeters;

    //Formas para sobrepor botoes
    private Sprite peixe, menuSprite, playSprite, loginSprite, rankingSprite, termsSprite, privacyPolicySprite, replaySprite, nextSprite, backSprite, simSprite, naoSprite, pauseSprite, musicSprite;
    private Sprite[] algas;

    //imagens
    private Texture gameOverText, reload, startGame, menuBotao, simBotao, naoBotao, continueText, videoIcon, pause, music;
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

    //Formas para colisões
    private Circle peixeCircle, bolhaCircle;
    private Rectangle minhocaBonusRect;
    private Rectangle[] minhocasRect, piranhasVerticalRect, piranhasHorizontalRect, tubaroesRect;
    private Circle[] anzoisCircle, poluicoesCircle;

    //Numero do peixe
    private int fishNumber;

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

    public GameScreen(SeafishGame seafishGame) {
        super(seafishGame);
        this.seafishGame = seafishGame;
    }

    @Override
    public void show() {

        System.gc();

        if (viewport == null) {
            OrthographicCamera camera = new OrthographicCamera();
            viewport = new StretchViewport(width, height, camera);
        }

        fundo = new Texture[9];
        enfeite = new Texture[7];

        //Inicializa os Sprites
        peixes = new Sprite[7][5];
        tubaroes = new Sprite[2];
        cardumes = new Sprite[3];
        poluicoes = new Sprite[5];
        anzois = new Sprite[4];
        minhocasScore = new Sprite[10];
        minhocaBonus = new Sprite();
        bolha = new Sprite(new Texture("images/elements/bolha.png"));

        //Distancia da minhoca do obstáculo
        distanciaMinhoca = new int[4];
        alturaMinhoca = new int[4];

        //Sair do anzol
        toques = 0;
        toquesParaSoltar = 0;

        contaEnfeite = 0;

        //Velocidade de queda do peixe começa em 0
        velocidadeQueda = 0;

        //Controladores do jogo
        estado = 0; //0=menu - 1=iniciado
        colide = true;
        gameOver = false;
        iniciado = false;
        colidiuObstaculo = false;
        isColiding = false;
        pausa = false;
        isSlowShark = false;
        isShark = false;
        voltando = false;
        mostraTelaSegueJogo = false;
        isRewarded = false;
        isRewardedYet = false;
        setor = false;
        mostraMinhocaBonus = false;
        mostraBolha = false;
        vidaExtra = false;

        bolhaTocouTopo = new boolean[10];
        bolhaTocouLado = new boolean[10];
        anzolTocouTopo = new boolean[4];

        Arrays.fill(anzolTocouTopo, Boolean.FALSE);

        for (int i = 0; i < 10; i++) {
            bolhaTocouTopo[i] = Boolean.FALSE;
            bolhaTocouLado[i] = Boolean.FALSE;
        }

        numMinhocas = 1;
        contaFundo = 0;
        contaMetrosSetor = 0;
        obstaculoAtualSetor = 3;

        //Inicializa formas
        minhocaColidiu = new boolean[4];
        colidiuMinhoca = new boolean[4];
        minhocasRect = new Rectangle[4];
        piranhasVerticalRect = new Rectangle[4];
        piranhasHorizontalRect = new Rectangle[4];
        tubaroesRect = new Rectangle[4];
        anzoisCircle = new Circle[4];
        poluicoesCircle = new Circle[5];

        //Numero do peixe
        fishNumber = 0;

        //Rotação algas
        controlRotation = false;

        //Variações dos elementos
        variacaoPeixe = 0;
        variacaoPeixeAux = 0;
        variacaoAlgaAux = 0;
        variacaoTubarao = 0;
        variacaoAlga = 0;
        variacaoCardume = 0;

        countSharkMeters = 0;
        alturaObstaculo = new float[4];

        prefs = Gdx.app.getPreferences("score");
        record = prefs.getInteger("score");

        batch = new SpriteBatch();

        setTextures();

        peixeCircle = new Circle();
        minhocaBonusRect = new Rectangle();
        bolhaCircle = new Circle();

        metragem = 0;
        alturaObstaculoRandom = new Random();
        obstaculoRandom = new Random();

        obstaculos = new Sprite[11];

        velocidade = 10 * adjustWidth;
        for (int i = 0; i < alturaObstaculo.length; i++) {
            if (i == 0) {
                alturaObstaculo[i] = i;
            } else {
                alturaObstaculo[i] = height / i;
            }
        }

        posicaoInicialVertical = height / 2;
        movimentoAnzolVertical = new float[4];

        obstaculos[0] = cardumes[0];
        obstaculos[1] = tubaroes[0];
        obstaculos[2] = poluicoes[0];
        obstaculos[3] = poluicoes[1];
        obstaculos[4] = poluicoes[2];
        obstaculos[5] = poluicoes[3];
        obstaculos[6] = poluicoes[4];
        obstaculos[7] = anzois[0];
        obstaculos[8] = anzois[1];
        obstaculos[9] = anzois[2];
        obstaculos[10] = anzois[3];

        posicaoMovimentoObstaculoHorizontal = new float[4];
        movimentoEnfeiteHorizontal = (width - differenceBetweenWidth) - enfeite[contaEnfeite].getWidth();

        numObstaculo = new int[4];
        if (!pausa) {
            for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                posicaoMovimentoObstaculoHorizontal[i] = -obstaculos[numObstaculo[i]].getWidth() * adjustWidth;
            }
        }

        metersLabel = new BitmapFont();
        metersLabel.setColor(Color.YELLOW);
        metersLayout = new GlyphLayout();
        metersLayout.setText(metersLabel, (int) metersScore + "m");

        recordLabel = new BitmapFont();
        recordLabel.setColor(Color.YELLOW);
        metersLabel.getData().setScale(adjustWidth != 0 ? 6 * adjustWidth : 6);
        if (recordLayout == null) {
            recordLayout = new GlyphLayout();
            recordLayout.setText(recordLabel, "Record: " + record + "m");
        }

        tap = new BitmapFont();
        userNameFont = new BitmapFont();
        userNameFont.setColor(Color.YELLOW);
        userNameFont.getData().setScale(adjustWidth != 0 ? 3 * adjustWidth : 3);
        tap.setColor(Color.YELLOW);
        tap.getData().setScale(adjustWidth != 0 ? 5 * adjustWidth : 5);
        metersScore = 0;
        metersSpeed = 100;

        distanciaMinhocaRandom = new Random();
        alturaMinhocaRandom = new Random();
        sorteioMetrosPorSetor = new Random();
        numeroPoluicao = new Random();

        for (int i = 0; i < piranhasHorizontalRect.length; i++) {
            piranhasVerticalRect[i] = new Rectangle();
            piranhasHorizontalRect[i] = new Rectangle();
            tubaroesRect[i] = new Rectangle();
            minhocasRect[i] = new Rectangle();
            anzoisCircle[i] = new Circle();
        }

        for (int i = 0; i < poluicoesCircle.length; i++) {
            poluicoesCircle[i] = new Circle();
        }

        for (int i = 0; i < minhocaColidiu.length; i++) {
            minhocaColidiu[i] = false;
            colidiuMinhoca[i] = false;
        }

        setSounds();

        AUMENTA_VELOCIDADE = (float) 0.001 * adjustWidth;
        ALTURA_SALTO = -12 * (adjustWidth);
        POSICAO_HORIZONTAL_PEIXE = width / 12;
        ALTURA_TOPO = height - peixe.getHeight() * adjustHeight;
        VELOCIDADE_QUEDA = (float) (0.7 * (adjustWidth));
        VELOCIDADE_OBSTACULO = 12 * adjustWidth;
        TAP_X = ((width - differenceBetweenWidth) / 8) + peixe.getWidth();
        TAP_Y = height - 120;
        VELOCIDADE_INICIAL = velocidade;
        VELOCIDADE_METROS_INICIAL = (int) (100 * adjustWidth);
        TOQUES_ANZOL1 = 10;
        TOQUES_ANZOL2 = 15;
        TOQUES_ANZOL3 = 20;
        TOQUES_ANZOL4 = 30;
        ALTURA_SELECT_PEIXE = ((height) - (startGame.getHeight() * adjustHeight * 5));

        setButtons();
        setSeaweed();

        caughtWarms = 0;
        caughtSpecialWarms = 0;
        caughtBubbles = 0;
        caughtByHook = 0;
        turnedShark = 0;

        System.gc();
    }

    private void setButtons() {
        pauseSprite = new Sprite(pause);
        pauseSprite.setSize(pause.getWidth() * adjustWidth, pause.getHeight() * adjustHeight);
        pauseSprite.setPosition(pause.getWidth(), (height - ((float) pause.getHeight() * 1.5f)));

        musicSprite = new Sprite(music);
        musicSprite.setSize(music.getWidth() * adjustWidth, music.getHeight() * adjustHeight);
        musicSprite.setPosition(music.getWidth() * 3, (height - ((float) pause.getHeight() * 1.5f)));
    }

    private void setSounds() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audios/somfundo.mp3"));
        eatSound = Gdx.audio.newSound(Gdx.files.internal("audios/nhac.mp3"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("audios/colisao.mpeg"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audios/gameover.aac"));
        bubbleSound = Gdx.audio.newSound(Gdx.files.internal("audios/bubble.mp3"));
        blowBubbleSound = Gdx.audio.newSound(Gdx.files.internal("audios/boombubble.mp3"));
        bubbleSound.setVolume(bubbleSound.play(), (float) 0.2);
        backgroundMusic.setVolume((float) 0.1);
        backgroundMusic.setLooping(true);
        eatSound.setVolume(eatSound.play(), (float) 1);
        hitSound.setVolume(hitSound.play(), (float) 0.5);
        blowBubbleSound.setVolume(blowBubbleSound.play(), (float) 1);
        backgroundMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void render(float delta) {
        //Limpa tela
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Varia o peixe
        variacaoPeixeAux += (float) 0.05;
        variacaoAlgaAux += (float) 0.009;
        varyFish();

        gameState();
    }

    private void gameState() {
        seafishGame.handler.showBannerAd(false);
        //Impede que peixe vá pra baixo do chão (bug)
        if (posicaoInicialVertical < 10) {
            posicaoInicialVertical = height / 2;
        }

        if (!gameOver) {
            variacaoTubarao += 0.01;
            variacaoCardume += 0.05;

            if (metersScore == contaMetrosSetor + 500 && !setor) {
                setor = true;
                metrosPorSetor = sorteioMetrosPorSetor.nextInt(250) + 150;
                contaMetrosSetor = (int) metersScore;
            }
            if (setor) {
                if (metersScore >= contaMetrosSetor + metrosPorSetor) {
                    setor = false;
                    if (obstaculoAtualSetor <= 2) {
                        obstaculoAtualSetor++;
                    } else {
                        obstaculoAtualSetor = 0;
                    }
                }
            }

            if (movimentoEnfeiteHorizontal < -enfeite[contaEnfeite].getWidth()) {
                movimentoEnfeiteHorizontal = width + enfeite[contaEnfeite].getWidth();
            }

            VaryShoal();
            varyShark();

            //controla a velocidade do jogo
            if (iniciado) {
                if (velocidade < 35) {
                    velocidade += AUMENTA_VELOCIDADE;
                }
            }

            controlSeaweedRotation(true);

            sortNextTackle();

            //faz o peixe subir a cada toque na tela
            if (Gdx.input.justTouched() && !pausa && !musicSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                bubbleSound.play(0.1f);
                iniciado = true;
                velocidadeQueda = ALTURA_SALTO;
                batch.begin();
                batch.draw(peixes[fishNumber][variacaoPeixe], POSICAO_HORIZONTAL_PEIXE, posicaoInicialVertical, peixes[fishNumber][variacaoPeixe].getWidth() * adjustWidth, peixes[fishNumber][variacaoPeixe].getHeight() * adjustHeight);
                batch.end();
            }

            if (Gdx.input.justTouched()) {
                if (pauseSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    iniciado = false;
                }
                if (musicSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    if (backgroundMusic.isPlaying()) {
                        backgroundMusic.pause();
                        music = new Texture("images/buttons/musicoff.png");
                    } else {
                        backgroundMusic.play();
                        music = new Texture("images/buttons/music.png");
                    }
                }
            }

            peixeCircle.set(POSICAO_HORIZONTAL_PEIXE + peixes[fishNumber][0].getWidth() * adjustWidth / 2, (posicaoInicialVertical + peixes[fishNumber][0].getHeight() * adjustHeight / 2), ((peixes[fishNumber][0].getHeight() * adjustWidth / 2) - peixes[fishNumber][0].getHeight() * adjustHeight / 12));

            //impede que o peixe passe do topo
            if (posicaoInicialVertical >= ALTURA_TOPO) {
                velocidadeQueda = (float) 0.1;
            }

            //aumenta a velocidade de queda do peixe
            if (iniciado && !pausa) {
                velocidadeQueda += VELOCIDADE_QUEDA;
                //Movimenta o obstáculo
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    posicaoMovimentoObstaculoHorizontal[i] -= velocidade;
                }
                movimentoEnfeiteHorizontal -= velocidade;
                minhocaBonusHorizontal -= velocidade;
                bolhaHorizontal -= velocidade;
                metragem++;
                if (metragem >= metersSpeed) {
                    metersScore++;
                    if (metersSpeed > 10) {
                        metersSpeed -= 0.5;
                    }
                    metragem = 0;
                }

                //Faz o peixe cair (impede que passe do chão)
                if (posicaoInicialVertical > height / 13 || velocidadeQueda < 0) {
                    posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
                }

                if (colide) {
                    testCollision();
                } else {
                    increaseGameSpeed();
                }
            }
            if (colide) {
                testHook();
            }
        }

        batch.begin();
        batch.draw(fundo[contaFundo], 0, 0, width, height);
        batch.draw(enfeite[contaEnfeite], movimentoEnfeiteHorizontal, 7, enfeite[contaEnfeite].getWidth() * adjustWidth, enfeite[contaEnfeite].getHeight() * adjustHeight);
        algas[variacaoAlga].draw(batch);
        algas[2].draw(batch);
        batch.draw(peixes[fishNumber][variacaoPeixe], POSICAO_HORIZONTAL_PEIXE, posicaoInicialVertical, peixes[fishNumber][variacaoPeixe].getWidth() * adjustWidth, peixes[fishNumber][variacaoPeixe].getHeight() * adjustHeight);
        batch.draw(pause, (pause.getWidth() * adjustWidth), (height - ((float) pause.getHeight() * adjustHeight * 1.5f)), pause.getWidth() * adjustWidth, pause.getHeight() * adjustHeight);
        batch.draw(music, (pause.getWidth() * adjustWidth * 3f), (height - ((float) pause.getHeight() * adjustHeight * 1.5f)), music.getWidth() * adjustWidth, music.getHeight() * adjustHeight);

        if (!voltando) {
            upAndDownHook();
        }
        for (int i = 0; i < numObstaculo.length; i++) {
            if (numObstaculo[i] > 6) {
                batch.draw(obstaculos[numObstaculo[i]], posicaoMovimentoObstaculoHorizontal[i], movimentoAnzolVertical[i], obstaculos[numObstaculo[i]].getWidth() * adjustWidth, obstaculos[numObstaculo[i]].getHeight() * adjustHeight);
            } else {
                batch.draw(obstaculos[numObstaculo[i]], posicaoMovimentoObstaculoHorizontal[i], alturaObstaculo[i], obstaculos[numObstaculo[i]].getWidth() * adjustWidth, obstaculos[numObstaculo[i]].getHeight() * adjustHeight);
            }
        }

        //Set retangulos colisoes minhocas
        for (int i = 0; i < minhocasRect.length; i++) {
            if (!isSlowShark && alturaMinhoca[i] > height / 13) {
                minhocasRect[i].set(posicaoMovimentoObstaculoHorizontal[i] - distanciaMinhoca[i], alturaMinhoca[i], minhocasScore[i].getWidth() * adjustWidth, minhocasScore[i].getHeight() * adjustHeight);

                if (!minhocaColidiu[i] && !pausa) {
                    batch.draw(minhocasScore[i], posicaoMovimentoObstaculoHorizontal[i] - distanciaMinhoca[i], alturaMinhoca[i], minhocasScore[i].getWidth() * adjustWidth, minhocasScore[i].getHeight() * adjustHeight);
                }
            }
        }

        if (metersScore >= 2500 && metersScore % 2500 == 0 && !vidaExtra) {
            mostraBolha = true;
            bolhaHorizontal = width;
        }
        if (bolhaHorizontal < 0) {
            mostraBolha = false;
        }
        if (mostraBolha) {
            bolhaCircle.set(bolhaHorizontal, height / 2, (float) (bolha.getTexture().getWidth() / 2) * adjustWidth);
            batch.draw(bolha, bolhaHorizontal, height / 2, bolha.getWidth() * adjustWidth, bolha.getHeight() * adjustHeight);
        }
        if (Intersector.overlaps(peixeCircle, bolhaCircle) && mostraBolha) {
            blowBubbleSound.play();
            mostraBolha = false;
            vidaExtra = true;
            caughtBubbles++;
        }
        if (metersScore % 650 == 0 && metersScore != 0) {
            mostraMinhocaBonus = true;
            minhocaBonusHorizontal = width;
        }
        if (minhocaBonusHorizontal < minhocaBonus.getWidth() * adjustWidth) {
            mostraMinhocaBonus = false;
        }
        if (mostraMinhocaBonus && !isShark && !isSlowShark) {
            minhocaBonusRect.set(minhocaBonusHorizontal, height / 2, minhocaBonus.getWidth() * adjustWidth, minhocaBonus.getHeight() * adjustHeight);
            batch.draw(minhocaBonus, minhocaBonusHorizontal, height / 2, minhocaBonus.getWidth() * adjustWidth, minhocaBonus.getHeight() * adjustHeight);
        }

        if (Intersector.overlaps(peixeCircle, minhocaBonusRect) && mostraMinhocaBonus) {
            mostraMinhocaBonus = false;
            colide = false;
            turnedShark++;
            caughtSpecialWarms++;
        }

        if (Intersector.overlaps(peixeCircle, minhocasRect[0])) {
            countWarm(0);
            minhocaColidiu[0] = true;
        } else if (Intersector.overlaps(peixeCircle, minhocasRect[1])) {
            countWarm(1);
            minhocaColidiu[1] = true;
        } else if (Intersector.overlaps(peixeCircle, minhocasRect[2])) {
            countWarm(2);
            minhocaColidiu[2] = true;
        } else if (Intersector.overlaps(peixeCircle, minhocasRect[3])) {
            countWarm(3);
            minhocaColidiu[3] = true;
        } else {
            colidiuMinhoca[0] = false;
            colidiuMinhoca[1] = false;
            colidiuMinhoca[2] = false;
            colidiuMinhoca[3] = false;
        }

        for (int i = 1; i <= numMinhocas; i++) {
            batch.draw(minhocasScore[i - 1], (float) ((width - differenceBetweenWidth) - (minhocasScore[i - 1].getWidth() * adjustWidth * i * 1.15)), (height - recordLayout.height - (minhocasScore[i - 1].getHeight() * adjustHeight * 1.5f)), minhocasScore[i - 1].getWidth() * adjustWidth, minhocasScore[i - 1].getHeight() * adjustHeight);
        }
        if (vidaExtra) {
            batch.draw(bolha, ((width - differenceBetweenWidth) / 2), (float) (height - minhocaBonus.getHeight() * adjustHeight * 1.3), bolha.getTexture().getWidth() * adjustWidth, bolha.getTexture().getHeight() * adjustHeight);
        }

        metersLabel.draw(batch, (int) metersScore + "m", (width - differenceBetweenWidth) / 2 - (metersLayout.width / 2), height - (height / 10));
        recordLabel.draw(batch, recordLayout, (width - differenceBetweenWidth - (recordLayout.width) - 15), height - 15);
        if (gameOver) {
            batch.draw(gameOverText, ((width - differenceBetweenWidth) / 2) - (gameOverText.getWidth() * adjustWidth / 2), (float) (height - (gameOverText.getHeight() * adjustHeight * 2.5)), gameOverText.getWidth() * adjustWidth, gameOverText.getHeight() * adjustHeight);
            batch.draw(reload, ((width - differenceBetweenWidth) / 2) - (reload.getWidth() * adjustWidth), height / 2, 150 * adjustWidth, 150 * adjustHeight);
            batch.draw(menuBotao, ((width - differenceBetweenWidth) / 2) - (menuBotao.getWidth() * adjustWidth / 2), (float) ((height / 3) - menuSprite.getHeight() * adjustHeight * 1.5), menuBotao.getWidth() * adjustWidth, menuBotao.getHeight() * adjustHeight);
        }

        if (mostraTelaSegueJogo) {
            batch.draw(continueText, ((width - differenceBetweenWidth) / 2) - (continueText.getWidth() * adjustWidth / 2), (float) (height - (continueText.getHeight() * adjustHeight * 2.5)), continueText.getWidth() * adjustWidth, continueText.getHeight() * adjustHeight);
            batch.draw(simBotao, (float) (((width - differenceBetweenWidth) / 2) - (simBotao.getWidth() * adjustWidth * 1.5)), (float) ((height / 3) - simSprite.getHeight() * adjustHeight * 1.5), simBotao.getWidth() * adjustWidth, simBotao.getHeight() * adjustHeight);
            batch.draw(naoBotao, (((width - differenceBetweenWidth) / 2) + (naoBotao.getWidth() * adjustWidth)), (float) ((height / 3) - naoSprite.getHeight() * adjustHeight * 1.5), naoBotao.getWidth() * adjustWidth, naoBotao.getHeight() * adjustHeight);
            batch.draw(videoIcon, (float) (((width - differenceBetweenWidth) / 2) - (simBotao.getWidth() * adjustWidth * 1.5) + videoIcon.getWidth() * adjustWidth * 5), (float) ((height / 3) - simSprite.getHeight() * adjustHeight * 1.5), simBotao.getWidth() * adjustWidth, simBotao.getHeight() * adjustHeight);
        }

        if (voltando) {
            if (variacaoPeixe == 0) {
                tap.draw(batch, "tap!", TAP_X, TAP_Y);
            }
        }

        batch.end();

        setTackles();

        if (Gdx.input.justTouched()) {
            if (mostraTelaSegueJogo) {
                if (simSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    this.googleServices.showRewardedVideoAd();
                    mostraTelaSegueJogo = false;
                }
                if (naoSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    mostraTelaSegueJogo = false;
                    seafishGame.setScreen(new GameOverScreen(seafishGame));
                }
            }
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

    private void upAndDownHook() {
        for (int i = 0; i < anzolTocouTopo.length; i++) {
            if (movimentoAnzolVertical[i] >= height - 10) {
                anzolTocouTopo[i] = true;
            }

            if (movimentoAnzolVertical[i] <= 0) {
                anzolTocouTopo[i] = false;
            }

            if (iniciado) {
                if (anzolTocouTopo[i]) {
                    movimentoAnzolVertical[i] -= (velocidade / 2);
                } else {
                    movimentoAnzolVertical[i] += (velocidade / 2);
                }
            }
        }
    }

    private void varyShark() {
        if (variacaoTubarao > 1) {
            variacaoTubarao = 0;
        } else if (variacaoTubarao >= 0.5) {
            obstaculos[1] = tubaroes[0];
        } else {
            obstaculos[1] = tubaroes[1];
        }
    }

    private void varyFish() {
        if (!isShark) {
            if (variacaoPeixeAux > 2) {
                variacaoPeixeAux = 0;
            } else if (variacaoPeixeAux >= 1) {
                variacaoPeixe = 0;
            } else {
                variacaoPeixe = 1;
            }
        } else if (isSlowShark) {
            if (variacaoPeixeAux > 2) {
                variacaoPeixeAux = 0;
            } else if (variacaoPeixeAux >= 1) {
                variacaoPeixe = 1;
            } else {
                variacaoPeixe = 3;
            }
        } else {
            if (variacaoPeixeAux > 2) {
                variacaoPeixeAux = 0;
            } else if (variacaoPeixeAux >= 1) {
                variacaoPeixe = 3;
            } else {
                variacaoPeixe = 4;
            }
        }
    }

    private void VaryShoal() {
        if (variacaoCardume > 1) {
            variacaoCardume = 0;
        } else if (variacaoCardume >= 0.5) {
            obstaculos[0] = cardumes[0];
        } else {
            obstaculos[0] = cardumes[1];
        }
    }

    private void testHook() {
        if (Intersector.overlaps(peixeCircle, anzoisCircle[0])) {
            backToStart(1);
        } else if (Intersector.overlaps(peixeCircle, anzoisCircle[1])) {
            backToStart(2);
        } else if (Intersector.overlaps(peixeCircle, anzoisCircle[2])) {
            backToStart(3);
        } else if (Intersector.overlaps(peixeCircle, anzoisCircle[3])) {
            backToStart(4);
        } else {
            voltando = false;
            if (!mostraTelaSegueJogo) {
                pausa = false;
            }
        }
    }

    private void backToStart(float circle) {
        if (!voltando) {
            caughtByHook++;
        }
        voltando = true;
        setor = false;
        contaMetrosSetor = 0;
        if (Gdx.input.justTouched()) {
            iniciado = true;
        }
        if (iniciado) {
            numMinhocas = 0;
            velocidadeQueda = 0;
            pausa = true;
            if (circle == 1) {
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    if (i == 0) {
                        posicaoMovimentoObstaculoHorizontal[i] += 0;
                    } else {
                        posicaoMovimentoObstaculoHorizontal[i] += velocidade * VELOCIDADE_OBSTACULO;
                    }
                }
            } else if (circle == 2) {
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    if (i == 1) {
                        posicaoMovimentoObstaculoHorizontal[i] += 0;
                    } else {
                        posicaoMovimentoObstaculoHorizontal[i] += velocidade * VELOCIDADE_OBSTACULO;
                    }
                }
            } else if (circle == 3) {
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    if (i == 2) {
                        posicaoMovimentoObstaculoHorizontal[i] += 0;
                    } else {
                        posicaoMovimentoObstaculoHorizontal[i] += velocidade * VELOCIDADE_OBSTACULO;
                    }
                }
            } else if (circle == 4) {
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    if (i == 3) {
                        posicaoMovimentoObstaculoHorizontal[i] += 0;
                    } else {
                        posicaoMovimentoObstaculoHorizontal[i] += velocidade * VELOCIDADE_OBSTACULO;
                    }
                }
            }

            movimentoEnfeiteHorizontal += velocidade * VELOCIDADE_OBSTACULO;

            if (metersScore > 0) {
                sortPreviousTackle();
            } else {
                gameOver = false;
                colide = true;
                posicaoInicialVertical = posicaoInicialVertical - 30;
                velocidade = VELOCIDADE_INICIAL;
                metersSpeed = VELOCIDADE_METROS_INICIAL;
            }

            if (Gdx.input.justTouched()) {
                switch ((int) circle - 1) {
                    case 0:
                        toquesParaSoltar = TOQUES_ANZOL1;
                        break;
                    case 1:
                        toquesParaSoltar = TOQUES_ANZOL2;
                        break;
                    case 2:
                        toquesParaSoltar = TOQUES_ANZOL3;
                        break;
                    case 3:
                        toquesParaSoltar = TOQUES_ANZOL4;
                        break;
                    default:
                        toquesParaSoltar = TOQUES_ANZOL1;
                }
                toques++;
                if (toques >= toquesParaSoltar) {
                    posicaoInicialVertical -= 30;
                    posicaoMovimentoObstaculoHorizontal[0] = width;
                    posicaoMovimentoObstaculoHorizontal[1] = (float) ((width - differenceBetweenWidth) * 1.5);
                    posicaoMovimentoObstaculoHorizontal[2] = width * 2;
                    posicaoMovimentoObstaculoHorizontal[3] = (float) ((width - differenceBetweenWidth) * 2.5);
                    gameOver = false;
                    colide = true;
                    posicaoInicialVertical = height / 2;
                    toques = 0;

                    if (metersScore >= 4000) {
                        velocidade = 33 * adjustWidth;
                        metersSpeed = (int) (VELOCIDADE_INICIAL);
                    } else if (metersScore >= 3000) {
                        velocidade = 30 * adjustWidth;
                        metersSpeed = (int) (25 * adjustWidth);
                    } else if (metersScore >= 25000) {
                        velocidade = 27 * adjustWidth;
                        metersSpeed = (int) (36 * adjustWidth);
                    } else if (metersScore >= 2000) {
                        velocidade = 24 * adjustWidth;
                        metersSpeed = (int) (47 * adjustWidth);
                    } else if (metersScore >= 1500) {
                        velocidade = 21 * adjustWidth;
                        metersSpeed = (int) (58 * adjustWidth);
                    } else if (metersScore >= 1000) {
                        velocidade = 18 * adjustWidth;
                        metersSpeed = (int) (71 * adjustWidth);
                    } else if (metersScore >= 500) {
                        velocidade = 15 * adjustWidth;
                        metersSpeed = (int) (82 * adjustWidth);
                    } else if (metersScore >= 250) {
                        velocidade = 12 * adjustWidth;
                        metersSpeed = (int) (90 * adjustWidth);
                    } else if (metersScore <= 5) {
                        velocidade = VELOCIDADE_INICIAL;
                        metersSpeed = (int) (100 * adjustWidth);
                    }
                }
            }
        }
    }

    private void countWarm(int warmNumber) {
        if (!colidiuMinhoca[warmNumber]) {
            if (numMinhocas <= 9) {
                if (!isSlowShark) {
                    numMinhocas++;
                    caughtWarms++;
                    eatSound.play(0.9f);
                    colidiuMinhoca[warmNumber] = true;
                }
            } else if (!isShark) {
                numMinhocas = 1;
                colide = false;
                changeBackground();
                turnedShark++;
            }
        }
    }

    private void changeBackground() {
        if (contaFundo <= 5) {
            contaFundo++;
            contaEnfeite++;
        } else {
            contaFundo = 0;
            contaEnfeite = 0;
        }
    }

    private void increaseGameSpeed() {
        countSharkMeters += 0.5;
        if (countSharkMeters <= 100) {
            this.metersScore += 0.5;
            for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                posicaoMovimentoObstaculoHorizontal[i] -= velocidade * (VELOCIDADE_INICIAL);
            }
            movimentoEnfeiteHorizontal -= velocidade * VELOCIDADE_INICIAL;
            isShark = true;
        } else if (countSharkMeters <= 190) {
            if (countSharkMeters % 5 == 0) {
                changeBackground();
            }
            isSlowShark = true;
            isShark = true;
        } else {
            colide = true;
            countSharkMeters = 0;
            isSlowShark = false;
            isShark = false;
        }
    }

    private void testCollision() {
        if (Intersector.overlaps(peixeCircle, piranhasHorizontalRect[0]) || Intersector.overlaps(peixeCircle, piranhasHorizontalRect[1]) ||
                Intersector.overlaps(peixeCircle, piranhasHorizontalRect[2]) || Intersector.overlaps(peixeCircle, piranhasHorizontalRect[3]) ||
                Intersector.overlaps(peixeCircle, piranhasVerticalRect[0]) || Intersector.overlaps(peixeCircle, piranhasVerticalRect[1]) ||
                Intersector.overlaps(peixeCircle, piranhasVerticalRect[2]) || Intersector.overlaps(peixeCircle, piranhasVerticalRect[3])) {

            deathTackle = "images/obstacles/piranhas.png";
            actionOnCollision(2);

        } else if (Intersector.overlaps(peixeCircle, tubaroesRect[0]) || Intersector.overlaps(peixeCircle, tubaroesRect[1]) ||
                Intersector.overlaps(peixeCircle, tubaroesRect[2]) || Intersector.overlaps(peixeCircle, tubaroesRect[3])) {

            deathTackle = "images/obstacles/tubaraoinimigo2.png";
            actionOnCollision(2);

        } else if (Intersector.overlaps(peixeCircle, poluicoesCircle[0])) {
            deathTackle = obstaculos[numObstaculo[0]].getTexture().toString();
            actionOnCollision(4);
        } else if (Intersector.overlaps(peixeCircle, poluicoesCircle[1])) {
            deathTackle = obstaculos[numObstaculo[1]].getTexture().toString();
            actionOnCollision(4);
        } else if (Intersector.overlaps(peixeCircle, poluicoesCircle[2])) {
            deathTackle = obstaculos[numObstaculo[2]].getTexture().toString();
            actionOnCollision(4);
        } else if (Intersector.overlaps(peixeCircle, poluicoesCircle[3])) {
            deathTackle = obstaculos[numObstaculo[3]].getTexture().toString();
            actionOnCollision(4);
        } else if (Intersector.overlaps(peixeCircle, poluicoesCircle[4])) {
            deathTackle = obstaculos[numObstaculo[4]].getTexture().toString();
            actionOnCollision(4);
        } else {
            colidiuObstaculo = false;
            isColiding = false;
        }
        if (isColiding) {
            variacaoPeixe = 2;
        }
    }

    private void actionOnCollision(int decreaseLives) {
        isColiding = true;
        if (!colidiuObstaculo) {
            Gdx.input.vibrate(50);
            numMinhocas -= decreaseLives;
            hitSound.play();
            colidiuObstaculo = true;
            if (numMinhocas < 0) {
                if (vidaExtra) {
                    vidaExtra = false;
                    mostraBolha = true;
                    numMinhocas = 0;
                } else {
                    if (!isRewardedYet && showRewardVideo()) {
                        if (this.googleServices.hasVideoLoaded()) {
                            iniciado = false;
                            pausa = true;
                            mostraTelaSegueJogo = true;
                        } else {
                            gameOver();
                        }
                    } else {
                        gameOver();
                        if (contaPartidas == 2 || metersScore >= 300) {
                            handler.showInterstitialAd(new Runnable() {
                                @Override
                                public void run() {
                                    contaPartidas = 0;
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void gameOver() {
        backgroundMusic.dispose();
        gameOver = true;
        gameOverSound.play();
        saveScores();
    }

    private void saveScores() {
        rankingInterface.saveRecord((int) metersScore, peixes[fishNumber][variacaoPeixe].getTexture().toString(), deathTackle, caughtWarms, caughtSpecialWarms, turnedShark, caughtBubbles, caughtByHook);
        if (metersScore > record) {
            prefs.putInteger("score", (int) metersScore);
            prefs.flush();
        }
    }

    private boolean showRewardVideo() {
        return record >= 70 && metersScore >= record - (record / 3F) && metersScore <= record;
    }

    private void setTackles() {
        if (numObstaculo[0] == 0) {
            piranhasVerticalRect[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * adjustWidth / 3, alturaObstaculo[0], (obstaculos[numObstaculo[0]].getWidth() * adjustWidth / 3), obstaculos[numObstaculo[0]].getHeight() * adjustHeight);
            piranhasHorizontalRect[0].set(posicaoMovimentoObstaculoHorizontal[0], alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight() * adjustHeight / 3, obstaculos[numObstaculo[0]].getWidth() * adjustWidth, (obstaculos[numObstaculo[0]].getHeight() * adjustHeight / 3));
        } else if (numObstaculo[0] == 1) {
            tubaroesRect[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * adjustWidth / 9, (float) (alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight() * adjustHeight / 4.5), (float) (obstaculos[numObstaculo[0]].getWidth() * adjustWidth / 1.5), (float) (obstaculos[numObstaculo[0]].getHeight() * adjustHeight / 2.5));
        } else if (numObstaculo[0] == 2 || numObstaculo[0] == 3 || numObstaculo[0] == 4 || numObstaculo[0] == 5 || numObstaculo[0] == 6) {
            poluicoesCircle[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * adjustWidth / 2, alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight() * adjustHeight / 2, obstaculos[numObstaculo[0]].getWidth() * adjustWidth / 2);
        } else if (numObstaculo[0] == 7 || numObstaculo[0] == 8 || numObstaculo[0] == 9 || numObstaculo[0] == 10) {
            anzoisCircle[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * adjustWidth / 2, movimentoAnzolVertical[0] + obstaculos[numObstaculo[0]].getWidth() * adjustWidth / 2, (obstaculos[numObstaculo[0]].getWidth() * adjustWidth / 2));
        }

        if (numObstaculo[1] == 0) {
            piranhasVerticalRect[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * adjustWidth / 3, alturaObstaculo[1], (obstaculos[numObstaculo[1]].getWidth() * adjustWidth / 3), obstaculos[numObstaculo[1]].getHeight() * adjustHeight);
            piranhasHorizontalRect[1].set(posicaoMovimentoObstaculoHorizontal[1], alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * adjustHeight / 3, obstaculos[numObstaculo[1]].getWidth() * adjustWidth, (obstaculos[numObstaculo[1]].getHeight() * adjustHeight / 3));
        } else if (numObstaculo[1] == 1) {
            tubaroesRect[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * adjustWidth / 9, (float) (alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * adjustHeight / 4.5), (float) (obstaculos[numObstaculo[1]].getWidth() * adjustWidth / 1.5), (float) (obstaculos[numObstaculo[1]].getHeight() * adjustHeight / 2.5));
        } else if (numObstaculo[1] == 2 || numObstaculo[1] == 3 || numObstaculo[1] == 4 || numObstaculo[1] == 5 || numObstaculo[1] == 6) {
            poluicoesCircle[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * adjustWidth / 2, alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * adjustHeight / 2, obstaculos[numObstaculo[1]].getWidth() * adjustWidth / 2);
        } else if (numObstaculo[1] == 7 || numObstaculo[1] == 8 || numObstaculo[1] == 9 || numObstaculo[1] == 10) {
            anzoisCircle[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * adjustWidth / 2, movimentoAnzolVertical[1] + obstaculos[numObstaculo[1]].getWidth() * adjustWidth / 2, (obstaculos[numObstaculo[1]].getWidth() * adjustWidth / 2));
        }

        if (numObstaculo[2] == 0) {
            piranhasVerticalRect[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * adjustWidth / 3, alturaObstaculo[2], (obstaculos[numObstaculo[2]].getWidth() * adjustWidth / 3), obstaculos[numObstaculo[2]].getHeight() * adjustHeight);
            piranhasHorizontalRect[2].set(posicaoMovimentoObstaculoHorizontal[2], alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * adjustHeight / 3, obstaculos[numObstaculo[2]].getWidth() * adjustWidth, (obstaculos[numObstaculo[2]].getHeight() * adjustHeight / 3));
        } else if (numObstaculo[2] == 1) {
            tubaroesRect[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * adjustWidth / 9, (float) (alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * adjustHeight / 4.5), (float) (obstaculos[numObstaculo[2]].getWidth() * adjustWidth / 1.5), (float) (obstaculos[numObstaculo[2]].getHeight() * adjustHeight / 2.5));
        } else if (numObstaculo[2] == 2 || numObstaculo[2] == 3 || numObstaculo[2] == 4 || numObstaculo[2] == 5 || numObstaculo[2] == 6) {
            poluicoesCircle[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * adjustWidth / 2, alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * adjustHeight / 2, obstaculos[numObstaculo[2]].getWidth() * adjustWidth / 2);
        } else if (numObstaculo[2] == 7 || numObstaculo[2] == 8 || numObstaculo[2] == 9 || numObstaculo[2] == 10) {
            anzoisCircle[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * adjustWidth / 2, movimentoAnzolVertical[2] + obstaculos[numObstaculo[2]].getWidth() * adjustWidth / 2, (obstaculos[numObstaculo[2]].getWidth() * adjustWidth / 2));
        }

        if (numObstaculo[3] == 0) {
            piranhasVerticalRect[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * adjustWidth / 3, alturaObstaculo[3], (obstaculos[numObstaculo[3]].getWidth() * adjustWidth / 3), obstaculos[numObstaculo[3]].getHeight() * adjustHeight);
            piranhasHorizontalRect[3].set(posicaoMovimentoObstaculoHorizontal[3], alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * adjustHeight / 3, obstaculos[numObstaculo[3]].getWidth() * adjustWidth, (obstaculos[numObstaculo[3]].getHeight() * adjustHeight / 3));
        } else if (numObstaculo[3] == 1) {
            tubaroesRect[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * adjustWidth / 9, (float) (alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * adjustHeight / 4.5), (float) (obstaculos[numObstaculo[3]].getWidth() * adjustWidth / 1.5), (float) (obstaculos[numObstaculo[3]].getHeight() * adjustHeight / 2.5));
        } else if (numObstaculo[3] == 2 || numObstaculo[3] == 3 || numObstaculo[3] == 4 || numObstaculo[3] == 5 || numObstaculo[3] == 6) {
            poluicoesCircle[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * adjustWidth / 2, alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * adjustHeight / 2, obstaculos[numObstaculo[3]].getWidth() * adjustWidth / 2);
        } else if (numObstaculo[3] == 7 || numObstaculo[3] == 8 || numObstaculo[3] == 9 || numObstaculo[3] == 10) {
            anzoisCircle[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * adjustWidth / 2, movimentoAnzolVertical[3] + obstaculos[numObstaculo[3]].getWidth() * adjustWidth / 2, (obstaculos[numObstaculo[3]].getWidth() * adjustWidth / 2));
        }
    }

    private void sortNextTackle() {
        float multLargura = (float) 2.5;
        //Verifica se o obstáculo saiu da tela e manda o próximo obstáculo
        for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {

            if (posicaoMovimentoObstaculoHorizontal[i] < -obstaculos[numObstaculo[i]].getWidth() * adjustWidth) {
                //Sorteia o próximo obstáculo
                numObstaculo[i] = obstaculoRandom.nextInt(10);
                if (!setor) {
                    if (numObstaculo[i] >= 0 && numObstaculo[i] <= 3) {
                        numObstaculo[i] = 0;
                    } else if (numObstaculo[i] >= 4 && numObstaculo[i] <= 6) {
                        numObstaculo[i] = 1;
                    } else if (numObstaculo[i] >= 7 && numObstaculo[i] <= 8) {
                        numObstaculo[i] = numeroPoluicao.nextInt(5) + 2;
                    } else if (numObstaculo[i] == 9) {
                        numObstaculo[i] = i + 7;
                    }
                } else {
                    if (obstaculoAtualSetor == 3) {
                        numObstaculo[i] = i + 7;
                    } else if (obstaculoAtualSetor == 2) {
                        numObstaculo[i] = numeroPoluicao.nextInt(5) + 2;
                    } else {
                        numObstaculo[i] = obstaculoAtualSetor;
                    }
                }
                if (numObstaculo[i] == 7 || numObstaculo[i] == 8 || numObstaculo[i] == 9 || numObstaculo[i] == 10) {
                    alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (obstaculos[numObstaculo[i]].getHeight() * adjustHeight / 1.5));
                    movimentoAnzolVertical[i] = alturaObstaculo[i];
                } else {
                    alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (height - obstaculos[numObstaculo[i]].getHeight() * adjustHeight / 1.5));
                }

                posicaoMovimentoObstaculoHorizontal[i] = ((width - differenceBetweenWidth) * multLargura);
                if (!isSlowShark) {
                    distanciaMinhoca[i] = distanciaMinhocaRandom.nextInt((int) (obstaculos[numObstaculo[i]].getWidth() * adjustWidth * 2));
                    alturaMinhoca[i] = (int) alturaObstaculo[i] - ((alturaMinhocaRandom.nextInt((int) (obstaculos[numObstaculo[i]].getHeight() * adjustHeight * 2))));
                    minhocaColidiu[i] = false;
                }
            }
            multLargura -= 0.5;
        }
    }

    private void sortPreviousTackle() {
        float multLargura = (float) 2.5;
        //Verifica se o obstáculo saiu da tela e manda o próximo obstáculo
        for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {

            if (posicaoMovimentoObstaculoHorizontal[i] > width + obstaculos[numObstaculo[i]].getWidth() * adjustWidth) {
                metersScore -= 0.5;
                //Sorteia o próximo obstáculo
                numObstaculo[i] = obstaculoRandom.nextInt(10);
                if (numObstaculo[i] >= 0 && numObstaculo[i] <= 3) {
                    numObstaculo[i] = 0;
                } else if (numObstaculo[i] >= 4 && numObstaculo[i] <= 6) {
                    numObstaculo[i] = 1;
                } else if (numObstaculo[i] >= 7 && numObstaculo[i] <= 8) {
                    numObstaculo[i] = 2;
                } else if (numObstaculo[i] == 9) {
                    numObstaculo[i] = 3;
                }
                if (numObstaculo[i] == 3 || numObstaculo[i] == 4 || numObstaculo[i] == 5 || numObstaculo[i] == 6) {
                    alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (obstaculos[numObstaculo[i]].getHeight() * adjustHeight / 1.5));
                } else {
                    alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (height - obstaculos[numObstaculo[i]].getHeight() * adjustHeight / 1.5));
                }
                posicaoMovimentoObstaculoHorizontal[i] = (-obstaculos[i].getWidth() * adjustWidth * multLargura);
            }
            multLargura -= 0.5;

        }
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

        if (record < 2000) {
            peixes[3][0] = new Sprite(new Texture("images/characters/sombra1.png"));
            peixes[3][1] = new Sprite(new Texture("images/characters/sombra1.png"));
        } else {
            peixes[3][0] = new Sprite(new Texture("images/characters/peixe4.png"));
            peixes[3][1] = new Sprite(new Texture("images/characters/peixe42.png"));
            peixes[3][2] = new Sprite(new Texture("images/characters/peixe4red.png"));
            peixes[3][3] = new Sprite(new Texture("images/characters/tubarao4.png"));
            peixes[3][4] = new Sprite(new Texture("images/characters/tubarao42.png"));
        }

        if (record < 4000) {
            peixes[4][0] = new Sprite(new Texture("images/characters/sombra2.png"));
            peixes[4][1] = new Sprite(new Texture("images/characters/sombra2.png"));
        } else {
            peixes[4][0] = new Sprite(new Texture("images/characters/peixe5.png"));
            peixes[4][1] = new Sprite(new Texture("images/characters/peixe52.png"));
            peixes[4][2] = new Sprite(new Texture("images/characters/peixe5red.png"));
            peixes[4][3] = new Sprite(new Texture("images/characters/tubarao5.png"));
            peixes[4][4] = new Sprite(new Texture("images/characters/tubarao52.png"));
        }

        if (record < 6000) {
            peixes[5][0] = new Sprite(new Texture("images/characters/sombra3.png"));
            peixes[5][1] = new Sprite(new Texture("images/characters/sombra3.png"));
        } else {
            peixes[5][0] = new Sprite(new Texture("images/characters/peixe6.png"));
            peixes[5][1] = new Sprite(new Texture("images/characters/peixe62.png"));
            peixes[5][2] = new Sprite(new Texture("images/characters/peixe6red.png"));
            peixes[5][3] = new Sprite(new Texture("images/characters/tubarao6.png"));
            peixes[5][4] = new Sprite(new Texture("images/characters/tubarao62.png"));
        }

        if (record < 8000) {
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

    private void setTextures() {
        //Texturas
        peixe = new Sprite(new Texture("images/characters/peixe1.png"));
        fundo[0] = new Texture("images/scenarios/fundo1.png");
        fundo[1] = new Texture("images/scenarios/fundo2.png");
        fundo[2] = new Texture("images/scenarios/fundo3.png");
        fundo[3] = new Texture("images/scenarios/fundo4.png");
        fundo[4] = new Texture("images/scenarios/fundo5.png");
        fundo[5] = new Texture("images/scenarios/fundo6.png");
        fundo[6] = new Texture("images/scenarios/fundo7.png");
        gameOverText = new Texture("images/texts/gameover.png");
        continueText = new Texture("images/texts/continue.png");
        reload = new Texture("images/buttons/refresh.png");
        music = new Texture("images/buttons/music.png");
        pause = new Texture("images/buttons/pause.png");
        menuBotao = new Texture("images/buttons/menu.png");
        simBotao = new Texture("images/buttons/yes.png");
        videoIcon = new Texture("images/buttons/video.png");
        naoBotao = new Texture("images/buttons/no.png");
        startGame = new Texture("images/buttons/startgame.png");
        setFishes();
        cardumes[0] = new Sprite(new Texture("images/obstacles/piranhas.png"));
        cardumes[1] = new Sprite(new Texture("images/obstacles/piranhas2.png"));
        tubaroes[0] = new Sprite(new Texture("images/obstacles/tubaraoinimigo2.png"));
        tubaroes[1] = new Sprite(new Texture("images/obstacles/tubaraoinimigo22.png"));
        poluicoes[0] = new Sprite(new Texture("images/obstacles/baiacu.png"));
        poluicoes[1] = new Sprite(new Texture("images/obstacles/canudo.png"));
        poluicoes[2] = new Sprite(new Texture("images/obstacles/garrafa.png"));
        poluicoes[3] = new Sprite(new Texture("images/obstacles/pneu.png"));
        poluicoes[4] = new Sprite(new Texture("images/obstacles/plastico.png"));
        anzois[0] = new Sprite(new Texture("images/obstacles/anzol1.png"));
        anzois[1] = new Sprite(new Texture("images/obstacles/anzol2.png"));
        anzois[2] = new Sprite(new Texture("images/obstacles/anzol3.png"));
        anzois[3] = new Sprite(new Texture("images/obstacles/anzol4.png"));
        for (int i = 0; i < minhocasScore.length; i++) {
            minhocasScore[i] = new Sprite(new Texture("images/elements/minhoca.png"));
        }
        minhocaBonus = new Sprite(new Texture("images/elements/minhocabonus.png"));
        enfeite[0] = new Texture("images/ornaments/enfeite1.png");
        enfeite[1] = new Texture("images/ornaments/enfeite2.png");
        enfeite[2] = new Texture("images/ornaments/enfeite3.png");
        enfeite[3] = new Texture("images/ornaments/enfeite4.png");
        enfeite[4] = new Texture("images/ornaments/enfeite5.png");
        enfeite[5] = new Texture("images/ornaments/enfeite6.png");
        enfeite[6] = new Texture("images/ornaments/enfeite7.png");
    }

    private void setSeaweed() {
        //set Algas
        algas = new Sprite[3];
        algas[0] = new Sprite(new Texture("images/ornaments/alga1.png"));
        algas[0].setPosition((width - differenceBetweenWidth) / 10, -algas[0].getHeight() / 5);
        algas[0].setSize(algas[0].getWidth() * adjustWidth, algas[0].getHeight() * adjustHeight);

        algas[1] = new Sprite(new Texture("images/ornaments/algaVaria.png"));
        algas[1].setPosition((width - differenceBetweenWidth) / 10, -algas[1].getHeight() / 5);
        algas[1].setSize(algas[1].getWidth() * adjustWidth, algas[1].getHeight() * adjustHeight);

        algas[2] = new Sprite(new Texture("images/ornaments/alga2.png"));
        algas[2].setPosition((width - differenceBetweenWidth) / 2, -algas[2].getHeight() / 5);
        algas[2].setSize(algas[2].getWidth() * adjustWidth, algas[2].getHeight() * adjustHeight);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.width = width;
        this.height = height;
        adjustHeight = (float) (this.height / heightStandard);
        adjustWidth = (float) (this.width / widthStandard);
        differenceBetweenWidth = this.width - originalWidth;
        viewport.update(width, height, true);
        setButtons();
        setSeaweed();
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
            gameOver();
        }
    }
}