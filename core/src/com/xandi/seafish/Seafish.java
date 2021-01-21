package com.xandi.seafish;

import com.badlogic.gdx.ApplicationAdapter;
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
import com.xandi.seafish.interfaces.AdService;
import com.xandi.seafish.interfaces.FacebookAuth;
import com.xandi.seafish.interfaces.GoogleServices;
import com.xandi.seafish.interfaces.LoginCallback;
import com.xandi.seafish.interfaces.PrivacyPolicyAndTerms;
import com.xandi.seafish.interfaces.RankingInterface;
import com.xandi.seafish.interfaces.VideoEventListener;

import java.util.Arrays;
import java.util.Random;

public class Seafish extends ApplicationAdapter implements VideoEventListener, LoginCallback {

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
    private float largura;
    private float altura;
    private float ajusteAltura;
    private float ajusteLargura;
    private double alturaPadrao = 1080;
    private double larguraPadrao = 1920;

    //Score
    private int metragem, velocidadeMetros;
    private BitmapFont metrosLabel;
    private static GlyphLayout metrosLayout;
    private float metrosScore;
    private Integer record;
    private BitmapFont recordLabel;
    private static GlyphLayout recordLayout;

    private BitmapFont tap, userNamefont;
    private String userName;

    //Metros enquanto tubarão
    private float contaMetrosTubarao;

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
    private int i;

    //Rotação algas
    private boolean controlaRot;

    //Sons
    private Sound comeSound, bateSound, gameOverSound, bolhaSound, estouraBolhaSound;
    private Music somFundo;

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

    Seafish(AdService handler, GoogleServices googleServices, RankingInterface rankingInterface, FacebookAuth facebookAuth, PrivacyPolicyAndTerms privacyPolicyAndTerms) {
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

        largura = Gdx.graphics.getWidth();
        altura = Gdx.graphics.getHeight();
        ajusteAltura = (float) (altura / alturaPadrao);
        ajusteLargura = (float) (largura / larguraPadrao);
        if (originalWidth == 0) {
            originalWidth = largura;
        }

        if (viewport == null) {
            OrthographicCamera camera = new OrthographicCamera();
            viewport = new StretchViewport(largura, altura, camera);
        }

        changeLoginButton(userName);

        System.gc();
        //Dimensões padrão

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
        bolha = new Sprite(new Texture("imagens/elementos/bolha.png"));
        bolhaInicio = new Texture("imagens/enfeites/bolhainicio.png");

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
        i = 0;

        //Rotação algas
        controlaRot = false;

        //Variações dos elementos
        variacaoPeixe = 0;
        variacaoPeixeAux = 0;
        variacaoAlgaAux = 0;
        variacaoTubarao = 0;
        variacaoAlga = 0;
        variacaoCardume = 0;

        contaMetrosTubarao = 0;
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

        velocidade = 10 * ajusteLargura;
        for (int i = 0; i < alturaObstaculo.length; i++) {
            if (i == 0) {
                alturaObstaculo[i] = i;
            } else {
                alturaObstaculo[i] = altura / i;
            }
        }

        posicaoInicialVertical = altura / 2;
        movimentoBolhaHorizontal = new float[10];
        movimentoBolhaVertical = new float[10];
        movimentoAnzolVertical = new float[4];

        posicaoBolha = new Random();
        for (int i = 0; i < 10; i++) {
            movimentoBolhaHorizontal[i] = (float) posicaoBolha.nextInt((int) largura * 2);
            movimentoBolhaVertical[i] = (float) posicaoBolha.nextInt((int) altura * 2);
        }

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
        movimentoEnfeiteHorizontal = (largura - differenceBetweenWidth) - enfeite[contaEnfeite].getWidth();

        numObstaculo = new int[4];
        if (!pausa) {
            for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                posicaoMovimentoObstaculoHorizontal[i] = -obstaculos[numObstaculo[i]].getWidth() * ajusteLargura;
            }
        }

        metrosLabel = new BitmapFont();
        metrosLabel.setColor(Color.YELLOW);
        metrosLabel.getData().setScale(6 * ajusteLargura);
        metrosLayout = new GlyphLayout();
        metrosLayout.setText(metrosLabel, (int) metrosScore + "m");

        recordLabel = new BitmapFont();
        recordLabel.setColor(Color.YELLOW);
        recordLabel.getData().setScale(6 * ajusteLargura);
        if (recordLayout == null) {
            recordLayout = new GlyphLayout();
            recordLayout.setText(recordLabel, "Record: " + record + "m");
        }

        tap = new BitmapFont();
        userNamefont = new BitmapFont();
        userNamefont.setColor(Color.YELLOW);
        userNamefont.getData().setScale(3 * ajusteLargura);
        tap.setColor(Color.YELLOW);
        tap.getData().setScale(5 * ajusteLargura);
        metrosScore = 0;
        velocidadeMetros = 100;

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

        AUMENTA_VELOCIDADE = (float) 0.001 * ajusteLargura;
        ALTURA_SALTO = -12 * (ajusteLargura);
        POSICAO_HORIZONTAL_PEIXE = largura / 12;
        ALTURA_TOPO = altura - peixe.getHeight() * ajusteAltura;
        VELOCIDADE_QUEDA = (float) (0.7 * (ajusteLargura));
        VELOCIDADE_OBSTACULO = 12 * ajusteLargura;
        TAP_X = ((largura - differenceBetweenWidth) / 8) + peixe.getWidth();
        TAP_Y = altura - 120;
        VELOCIDADE_INICIAL = velocidade;
        VELOCIDADE_METROS_INICIAL = (int) (100 * ajusteLargura);
        TOQUES_ANZOL1 = 10;
        TOQUES_ANZOL2 = 15;
        TOQUES_ANZOL3 = 20;
        TOQUES_ANZOL4 = 30;
        ALTURA_SELECT_PEIXE = ((altura) - (startGame.getHeight() * ajusteAltura * 5));

        setBotoes();
        setAlgas();

        System.gc();
    }

    @Override
    public void render() {
        super.render();

        //Limpa tela
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Varia o peixe
        variacaoPeixeAux += (float) 0.05;
        variacaoAlgaAux += (float) 0.009;
        variaPeixe();

        switch (estado) {
            case 0:
                menuState();
                break;
            case 1:
                jogoState();
                break;
        }
    }

    private void jogoState() {
        handler.showBannerAd(false);
        //Impede que peixe vá pra baixo do chão (bug)
        if (posicaoInicialVertical < 10) {
            posicaoInicialVertical = altura / 2;
        }

        if (!gameOver) {
            variacaoTubarao += 0.01;
            variacaoCardume += 0.05;

            if (metrosScore == contaMetrosSetor + 500 && !setor) {
                setor = true;
                metrosPorSetor = sorteioMetrosPorSetor.nextInt(250) + 150;
                contaMetrosSetor = (int) metrosScore;
            }
            if (setor) {
                if (metrosScore >= contaMetrosSetor + metrosPorSetor) {
                    setor = false;
                    if (obstaculoAtualSetor <= 2) {
                        obstaculoAtualSetor++;
                    } else {
                        obstaculoAtualSetor = 0;
                    }
                }
            }

            if (movimentoEnfeiteHorizontal < -enfeite[contaEnfeite].getWidth()) {
                movimentoEnfeiteHorizontal = largura + enfeite[contaEnfeite].getWidth();
            }

            variaCardume();
            variaTubarao();

            //controla a velocidade do jogo
            if (iniciado) {
                if (velocidade < 33) {
                    velocidade += AUMENTA_VELOCIDADE;
                }
            }

            controlaRotacaoAlga(true);

            sorteiaProximoObstaculo();

            //faz o peixe subir a cada toque na tela
            if (Gdx.input.justTouched() && !pausa && !musicSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                bolhaSound.play(0.1f);
                iniciado = true;
                velocidadeQueda = ALTURA_SALTO;
                batch.begin();
                batch.draw(peixes[i][variacaoPeixe], POSICAO_HORIZONTAL_PEIXE, posicaoInicialVertical, peixes[i][variacaoPeixe].getWidth() * ajusteLargura, peixes[i][variacaoPeixe].getHeight() * ajusteAltura);
                batch.end();
            }

            if (Gdx.input.justTouched()) {
                if (pauseSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    iniciado = false;
                }
                if (musicSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    if (somFundo.isPlaying()) {
                        somFundo.pause();
                        music = new Texture("imagens/botoes/musicoff.png");
                    } else {
                        somFundo.play();
                        music = new Texture("imagens/botoes/music.png");
                    }
                }
            }

            peixeCircle.set(POSICAO_HORIZONTAL_PEIXE + peixes[i][0].getWidth() * ajusteLargura / 2, (posicaoInicialVertical + peixes[i][0].getHeight() * ajusteAltura / 2), ((peixes[i][0].getHeight() * ajusteLargura / 2) - peixes[i][0].getHeight() * ajusteAltura / 12));

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
                if (metragem >= velocidadeMetros) {
                    metrosScore++;
                    if (velocidadeMetros > 10) {
                        velocidadeMetros -= 0.5;
                    }
                    metragem = 0;
                }

                //Faz o peixe cair (impede que passe do chão)
                if (posicaoInicialVertical > altura / 13 || velocidadeQueda < 0) {
                    posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
                }

                if (colide) {
                    testaColisao();
                } else {
                    aumentaVelocidadeJogo();
                }
            }
            if (colide) {
                testaAnzol();
            }
        }

        batch.begin();
        batch.draw(fundo[contaFundo], 0, 0, largura, altura);
        batch.draw(enfeite[contaEnfeite], movimentoEnfeiteHorizontal, 7, enfeite[contaEnfeite].getWidth() * ajusteLargura, enfeite[contaEnfeite].getHeight() * ajusteAltura);
        algas[variacaoAlga].draw(batch);
        algas[2].draw(batch);
        batch.draw(peixes[i][variacaoPeixe], POSICAO_HORIZONTAL_PEIXE, posicaoInicialVertical, peixes[i][variacaoPeixe].getWidth() * ajusteLargura, peixes[i][variacaoPeixe].getHeight() * ajusteAltura);
        batch.draw(pause, (pause.getWidth()), (altura - ((float) pause.getHeight() * ajusteAltura * 1.5f)), pause.getWidth() * ajusteLargura, pause.getHeight() * ajusteAltura);
        batch.draw(music, (pause.getWidth() * 3f), (altura - ((float) pause.getHeight() * ajusteAltura * 1.5f)), music.getWidth() * ajusteLargura, music.getHeight() * ajusteAltura);

        if (!voltando) {
            sobeDesceAnzol();
        }
        for (int i = 0; i < numObstaculo.length; i++) {
            if (numObstaculo[i] > 6) {
                batch.draw(obstaculos[numObstaculo[i]], posicaoMovimentoObstaculoHorizontal[i], movimentoAnzolVertical[i], obstaculos[numObstaculo[i]].getWidth() * ajusteLargura, obstaculos[numObstaculo[i]].getHeight() * ajusteAltura);
            } else {
                batch.draw(obstaculos[numObstaculo[i]], posicaoMovimentoObstaculoHorizontal[i], alturaObstaculo[i], obstaculos[numObstaculo[i]].getWidth() * ajusteLargura, obstaculos[numObstaculo[i]].getHeight() * ajusteAltura);
            }
        }

        //Set retangulos colisoes minhocas
        for (int i = 0; i < minhocasRect.length; i++) {
            if (!isSlowShark && alturaMinhoca[i] > altura / 13) {
                minhocasRect[i].set(posicaoMovimentoObstaculoHorizontal[i] - distanciaMinhoca[i], alturaMinhoca[i], minhocasScore[i].getWidth() * ajusteLargura, minhocasScore[i].getHeight() * ajusteAltura);

                if (!minhocaColidiu[i] && !pausa) {
                    batch.draw(minhocasScore[i], posicaoMovimentoObstaculoHorizontal[i] - distanciaMinhoca[i], alturaMinhoca[i], minhocasScore[i].getWidth() * ajusteLargura, minhocasScore[i].getHeight() * ajusteAltura);
                }
            }
        }

        if (metrosScore >= 2500 && metrosScore % 2500 == 0 && !vidaExtra) {
            mostraBolha = true;
            bolhaHorizontal = largura;
        }
        if (bolhaHorizontal < 0) {
            mostraBolha = false;
        }
        if (mostraBolha) {
            bolhaCircle.set(bolhaHorizontal, altura / 2, (float) (bolha.getTexture().getWidth() / 2) * ajusteLargura);
            batch.draw(bolha, bolhaHorizontal, altura / 2, bolha.getWidth() * ajusteLargura, bolha.getHeight() * ajusteAltura);
        }
        if (Intersector.overlaps(peixeCircle, bolhaCircle) && mostraBolha) {
            estouraBolhaSound.play();
            mostraBolha = false;
            vidaExtra = true;
        }
        if (metrosScore % 650 == 0 && metrosScore != 0) {
            mostraMinhocaBonus = true;
            minhocaBonusHorizontal = largura;
        }
        if (minhocaBonusHorizontal < minhocaBonus.getWidth() * ajusteLargura) {
            mostraMinhocaBonus = false;
        }
        if (mostraMinhocaBonus && !isShark && !isSlowShark) {
            minhocaBonusRect.set(minhocaBonusHorizontal, altura / 2, minhocaBonus.getWidth() * ajusteLargura, minhocaBonus.getHeight() * ajusteAltura);
            batch.draw(minhocaBonus, minhocaBonusHorizontal, altura / 2, minhocaBonus.getWidth() * ajusteLargura, minhocaBonus.getHeight() * ajusteAltura);
        }

        if (Intersector.overlaps(peixeCircle, minhocaBonusRect) && mostraMinhocaBonus) {
            mostraMinhocaBonus = false;
            colide = false;
        }

        if (Intersector.overlaps(peixeCircle, minhocasRect[0])) {
            contaMinhoca(0);
            minhocaColidiu[0] = true;
        } else if (Intersector.overlaps(peixeCircle, minhocasRect[1])) {
            contaMinhoca(1);
            minhocaColidiu[1] = true;
        } else if (Intersector.overlaps(peixeCircle, minhocasRect[2])) {
            contaMinhoca(2);
            minhocaColidiu[2] = true;
        } else if (Intersector.overlaps(peixeCircle, minhocasRect[3])) {
            contaMinhoca(3);
            minhocaColidiu[3] = true;
        } else {
            colidiuMinhoca[0] = false;
            colidiuMinhoca[1] = false;
            colidiuMinhoca[2] = false;
            colidiuMinhoca[3] = false;
        }

        for (int i = 1; i <= numMinhocas; i++) {
            batch.draw(minhocasScore[i - 1], (float) ((largura - differenceBetweenWidth) - (minhocasScore[i - 1].getWidth() * ajusteLargura * i * 1.5)), (altura - recordLayout.height - (minhocasScore[i - 1].getHeight() * ajusteAltura * 1.5f)), minhocasScore[i - 1].getWidth() * ajusteLargura, minhocasScore[i - 1].getHeight() * ajusteAltura);
        }
        if (vidaExtra) {
            batch.draw(bolha, (float) ((largura - differenceBetweenWidth) - (minhocaBonus.getWidth() * ajusteLargura * (numMinhocas + 2) * 1.5)), (float) (altura - minhocaBonus.getHeight() * ajusteAltura * 1.3), bolha.getTexture().getWidth() * ajusteLargura, bolha.getTexture().getHeight() * ajusteAltura);
        }

        metrosLabel.draw(batch, (int) metrosScore + "m", (largura - differenceBetweenWidth) / 2 - (metrosLayout.width / 2), altura - (altura / 10));
        recordLabel.draw(batch, recordLayout, (largura - differenceBetweenWidth - (recordLayout.width) - 15), altura - 15);
        if (gameOver) {
            batch.draw(gameOverText, ((largura - differenceBetweenWidth) / 2) - (gameOverText.getWidth() * ajusteLargura / 2), (float) (altura - (gameOverText.getHeight() * ajusteAltura * 2.5)), gameOverText.getWidth() * ajusteLargura, gameOverText.getHeight() * ajusteAltura);
            batch.draw(reload, ((largura - differenceBetweenWidth) / 2) - (reload.getWidth() * ajusteLargura), altura / 2, 150 * ajusteLargura, 150 * ajusteAltura);
            batch.draw(menuBotao, ((largura - differenceBetweenWidth) / 2) - (menuBotao.getWidth() * ajusteLargura / 2), (float) ((altura / 3) - menuSprite.getHeight() * ajusteAltura * 1.5), menuBotao.getWidth() * ajusteLargura, menuBotao.getHeight() * ajusteAltura);
        }

        if (mostraTelaSegueJogo) {
            batch.draw(continueText, ((largura - differenceBetweenWidth) / 2) - (continueText.getWidth() * ajusteLargura / 2), (float) (altura - (continueText.getHeight() * ajusteAltura * 2.5)), continueText.getWidth() * ajusteLargura, continueText.getHeight() * ajusteAltura);
            batch.draw(simBotao, (float) (((largura - differenceBetweenWidth) / 2) - (simBotao.getWidth() * ajusteLargura * 1.5)), (float) ((altura / 3) - simSprite.getHeight() * ajusteAltura * 1.5), simBotao.getWidth() * ajusteLargura, simBotao.getHeight() * ajusteAltura);
            batch.draw(naoBotao, (((largura - differenceBetweenWidth) / 2) + (naoBotao.getWidth() * ajusteLargura)), (float) ((altura / 3) - naoSprite.getHeight() * ajusteAltura * 1.5), naoBotao.getWidth() * ajusteLargura, naoBotao.getHeight() * ajusteAltura);
            batch.draw(videoIcon, (float) (((largura - differenceBetweenWidth) / 2) - (simBotao.getWidth() * ajusteLargura * 1.5) + videoIcon.getWidth() * ajusteLargura * 5), (float) ((altura / 3) - simSprite.getHeight() * ajusteAltura * 1.5), simBotao.getWidth() * ajusteLargura, simBotao.getHeight() * ajusteAltura);
        }

        if (voltando) {
            if (variacaoPeixe == 0) {
                tap.draw(batch, "tap!", TAP_X, TAP_Y);
            }
        }

        batch.end();

        setObstaculos();

        //Botões game over
        if (Gdx.input.justTouched()) {
            if (gameOver) {
                if (replaySprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    gameOver = false;
                    int peixe = i;
                    System.gc();
                    create();
                    estado = 1;
                    if (metrosScore < 300) {
                        contaPartidas++;
                    }
                    i = peixe;
                }
                if (menuSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    System.gc();
                    create();
                    if (metrosScore < 300) {
                        contaPartidas++;
                    }
                }
            }
        }

        if (Gdx.input.justTouched()) {
            if (mostraTelaSegueJogo) {
                if (simSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    this.googleServices.showRewardedVideoAd();
                    mostraTelaSegueJogo = false;
                }
                if (naoSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    mostraTelaSegueJogo = false;
                    gameOver();
                }
            }
        }
    }

    private void menuState() {
        handler.showBannerAd(true);
        //Se o botão iniciar jogo for clicado
        if (Gdx.input.justTouched()) {
            if (playSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (i == 0 || i == 1 || i == 2 || (i == 3 && record >= 2000) || (i == 4 && record >= 4000) || (i == 5 && record >= 6000) || (i == 6 && record >= 8000)) {
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

        batch.draw(telaInicial, 0, 0, largura, altura);
        batch.draw(startGame, ((largura - differenceBetweenWidth) / 2) - (startGame.getWidth() * ajusteLargura / 2), ((altura) - (startGame.getHeight() * ajusteAltura * 3)), startGame.getWidth() * ajusteLargura, startGame.getHeight() * ajusteAltura);

        batch.draw(terms, (largura - differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * ajusteLargura), ((privacyPolicy.getHeight() / 1.5f) * ajusteAltura) + (privacyPolicy.getHeight() * ajusteAltura * 1.2f), (terms.getWidth() / 2f) * ajusteLargura, terms.getHeight() * ajusteAltura);
        batch.draw(privacyPolicy, (largura - differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * ajusteLargura), ((privacyPolicy.getHeight() / 1.5f) * ajusteAltura), (privacyPolicy.getWidth() / 2f) * ajusteLargura, privacyPolicy.getHeight() * ajusteAltura);

        batch.draw(ranking, (ranking.getWidth() * ajusteLargura) / 2.5f, (altura / 2f), ranking.getWidth() * ajusteLargura, ranking.getHeight() * ajusteAltura);
        batch.draw(loginFb, (loginFb.getWidth() * ajusteLargura) / 10f, (altura / 2f) - (ranking.getHeight() * ajusteAltura), (loginFb.getWidth() * ajusteLargura / 2f), loginFb.getHeight() * ajusteAltura);

        if (userName != null) {
            userNamefont.draw(batch, userName, (loginFb.getWidth() * ajusteLargura) / 10f, (altura / 2f) - (loginFb.getHeight() * ajusteAltura * 2f) - (loginFb.getHeight() * ajusteAltura) / 10f);
        }

        algas[variacaoAlga].draw(batch);
        algas[2].draw(batch);
        batch.draw(back, ((largura - differenceBetweenWidth) / 2) - (back.getWidth() * 1.85f * ajusteLargura), ALTURA_SELECT_PEIXE + (back.getHeight() * ajusteAltura), back.getWidth() * ajusteLargura, back.getHeight() * ajusteAltura);
        batch.draw(next, ((largura - differenceBetweenWidth) / 2) + (next.getWidth() * ajusteLargura), ALTURA_SELECT_PEIXE + (next.getHeight() * ajusteAltura), next.getWidth() * ajusteLargura, next.getHeight() * ajusteAltura);
        batch.draw(peixes[i][variacaoPeixe], ((largura - differenceBetweenWidth) / 2) - (peixes[i][0].getWidth() * ajusteLargura / 2), ALTURA_SELECT_PEIXE + 70, peixes[i][0].getWidth() * ajusteLargura, peixes[i][0].getHeight() * ajusteAltura);

        for (int i = 0; i < 10; i++) {
            if (movimentoBolhaHorizontal[i] >= (largura - differenceBetweenWidth) - bolhaInicio.getWidth() * ajusteLargura) {
                bolhaTocouLado[i] = true;
            }

            if (movimentoBolhaHorizontal[i] <= 0) {
                bolhaTocouLado[i] = false;
            }

            if (bolhaTocouLado[i]) {
                movimentoBolhaHorizontal[i] -= (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(50) + 1) * ajusteLargura;
            } else {
                movimentoBolhaHorizontal[i] += (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(200) + 1) * ajusteLargura;
            }

            if (movimentoBolhaVertical[i] >= altura - bolhaInicio.getHeight() * ajusteAltura) {
                bolhaTocouTopo[i] = true;
            }

            if (movimentoBolhaVertical[i] <= 0) {
                bolhaTocouTopo[i] = false;
            }

            if (bolhaTocouTopo[i]) {
                movimentoBolhaVertical[i] -= (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(100) + 1) * ajusteAltura;
            } else {
                movimentoBolhaVertical[i] += (Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(150) + 1) * ajusteAltura;
            }
            batch.draw(bolhaInicio, movimentoBolhaHorizontal[i], movimentoBolhaVertical[i], bolhaInicio.getWidth() * ajusteLargura, bolhaInicio.getHeight() * ajusteLargura);
        }

        batch.end();

        controlaRotacaoAlga(false);
        variaAlga();

        selecionaPeixe();
    }

    private void changeLoginButton(String name) {
        userName = name;
        if (facebookAuth.isLoggedIn()) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    loginFb = new Texture("imagens/botoes/logout.png");
                }
            });
        } else {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    loginFb = new Texture("imagens/botoes/login.png");
                }
            });
        }
    }

    private void sobeDesceAnzol() {
        for (int i = 0; i < anzolTocouTopo.length; i++) {
            if (movimentoAnzolVertical[i] >= altura - 10) {
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

    private void variaTubarao() {
        if (variacaoTubarao > 1) {
            variacaoTubarao = 0;
        } else if (variacaoTubarao >= 0.5) {
            obstaculos[1] = tubaroes[0];
        } else {
            obstaculos[1] = tubaroes[1];
        }
    }

    private void variaAlga() {
        if (variacaoAlgaAux > 1) {
            variacaoAlgaAux = 0;
        }
        if (variacaoAlgaAux >= 0.5) {
            variacaoAlga = 0;
        } else {
            variacaoAlga = 1;
        }
    }

    private void variaPeixe() {
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

    private void variaCardume() {
        if (variacaoCardume > 1) {
            variacaoCardume = 0;
        } else if (variacaoCardume >= 0.5) {
            obstaculos[0] = cardumes[0];
        } else {
            obstaculos[0] = cardumes[1];
        }
    }

    private void selecionaPeixe() {
        if (Gdx.input.justTouched()) {
            if (backSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (i == 0) {
                    i = 6;
                } else {
                    i--;
                }
            }
        }
        if (Gdx.input.justTouched()) {
            if (nextSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (i == 6) {
                    i = 0;
                } else {
                    i++;
                }
            }
        }
    }

    private void controlaRotacaoAlga(boolean mudaPosicao) {
        if (controlaRot) {
            if (algas[2].getRotation() <= -35 * (ajusteLargura)) {
                controlaRot = false;
            }
            algas[2].rotate((float) -0.4 * ajusteLargura);
        } else {
            if (algas[2].getRotation() >= 20 * (ajusteLargura)) {
                controlaRot = true;
            }
            algas[2].rotate((float) 0.4 * ajusteLargura);
        }
        if (mudaPosicao) {
            algas[variacaoAlga].setPosition(posicaoMovimentoObstaculoHorizontal[variacaoAlga], ((-algas[variacaoAlga].getTexture().getHeight()) * ajusteAltura) / 5);
            algas[2].setPosition(posicaoMovimentoObstaculoHorizontal[2], ((-algas[2].getTexture().getHeight()) * ajusteAltura) / 5);
        }
    }

    private void testaAnzol() {
        if (Intersector.overlaps(peixeCircle, anzoisCircle[0])) {
            voltaProInicio(1);
        } else if (Intersector.overlaps(peixeCircle, anzoisCircle[1])) {
            voltaProInicio(2);
        } else if (Intersector.overlaps(peixeCircle, anzoisCircle[2])) {
            voltaProInicio(3);
        } else if (Intersector.overlaps(peixeCircle, anzoisCircle[3])) {
            voltaProInicio(4);
        } else {
            voltando = false;
            if (!mostraTelaSegueJogo) {
                pausa = false;
            }
        }
    }

    private void voltaProInicio(float circle) {
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

            if (metrosScore > 0) {
                sorteiaObstaculoAnterior();
            } else {
                gameOver = false;
                colide = true;
                posicaoInicialVertical = posicaoInicialVertical - 30;
                velocidade = VELOCIDADE_INICIAL;
                velocidadeMetros = VELOCIDADE_METROS_INICIAL;
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
                    posicaoMovimentoObstaculoHorizontal[0] = largura;
                    posicaoMovimentoObstaculoHorizontal[1] = (float) ((largura - differenceBetweenWidth) * 1.5);
                    posicaoMovimentoObstaculoHorizontal[2] = largura * 2;
                    posicaoMovimentoObstaculoHorizontal[3] = (float) ((largura - differenceBetweenWidth) * 2.5);
                    gameOver = false;
                    colide = true;
                    posicaoInicialVertical = altura / 2;
                    toques = 0;

                    if (metrosScore >= 4000) {
                        velocidade = 33 * ajusteLargura;
                        velocidadeMetros = (int) (VELOCIDADE_INICIAL);
                    } else if (metrosScore >= 3000) {
                        velocidade = 30 * ajusteLargura;
                        velocidadeMetros = (int) (25 * ajusteLargura);
                    } else if (metrosScore >= 25000) {
                        velocidade = 27 * ajusteLargura;
                        velocidadeMetros = (int) (36 * ajusteLargura);
                    } else if (metrosScore >= 2000) {
                        velocidade = 24 * ajusteLargura;
                        velocidadeMetros = (int) (47 * ajusteLargura);
                    } else if (metrosScore >= 1500) {
                        velocidade = 21 * ajusteLargura;
                        velocidadeMetros = (int) (58 * ajusteLargura);
                    } else if (metrosScore >= 1000) {
                        velocidade = 18 * ajusteLargura;
                        velocidadeMetros = (int) (71 * ajusteLargura);
                    } else if (metrosScore >= 500) {
                        velocidade = 15 * ajusteLargura;
                        velocidadeMetros = (int) (82 * ajusteLargura);
                    } else if (metrosScore >= 250) {
                        velocidade = 12 * ajusteLargura;
                        velocidadeMetros = (int) (90 * ajusteLargura);
                    } else if (metrosScore <= 5) {
                        velocidade = VELOCIDADE_INICIAL;
                        velocidadeMetros = (int) (100 * ajusteLargura);
                    }
                }
            }
        }
    }

    private void contaMinhoca(int numMinhoca) {
        if (!colidiuMinhoca[numMinhoca]) {
            if (numMinhocas <= 9) {
                if (!isSlowShark) {
                    numMinhocas++;
                    comeSound.play(0.9f);
                    colidiuMinhoca[numMinhoca] = true;
                }
            } else if (!isShark) {
                numMinhocas = 1;
                colide = false;
                mudaFundo();
            }
        }
    }

    private void mudaFundo() {
        if (contaFundo <= 5) {
            contaFundo++;
            contaEnfeite++;
        } else {
            contaFundo = 0;
            contaEnfeite = 0;
        }
    }

    private void aumentaVelocidadeJogo() {
        contaMetrosTubarao += 0.5;
        if (contaMetrosTubarao <= 100) {
            this.metrosScore += 0.5;
            for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                posicaoMovimentoObstaculoHorizontal[i] -= velocidade * (VELOCIDADE_INICIAL);
            }
            movimentoEnfeiteHorizontal -= velocidade * VELOCIDADE_INICIAL;
            isShark = true;
        } else if (contaMetrosTubarao <= 190) {
            if (contaMetrosTubarao % 5 == 0) {
                mudaFundo();
            }
            isSlowShark = true;
            isShark = true;
        } else {
            colide = true;
            contaMetrosTubarao = 0;
            isSlowShark = false;
            isShark = false;
        }
    }

    private void testaColisao() {
        if (Intersector.overlaps(peixeCircle, piranhasHorizontalRect[0]) || Intersector.overlaps(peixeCircle, piranhasHorizontalRect[1]) ||
                Intersector.overlaps(peixeCircle, piranhasHorizontalRect[2]) || Intersector.overlaps(peixeCircle, piranhasHorizontalRect[3]) ||
                Intersector.overlaps(peixeCircle, piranhasVerticalRect[0]) || Intersector.overlaps(peixeCircle, piranhasVerticalRect[1]) ||
                Intersector.overlaps(peixeCircle, piranhasVerticalRect[2]) || Intersector.overlaps(peixeCircle, piranhasVerticalRect[3]) ||
                Intersector.overlaps(peixeCircle, tubaroesRect[0]) || Intersector.overlaps(peixeCircle, tubaroesRect[1]) ||
                Intersector.overlaps(peixeCircle, tubaroesRect[2]) || Intersector.overlaps(peixeCircle, tubaroesRect[3])) {

            actionAoColidir(2);

        } else if (Intersector.overlaps(peixeCircle, poluicoesCircle[0]) || Intersector.overlaps(peixeCircle, poluicoesCircle[1]) ||
                Intersector.overlaps(peixeCircle, poluicoesCircle[2]) || Intersector.overlaps(peixeCircle, poluicoesCircle[3]) ||
                Intersector.overlaps(peixeCircle, poluicoesCircle[4])) {

            actionAoColidir(4);

        } else {
            colidiuObstaculo = false;
            isColiding = false;
        }
        if (isColiding) {
            variacaoPeixe = 2;
        }
    }

    private void actionAoColidir(int descontaVidas) {
        isColiding = true;
        if (!colidiuObstaculo) {
            Gdx.input.vibrate(50);
            numMinhocas -= descontaVidas;
            bateSound.play();
            colidiuObstaculo = true;
            if (numMinhocas < 0) {
                if (vidaExtra) {
                    vidaExtra = false;
                    mostraBolha = true;
                    numMinhocas = 0;
                } else {
                    if (!isRewardedYet && mostraVideoPremiado()) {
                        if (this.googleServices.hasVideoLoaded()) {
                            iniciado = false;
                            pausa = true;
                            mostraTelaSegueJogo = true;
                        } else {
                            gameOver();
                        }
                    } else {
                        gameOver();
                        if (contaPartidas == 2 || metrosScore >= 300) {
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
        somFundo.dispose();
        gameOver = true;
        gameOverSound.play();
        saveScores();
    }

    private void saveScores() {
        rankingInterface.saveRecord((int) metrosScore);
        if (metrosScore > record) {
            prefs.putInteger("score", (int) metrosScore);
            prefs.flush();
        }
    }

    private boolean mostraVideoPremiado() {
        return record >= 70 && metrosScore >= record - (record / 3F) && metrosScore <= record;
    }

    private void setObstaculos() {

        if (numObstaculo[0] == 0) {
            piranhasVerticalRect[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 3, alturaObstaculo[0], (obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[0]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[0].set(posicaoMovimentoObstaculoHorizontal[0], alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[0]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[0]].getHeight() * ajusteAltura / 3));
        } else if (numObstaculo[0] == 1) {
            tubaroesRect[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[0]].getHeight() * ajusteAltura / 2.5));
        } else if (numObstaculo[0] == 2 || numObstaculo[0] == 3 || numObstaculo[0] == 4 || numObstaculo[0] == 5 || numObstaculo[0] == 6) {
            poluicoesCircle[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2, alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2);
        } else if (numObstaculo[0] == 7 || numObstaculo[0] == 8 || numObstaculo[0] == 9 || numObstaculo[0] == 10) {
            anzoisCircle[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2, movimentoAnzolVertical[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2));
        }

        if (numObstaculo[1] == 0) {
            piranhasVerticalRect[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 3, alturaObstaculo[1], (obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[1]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[1].set(posicaoMovimentoObstaculoHorizontal[1], alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[1]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 3));
        } else if (numObstaculo[1] == 1) {
            tubaroesRect[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 2.5));
        } else if (numObstaculo[1] == 2 || numObstaculo[1] == 3 || numObstaculo[1] == 4 || numObstaculo[1] == 5 || numObstaculo[1] == 6) {
            poluicoesCircle[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2, alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2);
        } else if (numObstaculo[1] == 7 || numObstaculo[1] == 8 || numObstaculo[1] == 9 || numObstaculo[1] == 10) {
            anzoisCircle[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2, movimentoAnzolVertical[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2));
        }

        if (numObstaculo[2] == 0) {
            piranhasVerticalRect[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 3, alturaObstaculo[2], (obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[2]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[2].set(posicaoMovimentoObstaculoHorizontal[2], alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[2]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 3));
        } else if (numObstaculo[2] == 1) {
            tubaroesRect[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 2.5));
        } else if (numObstaculo[2] == 2 || numObstaculo[2] == 3 || numObstaculo[2] == 4 || numObstaculo[2] == 5 || numObstaculo[2] == 6) {
            poluicoesCircle[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2, alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2);
        } else if (numObstaculo[2] == 7 || numObstaculo[2] == 8 || numObstaculo[2] == 9 || numObstaculo[2] == 10) {
            anzoisCircle[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2, movimentoAnzolVertical[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2));
        }

        if (numObstaculo[3] == 0) {
            piranhasVerticalRect[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 3, alturaObstaculo[3], (obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[3]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[3].set(posicaoMovimentoObstaculoHorizontal[3], alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[3]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 3));
        } else if (numObstaculo[3] == 1) {
            tubaroesRect[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 2.5));
        } else if (numObstaculo[3] == 2 || numObstaculo[3] == 3 || numObstaculo[3] == 4 || numObstaculo[3] == 5 || numObstaculo[3] == 6) {
            poluicoesCircle[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2, alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2);
        } else if (numObstaculo[3] == 7 || numObstaculo[3] == 8 || numObstaculo[3] == 9 || numObstaculo[3] == 10) {
            anzoisCircle[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2, movimentoAnzolVertical[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2));
        }
    }

    private void sorteiaProximoObstaculo() {
        float multLargura = (float) 2.5;
        //Verifica se o obstáculo saiu da tela e manda o próximo obstáculo
        for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {

            if (posicaoMovimentoObstaculoHorizontal[i] < -obstaculos[numObstaculo[i]].getWidth() * ajusteLargura) {
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
                    alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (obstaculos[numObstaculo[i]].getHeight() * ajusteAltura / 1.5));
                    movimentoAnzolVertical[i] = alturaObstaculo[i];
                } else {
                    alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (altura - obstaculos[numObstaculo[i]].getHeight() * ajusteAltura / 1.5));
                }

                posicaoMovimentoObstaculoHorizontal[i] = ((largura - differenceBetweenWidth) * multLargura);
                if (!isSlowShark) {
                    distanciaMinhoca[i] = distanciaMinhocaRandom.nextInt((int) (obstaculos[numObstaculo[i]].getWidth() * ajusteLargura * 2));
                    alturaMinhoca[i] = (int) alturaObstaculo[i] - ((alturaMinhocaRandom.nextInt((int) (obstaculos[numObstaculo[i]].getHeight() * ajusteAltura * 2))));
                    minhocaColidiu[i] = false;
                }
            }
            multLargura -= 0.5;
        }
    }

    private void sorteiaObstaculoAnterior() {
        float multLargura = (float) 2.5;
        //Verifica se o obstáculo saiu da tela e manda o próximo obstáculo
        for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {

            if (posicaoMovimentoObstaculoHorizontal[i] > largura + obstaculos[numObstaculo[i]].getWidth() * ajusteLargura) {
                metrosScore -= 0.5;
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
                    alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (obstaculos[numObstaculo[i]].getHeight() * ajusteAltura / 1.5));
                } else {
                    alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (altura - obstaculos[numObstaculo[i]].getHeight() * ajusteAltura / 1.5));
                }
                posicaoMovimentoObstaculoHorizontal[i] = (-obstaculos[i].getWidth() * ajusteLargura * multLargura);
            }
            multLargura -= 0.5;

        }
    }

    private void setPeixes() {
        peixes[0][0] = new Sprite(new Texture("imagens/personagens/peixe1.png"));
        peixes[0][1] = new Sprite(new Texture("imagens/personagens/peixe12.png"));
        peixes[0][2] = new Sprite(new Texture("imagens/personagens/peixe1red.png"));
        peixes[0][3] = new Sprite(new Texture("imagens/personagens/tubarao1.png"));
        peixes[0][4] = new Sprite(new Texture("imagens/personagens/tubarao12.png"));
        peixes[1][0] = new Sprite(new Texture("imagens/personagens/peixe2.png"));
        peixes[1][1] = new Sprite(new Texture("imagens/personagens/peixe22.png"));
        peixes[1][2] = new Sprite(new Texture("imagens/personagens/peixe2red.png"));
        peixes[1][3] = new Sprite(new Texture("imagens/personagens/tubarao2.png"));
        peixes[1][4] = new Sprite(new Texture("imagens/personagens/tubarao22.png"));
        peixes[2][0] = new Sprite(new Texture("imagens/personagens/peixe3.png"));
        peixes[2][1] = new Sprite(new Texture("imagens/personagens/peixe32.png"));
        peixes[2][2] = new Sprite(new Texture("imagens/personagens/peixe3red.png"));
        peixes[2][3] = new Sprite(new Texture("imagens/personagens/tubarao3.png"));
        peixes[2][4] = new Sprite(new Texture("imagens/personagens/tubarao32.png"));

        if (record < 2000) {
            peixes[3][0] = new Sprite(new Texture("imagens/personagens/sombra1.png"));
            peixes[3][1] = new Sprite(new Texture("imagens/personagens/sombra1.png"));
        } else {
            peixes[3][0] = new Sprite(new Texture("imagens/personagens/peixe4.png"));
            peixes[3][1] = new Sprite(new Texture("imagens/personagens/peixe42.png"));
            peixes[3][2] = new Sprite(new Texture("imagens/personagens/peixe4red.png"));
            peixes[3][3] = new Sprite(new Texture("imagens/personagens/tubarao4.png"));
            peixes[3][4] = new Sprite(new Texture("imagens/personagens/tubarao42.png"));
        }

        if (record < 4000) {
            peixes[4][0] = new Sprite(new Texture("imagens/personagens/sombra2.png"));
            peixes[4][1] = new Sprite(new Texture("imagens/personagens/sombra2.png"));
        } else {
            peixes[4][0] = new Sprite(new Texture("imagens/personagens/peixe5.png"));
            peixes[4][1] = new Sprite(new Texture("imagens/personagens/peixe52.png"));
            peixes[4][2] = new Sprite(new Texture("imagens/personagens/peixe5red.png"));
            peixes[4][3] = new Sprite(new Texture("imagens/personagens/tubarao5.png"));
            peixes[4][4] = new Sprite(new Texture("imagens/personagens/tubarao52.png"));
        }

        if (record < 6000) {
            peixes[5][0] = new Sprite(new Texture("imagens/personagens/sombra3.png"));
            peixes[5][1] = new Sprite(new Texture("imagens/personagens/sombra3.png"));
        } else {
            peixes[5][0] = new Sprite(new Texture("imagens/personagens/peixe6.png"));
            peixes[5][1] = new Sprite(new Texture("imagens/personagens/peixe62.png"));
            peixes[5][2] = new Sprite(new Texture("imagens/personagens/peixe6red.png"));
            peixes[5][3] = new Sprite(new Texture("imagens/personagens/tubarao6.png"));
            peixes[5][4] = new Sprite(new Texture("imagens/personagens/tubarao62.png"));
        }

        if (record < 8000) {
            peixes[6][0] = new Sprite(new Texture("imagens/personagens/sombra4.png"));
            peixes[6][1] = new Sprite(new Texture("imagens/personagens/sombra4.png"));
        } else {
            peixes[6][0] = new Sprite(new Texture("imagens/personagens/peixe7.png"));
            peixes[6][1] = new Sprite(new Texture("imagens/personagens/peixe72.png"));
            peixes[6][2] = new Sprite(new Texture("imagens/personagens/peixe7red.png"));
            peixes[6][3] = new Sprite(new Texture("imagens/personagens/tubarao7.png"));
            peixes[6][4] = new Sprite(new Texture("imagens/personagens/tubarao72.png"));
        }
    }

    private void setTextures() {
        //Texturas
        peixe = new Sprite(new Texture("imagens/personagens/peixe1.png"));
        fundo[0] = new Texture("imagens/cenarios/fundo1.png");
        fundo[1] = new Texture("imagens/cenarios/fundo2.png");
        fundo[2] = new Texture("imagens/cenarios/fundo3.png");
        fundo[3] = new Texture("imagens/cenarios/fundo4.png");
        fundo[4] = new Texture("imagens/cenarios/fundo5.png");
        fundo[5] = new Texture("imagens/cenarios/fundo6.png");
        fundo[6] = new Texture("imagens/cenarios/fundo7.png");
        telaInicial = new Texture("imagens/cenarios/telainicio.png");
        gameOverText = new Texture("imagens/textos/gameover.png");
        continueText = new Texture("imagens/textos/continue.png");
        reload = new Texture("imagens/botoes/refresh.png");
        music = new Texture("imagens/botoes/music.png");
        pause = new Texture("imagens/botoes/pause.png");
        menuBotao = new Texture("imagens/botoes/menu.png");
        simBotao = new Texture("imagens/botoes/yes.png");
        videoIcon = new Texture("imagens/botoes/video.png");
        naoBotao = new Texture("imagens/botoes/no.png");
        startGame = new Texture("imagens/botoes/startgame.png");
        loginFb = new Texture("imagens/botoes/login.png");
        ranking = new Texture("imagens/botoes/ranking.png");
        terms = new Texture("imagens/botoes/terms.png");
        privacyPolicy = new Texture("imagens/botoes/privacypolicy.png");
        next = new Texture("imagens/botoes/next.png");
        back = new Texture("imagens/botoes/back.png");
        setPeixes();
        cardumes[0] = new Sprite(new Texture("imagens/obstaculos/piranhas.png"));
        cardumes[1] = new Sprite(new Texture("imagens/obstaculos/piranhas2.png"));
        tubaroes[0] = new Sprite(new Texture("imagens/obstaculos/tubaraoinimigo2.png"));
        tubaroes[1] = new Sprite(new Texture("imagens/obstaculos/tubaraoinimigo22.png"));
        poluicoes[0] = new Sprite(new Texture("imagens/obstaculos/baiacu.png"));
        poluicoes[1] = new Sprite(new Texture("imagens/obstaculos/canudo.png"));
        poluicoes[2] = new Sprite(new Texture("imagens/obstaculos/garrafa.png"));
        poluicoes[3] = new Sprite(new Texture("imagens/obstaculos/pneu.png"));
        poluicoes[4] = new Sprite(new Texture("imagens/obstaculos/plastico.png"));
        anzois[0] = new Sprite(new Texture("imagens/obstaculos/anzol1.png"));
        anzois[1] = new Sprite(new Texture("imagens/obstaculos/anzol2.png"));
        anzois[2] = new Sprite(new Texture("imagens/obstaculos/anzol3.png"));
        anzois[3] = new Sprite(new Texture("imagens/obstaculos/anzol4.png"));
        for (int i = 0; i < minhocasScore.length; i++) {
            minhocasScore[i] = new Sprite(new Texture("imagens/elementos/minhoca.png"));
        }
        minhocaBonus = new Sprite(new Texture("imagens/elementos/minhocabonus.png"));
        enfeite[0] = new Texture("imagens/enfeites/enfeite1.png");
        enfeite[1] = new Texture("imagens/enfeites/enfeite2.png");
        enfeite[2] = new Texture("imagens/enfeites/enfeite3.png");
        enfeite[3] = new Texture("imagens/enfeites/enfeite4.png");
        enfeite[4] = new Texture("imagens/enfeites/enfeite5.png");
        enfeite[5] = new Texture("imagens/enfeites/enfeite6.png");
        enfeite[6] = new Texture("imagens/enfeites/enfeite7.png");
    }

    private void setAlgas() {
        //set Algas
        algas = new Sprite[3];
        algas[0] = new Sprite(new Texture("imagens/enfeites/alga1.png"));
        algas[0].setPosition((largura - differenceBetweenWidth) / 10, -algas[0].getHeight() / 5);
        algas[0].setSize(algas[0].getWidth(), algas[0].getHeight());

        algas[1] = new Sprite(new Texture("imagens/enfeites/algaVaria.png"));
        algas[1].setPosition((largura - differenceBetweenWidth) / 10, -algas[1].getHeight() / 5);
        algas[1].setSize(algas[1].getWidth(), algas[1].getHeight());

        algas[2] = new Sprite(new Texture("imagens/enfeites/alga2.png"));
        algas[2].setPosition((largura - differenceBetweenWidth) / 2, -algas[2].getHeight() / 5);
        algas[2].setSize(algas[2].getWidth(), algas[2].getHeight());
    }

    private void setBotoes() {
        //Sobrepor Botões
        menuSprite = new Sprite(menuBotao);
        menuSprite.setSize(menuSprite.getWidth() * ajusteLargura, menuSprite.getHeight() * ajusteAltura);
        menuSprite.setPosition(((largura - differenceBetweenWidth) / 2) - (menuBotao.getWidth() * ajusteLargura / 2), (float) ((altura / 3) - menuSprite.getHeight() * ajusteAltura * 1.5));

        simSprite = new Sprite(simBotao);
        simSprite.setSize(simSprite.getWidth() * ajusteLargura, simSprite.getHeight() * ajusteAltura);
        simSprite.setPosition((float) (((largura - differenceBetweenWidth) / 2) - (simBotao.getWidth() * ajusteLargura * 1.5)), (float) ((altura / 3) - simSprite.getHeight() * ajusteAltura * 1.5));

        naoSprite = new Sprite(naoBotao);
        naoSprite.setSize(naoSprite.getWidth() * ajusteLargura, naoSprite.getHeight() * ajusteAltura);
        naoSprite.setPosition((((largura - differenceBetweenWidth) / 2) + (naoBotao.getWidth() * ajusteLargura)), (float) ((altura / 3) - naoSprite.getHeight() * ajusteAltura * 1.5));

        playSprite = new Sprite(startGame);
        playSprite.setSize(startGame.getWidth() * ajusteLargura, startGame.getHeight() * ajusteAltura);
        playSprite.setPosition(((largura - differenceBetweenWidth) / 2) - (startGame.getWidth() * ajusteLargura / 2), ((altura) - (startGame.getHeight() * ajusteAltura * 3)));

        loginSprite = new Sprite(loginFb);
        loginSprite.setSize((loginFb.getWidth() / 2f) * ajusteLargura, loginFb.getHeight() * ajusteAltura);
        loginSprite.setPosition((loginFb.getWidth() * ajusteLargura) / 10f, (altura / 2f) - (ranking.getHeight() * ajusteAltura));

        rankingSprite = new Sprite(ranking);
        rankingSprite.setSize((ranking.getWidth()) * ajusteLargura, ranking.getHeight() * ajusteAltura);
        rankingSprite.setPosition((ranking.getWidth() * ajusteLargura) / 2.5f, (altura / 2f));

        termsSprite = new Sprite(terms);
        termsSprite.setSize((terms.getWidth() / 2f) * ajusteLargura, terms.getHeight() * ajusteAltura);
        termsSprite.setPosition((largura - differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * ajusteLargura), ((privacyPolicy.getHeight() / 1.5f) * ajusteAltura) + (privacyPolicy.getHeight() * ajusteAltura * 1.2f));

        privacyPolicySprite = new Sprite(privacyPolicy);
        privacyPolicySprite.setSize((privacyPolicy.getWidth() / 2f) * ajusteLargura, privacyPolicy.getHeight() * ajusteAltura);
        privacyPolicySprite.setPosition((largura - differenceBetweenWidth) - ((privacyPolicy.getWidth() / 2f) * ajusteLargura), ((privacyPolicy.getHeight() / 1.5f) * ajusteAltura));

        replaySprite = new Sprite(reload);
        replaySprite.setSize(150 * ajusteLargura, 150 * ajusteAltura);
        replaySprite.setPosition(((largura - differenceBetweenWidth) / 2) - (reload.getWidth() * ajusteLargura), altura / 2);

        pauseSprite = new Sprite(pause);
        pauseSprite.setSize(pause.getWidth() * ajusteLargura, pause.getHeight() * ajusteAltura);
        pauseSprite.setPosition(pause.getWidth(), (altura - ((float) pause.getHeight() * 1.5f)));

        musicSprite = new Sprite(music);
        musicSprite.setSize(music.getWidth() * ajusteLargura, music.getHeight() * ajusteAltura);
        musicSprite.setPosition(music.getWidth() * 3, (altura - ((float) pause.getHeight() * 1.5f)));

        nextSprite = new Sprite(next);
        nextSprite.setSize(next.getWidth() * ajusteLargura, next.getHeight() * ajusteAltura);
        nextSprite.setPosition(((largura - differenceBetweenWidth) / 2) + (next.getWidth() * ajusteLargura * 1.5f), ALTURA_SELECT_PEIXE + (next.getHeight() * ajusteAltura));

        backSprite = new Sprite(back);
        backSprite.setSize(back.getWidth() * ajusteLargura, back.getHeight() * ajusteAltura);
        backSprite.setPosition(((largura - differenceBetweenWidth) / 2) - (back.getWidth() * 1.5f * ajusteLargura), ALTURA_SELECT_PEIXE + (back.getHeight() * ajusteAltura));
    }

    private void setSounds() {
        somFundo = Gdx.audio.newMusic(Gdx.files.internal("audios/somfundo.mp3"));
        comeSound = Gdx.audio.newSound(Gdx.files.internal("audios/nhac.mp3"));
        bateSound = Gdx.audio.newSound(Gdx.files.internal("audios/colisao.mpeg"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audios/gameover.aac"));
        bolhaSound = Gdx.audio.newSound(Gdx.files.internal("audios/bubble.mp3"));
        estouraBolhaSound = Gdx.audio.newSound(Gdx.files.internal("audios/boombubble.mp3"));
        bolhaSound.setVolume(bolhaSound.play(), (float) 0.2);
        somFundo.setVolume((float) 0.1);
        somFundo.setLooping(true);
        comeSound.setVolume(comeSound.play(), (float) 1);
        bateSound.setVolume(bateSound.play(), (float) 0.5);
        estouraBolhaSound.setVolume(estouraBolhaSound.play(), (float) 1);
        somFundo.play();
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

    @Override
    public void userLoggedIn(String name) {
        changeLoginButton(name);
    }

    @Override
    public void userLoggedOut() {
        changeLoginButton(null);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        largura = width;
        altura = height;
        ajusteAltura = (float) (altura / alturaPadrao);
        ajusteLargura = (float) (largura / larguraPadrao);
        differenceBetweenWidth = largura - originalWidth;
        viewport.update(width, height, true);
        setBotoes();
        setAlgas();
    }
}