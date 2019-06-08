package com.xandi.seafish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Seafish extends ApplicationAdapter implements VideoEventListener {

    //Mantém dados salvos
    private Preferences prefs;

    //Controla anúncios
    private AdService handler;
    private GoogleServices googleServices;

    Seafish(AdService handler, GoogleServices googleServices) {
        this.handler = handler;
        this.googleServices = googleServices;
        this.googleServices.setVideoEventListener(this);
    }

    //batch
    private SpriteBatch batch;

    //Dimensões
    private float ajusteAltura, ajusteLargura, largura, altura;

    //Score
    private int metragem, velocidadeMetros;
    private BitmapFont metros;
    private float metrosScore;
    private Integer record;
    private BitmapFont recordLabel;

    private BitmapFont tap;

    //Metros enquanto tubarão
    private float contaMetrosTubarao;

    //Formas para sobrepor botoes
    private Sprite peixe, menuSprite, playSprite, replaySprite, pauseSprite, nextSprite, backSprite, simSprite, naoSprite, musicSprite;
    private Sprite[] algas;

    //imagens
    private Texture telaInicial, gameOverText, reload, pause, startGame, next, back, menuBotao, simBotao, naoBotao, continueText, videoIcon, music, bolhaInicio;
    private Texture[] fundo;
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

    //counters
    private int estado; //0=menu - 1=iniciado
    private int numMinhocas;
    private int contaPartidas = 1;
    private int contaFundo;
    private int contaMetrosSetor;
    private int obstaculoAtualSetor;
    private int toques;
    private int toquesParaSoltar;

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
    private Rectangle peixeRect, minhocaBonusRect;
    private Rectangle[] minhocasRect, piranhasVerticalRect, piranhasHorizontalRect, tubaroesRect;
    private Circle[] anzoisCircle, poluicoesCircle;

    //Numero do peixe
    private int i;

    //Rotação algas
    private boolean controlaRot;
    private boolean controlaRot2;

    //Sons
    private Sound comeSound, bateSound, gameOverSound, bolhaSound;
    private Music somFundo;

    //Variações
    private int variacaoPeixe;
    private float variacaoPeixeAux;
    private float variacaoTubarao;
    private float variacaoCardume;

    @Override
    public void create() {

        //Dimensões padrão
        double alturaPadrao = 1080;
        double larguraPadrao = 1920;

        fundo = new Texture[9];

        //Inicializa os Sprites
        peixes = new Sprite[3][5];
        tubaroes = new Sprite[3];
        cardumes = new Sprite[3];
        poluicoes = new Sprite[5];
        anzois = new Sprite[4];
        minhocasScore = new Sprite[10];
        minhocaBonus = new Sprite();
        bolha = new Sprite(new Texture("imagens/bolha.png"));
        bolhaInicio = new Texture("imagens/bolhainicio.png");

        //Distancia da minhoca do obstáculo
        distanciaMinhoca = new int[4];
        alturaMinhoca = new int[4];

        //Sair do anzol
        toques = 0;
        toquesParaSoltar = 0;

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
        poluicoesCircle = new Circle[4];

        //Numero do peixe
        i = 0;

        //Rotação algas
        controlaRot = false;
        controlaRot2 = false;

        //Variações dos elementos
        variacaoPeixe = 0;
        variacaoPeixeAux = 0;
        variacaoTubarao = 0;
        variacaoCardume = 0;

        contaMetrosTubarao = 0;
        alturaObstaculo = new float[4];

        prefs = Gdx.app.getPreferences("score");
        record = prefs.getInteger("score");

        batch = new SpriteBatch();

        setTextures();

        peixeCircle = new Circle();
        peixeRect = new Rectangle();
        minhocaBonusRect = new Rectangle();
        bolhaCircle = new Circle();

        largura = Gdx.graphics.getWidth();
        altura = Gdx.graphics.getHeight();

        ajusteAltura = (float) (altura / alturaPadrao);
        ajusteLargura = (float) (largura / larguraPadrao);


        metragem = 0;
        alturaObstaculoRandom = new Random();
        obstaculoRandom = new Random();

        setBotoes();

        setAlgas();

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

        numObstaculo = new int[4];
        if (!pausa) {
            for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                posicaoMovimentoObstaculoHorizontal[i] = -obstaculos[numObstaculo[i]].getWidth() * ajusteLargura;
            }
        }

        metros = new BitmapFont();
        recordLabel = new BitmapFont();
        recordLabel.setColor(Color.WHITE);
        recordLabel.getData().setScale(6 * ajusteLargura);
        tap = new BitmapFont();
        tap.setColor(Color.WHITE);
        tap.getData().setScale(5 * ajusteLargura);
        metros.setColor(Color.WHITE);
        metros.getData().setScale(6 * ajusteLargura);
        metrosScore = 0;
        velocidadeMetros = 100;

        distanciaMinhocaRandom = new Random();
        alturaMinhocaRandom = new Random();
        sorteioMetrosPorSetor = new Random();
        numeroPoluicao = new Random();

        for (int i = 0; i < piranhasHorizontalRect.length; i++) {
            piranhasVerticalRect[i] = new Rectangle();
            piranhasHorizontalRect[i] = new Rectangle();
            poluicoesCircle[i] = new Circle();
            tubaroesRect[i] = new Rectangle();
            minhocasRect[i] = new Rectangle();
            anzoisCircle[i] = new Circle();
        }

        for (int i = 0; i < minhocaColidiu.length; i++) {
            minhocaColidiu[i] = false;
            colidiuMinhoca[i] = false;
        }

        pauseSprite = new Sprite(pause);
        pauseSprite.setSize(100 * ajusteLargura, 150 * ajusteAltura);
        pauseSprite.setPosition(largura - (pause.getWidth() * ajusteLargura * 3), (float) (altura - (minhocasScore[0].getHeight() * ajusteAltura * 3.5)));

        setSounds();
    }

    @Override
    public void render() {
        super.render();

        //Limpa tela
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Varia o peixe
        variacaoPeixeAux += (float) 0.05;
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

            variaCardume();
            variaTubarao();

            //controla a velocidade do jogo
            if (iniciado) {
                if (velocidade < 33) {
                    velocidade += (float) 0.001;
                    Gdx.app.log("Velocidade:", " " + velocidade);
                    if (velocidade >= 33) {
                        Gdx.app.log("Distância:", " " + metrosScore);
                    }
                }
            }

            controlaRotacaoAlga(true);

            sorteiaProximoObstaculo();

            //faz o peixe subir a cada toque na tela
            if (Gdx.input.justTouched() && !pausa && !musicSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                bolhaSound.play(0.1f);
                iniciado = true;
                velocidadeQueda = -12 * (ajusteLargura);
                batch.begin();
                batch.draw(peixes[i][variacaoPeixe], largura / 12, posicaoInicialVertical, peixes[i][variacaoPeixe].getWidth() * ajusteLargura, peixes[i][variacaoPeixe].getHeight() * ajusteAltura);
                batch.end();
            }

            //Ajusta forma do peixe
            if (peixes[i][0].getWidth() * ajusteLargura > peixes[i][0].getHeight() * ajusteAltura * 1.5) {
                peixeRect.set((largura / 12), posicaoInicialVertical, peixes[i][0].getWidth() * ajusteLargura, peixes[i][0].getHeight() * ajusteAltura);
            } else {
                peixeCircle.set((largura / 12) + peixes[i][0].getWidth() * ajusteLargura / 2, (posicaoInicialVertical + peixes[i][0].getHeight() * ajusteAltura / 2), ((peixes[i][0].getHeight() * ajusteLargura / 2) - peixes[i][0].getHeight() * ajusteAltura / 12));
            }

            //impede que o peixe passe do topo
            if (posicaoInicialVertical >= altura - peixe.getHeight() * ajusteAltura) {
                velocidadeQueda = (float) 0.1;
            }

            //aumenta a velocidade de queda do peixe
            if (iniciado && !pausa) {
                velocidadeQueda += 0.7 * (ajusteLargura);
                //Movimenta o obstáculo
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    posicaoMovimentoObstaculoHorizontal[i] -= velocidade;
                }
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
        batch.draw(peixes[i][variacaoPeixe], largura / 12, posicaoInicialVertical, peixes[i][variacaoPeixe].getWidth() * ajusteLargura, peixes[i][variacaoPeixe].getHeight() * ajusteAltura);
        for (int i = 0; i < numObstaculo.length; i++) {
            batch.draw(obstaculos[numObstaculo[i]], posicaoMovimentoObstaculoHorizontal[i], alturaObstaculo[i], obstaculos[numObstaculo[i]].getWidth() * ajusteLargura, obstaculos[numObstaculo[i]].getHeight() * ajusteAltura);
        }
        batch.draw(pause, largura - (pause.getWidth() * ajusteLargura * 3), (float) (altura - (minhocasScore[0].getHeight() * ajusteAltura * 3.5)), 100 * ajusteLargura, 100 * ajusteAltura);
        batch.draw(music, largura - (music.getWidth() * ajusteLargura * 3 * 2), (float) (altura - (minhocasScore[0].getHeight() * ajusteAltura * 3.5)), 100 * ajusteLargura, 100 * ajusteAltura);

        musicSprite = new Sprite(music);
        musicSprite.setSize(100 * ajusteLargura, 100 * ajusteAltura);
        musicSprite.setPosition(largura - (music.getWidth() * ajusteLargura * 3 * 2), (float) (altura - (minhocasScore[0].getHeight() * ajusteAltura * 3.5)));

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
            mostraBolha = false;
            vidaExtra = true;
        }
        if (metrosScore % 650 == 0 && metrosScore != 0) {
            mostraMinhocaBonus = true;
            minhocaBonusHorizontal = largura;
        }
        if (minhocaBonusHorizontal < 0) {
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
            batch.draw(minhocasScore[i - 1], (float) (largura - (minhocasScore[i - 1].getWidth() * ajusteLargura * i * 1.5)), (float) (altura - minhocasScore[i - 1].getHeight() * ajusteAltura * 1.5), minhocasScore[i - 1].getWidth() * ajusteLargura, minhocasScore[i - 1].getHeight() * ajusteAltura);
        }
        if (vidaExtra) {
            batch.draw(bolha, (float) (largura - (minhocaBonus.getWidth() * ajusteLargura * (numMinhocas + 2) * 1.5)), (float) (altura - minhocaBonus.getHeight() * ajusteAltura * 1.3), bolha.getTexture().getWidth() * ajusteLargura, bolha.getTexture().getHeight() * ajusteAltura);
        }

        algas[0].draw(batch);
        algas[2].draw(batch);
        algas[3].draw(batch);

        metros.draw(batch, (int) metrosScore + "m", largura / 2 - (metros.getXHeight() / 2), altura - (altura / 10));
        recordLabel.draw(batch, "Record: " + record + "m", 15, altura - 15);
        if (gameOver) {
            batch.draw(gameOverText, (largura / 2) - (gameOverText.getWidth() * ajusteLargura / 2), (float) (altura - (gameOverText.getHeight() * ajusteAltura * 2.5)), gameOverText.getWidth() * ajusteLargura, gameOverText.getHeight() * ajusteAltura);
            batch.draw(reload, (largura / 2) - (reload.getWidth() * ajusteLargura), altura / 2, 150 * ajusteLargura, 150 * ajusteAltura);
            batch.draw(menuBotao, (largura / 2) - (menuBotao.getWidth() * ajusteLargura / 2), (float) ((altura / 3) - menuSprite.getHeight() * ajusteAltura * 1.5), menuBotao.getWidth() * ajusteLargura, menuBotao.getHeight() * ajusteAltura);
        }

        if (mostraTelaSegueJogo) {
            batch.draw(continueText, (largura / 2) - (continueText.getWidth() * ajusteLargura / 2), (float) (altura - (continueText.getHeight() * ajusteAltura * 2.5)), continueText.getWidth() * ajusteLargura, continueText.getHeight() * ajusteAltura);
            batch.draw(simBotao, (float) ((largura / 2) - (simBotao.getWidth() * ajusteLargura * 1.5)), (float) ((altura / 3) - simSprite.getHeight() * ajusteAltura * 1.5), simBotao.getWidth() * ajusteLargura, simBotao.getHeight() * ajusteAltura);
            batch.draw(naoBotao, ((largura / 2) + (naoBotao.getWidth() * ajusteLargura)), (float) ((altura / 3) - naoSprite.getHeight() * ajusteAltura * 1.5), naoBotao.getWidth() * ajusteLargura, naoBotao.getHeight() * ajusteAltura);
            batch.draw(videoIcon, (float) ((largura / 2) - (simBotao.getWidth() * ajusteLargura * 1.5) + videoIcon.getWidth() * ajusteLargura * 5), (float) ((altura / 3) - simSprite.getHeight() * ajusteAltura * 1.5), simBotao.getWidth() * ajusteLargura, simBotao.getHeight() * ajusteAltura);
        }

        if (voltando) {
            if (variacaoPeixe == 0) {
                tap.draw(batch, "tap!", (largura / 8) + peixe.getWidth(), altura - 120);
            }
        }

        batch.end();

        setObstaculos();

        //Botões game over
        if (Gdx.input.justTouched()) {
            if (gameOver) {
                if (metrosScore > record) {
                    prefs.putInteger("score", (int) metrosScore);
                    prefs.flush();
                }
                if (replaySprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    gameOver = false;
                    int peixe = i;
                    create();
                    estado = 1;
                    if (metrosScore < 300) {
                        contaPartidas++;
                    }
                    i = peixe;
                }
                if (menuSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
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
                    if (this.googleServices.hasVideoLoaded()) {
                        this.googleServices.showRewardedVideoAd();
                    } else {
                        gameOver();
                    }
                    mostraTelaSegueJogo = false;
                }
                if (naoSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    mostraTelaSegueJogo = false;
                    gameOver();
                }
            }
            if (pauseSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                iniciado = false;
            }
            if (musicSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (somFundo.isPlaying()) {
                    somFundo.pause();
                    music = new Texture("imagens/musicoff.png");
                } else {
                    somFundo.play();
                    music = new Texture("imagens/music.png");
                }
            }
        }
    }

    private void menuState() {
        handler.showBannerAd(true);
        //Se o botão iniciar jogo for clicado
        if (Gdx.input.justTouched()) {
            if (playSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                estado = 1;
                gameOver = false;
            }
        }

        batch.begin();
        batch.draw(telaInicial, 0, 0, largura, altura);
        batch.draw(startGame, (largura / 2) - (startGame.getWidth() * ajusteLargura / 2), ((altura) - (startGame.getHeight() * ajusteAltura * 3)), startGame.getWidth() * ajusteLargura, startGame.getHeight() * ajusteAltura);
        batch.draw(back, (largura / 2) - (back.getWidth() * ajusteLargura * 7), (float) ((altura) - ((startGame.getHeight() * ajusteLargura * 4.5) + back.getHeight() * ajusteAltura * 2)), 250 * ajusteLargura, 250 * ajusteLargura);
        batch.draw(next, (largura / 2) + (next.getWidth() * ajusteLargura / 2), (float) ((altura) - ((startGame.getHeight() * ajusteLargura * 4.5) + next.getHeight() * ajusteAltura * 2)), 250 * ajusteLargura, 250 * ajusteLargura);
        batch.draw(peixes[i][variacaoPeixe], (largura / 2) - (peixes[i][0].getWidth() * ajusteLargura / 2), (float) ((altura) - ((startGame.getHeight() * ajusteAltura * 4.5) - back.getHeight() * ajusteAltura)), peixes[i][0].getWidth() * ajusteLargura, peixes[i][0].getHeight() * ajusteAltura);

        //Draw algas
        for (Sprite alga : algas) {
            alga.draw(batch);
        }

        for (int i = 0; i < 10; i++) {
            if (movimentoBolhaHorizontal[i] >= largura - bolhaInicio.getWidth()) {
                bolhaTocouLado[i] = true;
            }

            if (movimentoBolhaHorizontal[i] <= 0) {
                bolhaTocouLado[i] = false;
            }

            if (bolhaTocouLado[i]) {
                movimentoBolhaHorizontal[i] -= Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(50) + 1;
            } else {
                movimentoBolhaHorizontal[i] += Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(200) + 1;
            }

            if (movimentoBolhaVertical[i] >= altura - bolhaInicio.getHeight()) {
                bolhaTocouTopo[i] = true;
            }

            if (movimentoBolhaVertical[i] <= 0) {
                bolhaTocouTopo[i] = false;
            }

            if (bolhaTocouTopo[i]) {
                movimentoBolhaVertical[i] -= Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(100) + 1;
            } else {
                movimentoBolhaVertical[i] += Gdx.graphics.getDeltaTime() * posicaoBolha.nextInt(150) + 1;
            }
            batch.draw(bolhaInicio, movimentoBolhaHorizontal[i], movimentoBolhaVertical[i], bolhaInicio.getWidth(), bolhaInicio.getHeight());
        }

        batch.end();

        controlaRotacaoAlga(false);

        selecionaPeixe();
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
            Gdx.app.log("Variação", " " + variacaoPeixe);
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
                    i = 2;
                } else {
                    i--;
                }
            }
        }
        if (Gdx.input.justTouched()) {
            if (nextSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if (i == 2) {
                    i = 0;
                } else {
                    i++;
                }
            }
        }
    }

    private void controlaRotacaoAlga(boolean mudaPosicao) {
        if (controlaRot) {
            if (algas[0].getRotation() <= -25 * (ajusteLargura)) {
                controlaRot = false;
            }
            algas[0].rotate((float) -0.3);
            algas[2].rotate((float) -0.3);
            algas[4].rotate((float) -0.3);
        } else {
            if (algas[0].getRotation() >= 25 * (ajusteLargura)) {
                controlaRot = true;
            }
            algas[0].rotate((float) 0.3);
            algas[2].rotate((float) 0.3);
            algas[4].rotate((float) 0.3);
        }

        if (controlaRot2) {
            if (algas[1].getRotation() <= -35 * (ajusteLargura)) {
                controlaRot2 = false;
            }
            algas[1].rotate((float) -0.4);
            algas[3].rotate((float) -0.4);
        } else {
            if (algas[1].getRotation() >= 20 * (ajusteLargura)) {
                controlaRot2 = true;
            }
            algas[1].rotate((float) 0.4);
            algas[3].rotate((float) 0.4);
        }
        if (mudaPosicao) {
            algas[0].setPosition(posicaoMovimentoObstaculoHorizontal[0], 0 - algas[0].getTexture().getHeight() * ajusteAltura / 5);
            algas[2].setPosition(posicaoMovimentoObstaculoHorizontal[2], 0 - algas[2].getTexture().getHeight() * ajusteAltura / 5);
            algas[3].setPosition(posicaoMovimentoObstaculoHorizontal[3], 0 - algas[3].getTexture().getHeight() * ajusteAltura / 5);
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
            numMinhocas = 1;
            velocidadeQueda = 0;
            pausa = true;
            if (circle == 1) {
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    if (i == 0) {
                        posicaoMovimentoObstaculoHorizontal[i] += 0;
                    } else {
                        posicaoMovimentoObstaculoHorizontal[i] += velocidade * 12;
                    }
                }
                if (alturaObstaculo[0] < altura - 150) {
                    alturaObstaculo[0] += 4;
                    posicaoInicialVertical += 4;
                }
            } else if (circle == 2) {
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    if (i == 1) {
                        posicaoMovimentoObstaculoHorizontal[i] += 0;
                    } else {
                        posicaoMovimentoObstaculoHorizontal[i] += velocidade * 12;
                    }
                }
                if (alturaObstaculo[1] < altura - 150) {
                    alturaObstaculo[1] += 4;
                    posicaoInicialVertical += 4;
                }
            } else if (circle == 3) {
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    if (i == 2) {
                        posicaoMovimentoObstaculoHorizontal[i] += 0;
                    } else {
                        posicaoMovimentoObstaculoHorizontal[i] += velocidade * 12;
                    }
                }
                if (alturaObstaculo[2] < altura - 150) {
                    alturaObstaculo[2] += 4;
                    posicaoInicialVertical += 4;
                }
            } else if (circle == 4) {
                for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                    if (i == 3) {
                        posicaoMovimentoObstaculoHorizontal[i] += 0;
                    } else {
                        posicaoMovimentoObstaculoHorizontal[i] += velocidade * 12;
                    }
                }
                if (alturaObstaculo[3] < altura - 150) {
                    alturaObstaculo[3] += 4;
                    posicaoInicialVertical += 4;
                }
            }

            if (metrosScore > 0) {
                sorteiaObstaculoAnterior();
            } else {
                gameOver = false;
                colide = true;
                posicaoInicialVertical = posicaoInicialVertical - 30;
                velocidade = 10 * ajusteLargura;
                velocidadeMetros = 100;
            }

            if (Gdx.input.justTouched()) {
                switch ((int) circle - 1) {
                    case 0:
                        toquesParaSoltar = 50;
                        break;
                    case 1:
                        toquesParaSoltar = 85;
                        break;
                    case 2:
                        toquesParaSoltar = 115;
                        break;
                    case 3:
                        toquesParaSoltar = 150;
                        break;
                    default:
                        toquesParaSoltar = 50;
                }
                toques++;
                Gdx.app.log("Toques:", " "+toques);
                if (toques >= toquesParaSoltar) {
                    posicaoInicialVertical -= 30;
                    posicaoMovimentoObstaculoHorizontal[0] = largura;
                    posicaoMovimentoObstaculoHorizontal[1] = (float) (largura * 1.5);
                    posicaoMovimentoObstaculoHorizontal[2] = largura * 2;
                    posicaoMovimentoObstaculoHorizontal[3] = (float) (largura * 2.5);
                    gameOver = false;
                    colide = true;
                    posicaoInicialVertical = altura / 2;
                    toques = 0;

                    if (metrosScore >= 4000) {
                        velocidade = 33;
                        velocidadeMetros = 10;
                    } else if (metrosScore >= 3000) {
                        velocidade = 30;
                        velocidadeMetros = 25;
                    } else if (metrosScore >= 25000) {
                        velocidade = 27;
                        velocidadeMetros = 36;
                    } else if (metrosScore >= 2000) {
                        velocidade = 24;
                        velocidadeMetros = 47;
                    } else if (metrosScore >= 1500) {
                        velocidade = 21;
                        velocidadeMetros = 58;
                    } else if (metrosScore >= 1000) {
                        velocidade = 18;
                        velocidadeMetros = 71;
                    } else if (metrosScore >= 500) {
                        velocidade = 15;
                        velocidadeMetros = 82;
                    } else if (metrosScore >= 250) {
                        velocidade = 12;
                        velocidadeMetros = 90;
                    } else if (metrosScore <= 5) {
                        velocidade = 10 * ajusteLargura;
                        velocidadeMetros = 100;
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
        if (contaFundo <= 7) {
            contaFundo++;
        } else {
            contaFundo = 0;
        }
    }

    private void aumentaVelocidadeJogo() {
        contaMetrosTubarao += 0.5;
        if (contaMetrosTubarao <= 100) {
            this.metrosScore += 0.5;
            for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {
                posicaoMovimentoObstaculoHorizontal[i] -= velocidade * 10;
            }
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
                Intersector.overlaps(peixeCircle, tubaroesRect[2]) || Intersector.overlaps(peixeCircle, tubaroesRect[3]) ||
                Intersector.overlaps(peixeCircle, poluicoesCircle[0]) || Intersector.overlaps(peixeCircle, poluicoesCircle[1]) ||
                Intersector.overlaps(peixeCircle, poluicoesCircle[2]) || Intersector.overlaps(peixeCircle, poluicoesCircle[3])) {

            isColiding = true;

            if (!colidiuObstaculo) {
                bateSound.play(1f);
                Gdx.input.vibrate(50);
                numMinhocas -= 3;
                colidiuObstaculo = true;
                if (numMinhocas < 0) {
                    if (vidaExtra) {
                        vidaExtra = false;
                        mostraBolha = true;
                    } else {
                        if (!isRewardedYet && mostraVideoPremiado()) {
                            iniciado = false;
                            pausa = true;
                            mostraTelaSegueJogo = true;
                        } else {
                            gameOver();
                            if (contaPartidas == 2 || metrosScore >= 300) {
                                handler.showinterstitialAd(new Runnable() {
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
        } else {
            colidiuObstaculo = false;
            isColiding = false;
        }
        if (isColiding) {
            variacaoPeixe = 2;
        }
    }

    private void gameOver() {
        somFundo.dispose();
        gameOver = true;
        gameOverSound.play();
    }

    private boolean mostraVideoPremiado() {
        return record >= 70 && metrosScore >= record - (record / 3) && metrosScore <= record;
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
            anzoisCircle[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2, alturaObstaculo[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2));
        }

        if (numObstaculo[1] == 0) {
            piranhasVerticalRect[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 3, alturaObstaculo[1], (obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[1]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[1].set(posicaoMovimentoObstaculoHorizontal[1], alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[1]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 3));
        } else if (numObstaculo[1] == 1) {
            tubaroesRect[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 2.5));
        } else if (numObstaculo[1] == 2 || numObstaculo[1] == 3 || numObstaculo[1] == 4 || numObstaculo[1] == 5 || numObstaculo[1] == 6) {
            poluicoesCircle[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2, alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2);
        } else if (numObstaculo[1] == 7 || numObstaculo[1] == 8 || numObstaculo[1] == 9 || numObstaculo[1] == 10) {
            anzoisCircle[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2, alturaObstaculo[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2));
        }

        if (numObstaculo[2] == 0) {
            piranhasVerticalRect[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 3, alturaObstaculo[2], (obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[2]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[2].set(posicaoMovimentoObstaculoHorizontal[2], alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[2]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 3));
        } else if (numObstaculo[2] == 1) {
            tubaroesRect[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 2.5));
        } else if (numObstaculo[2] == 2 || numObstaculo[2] == 3 || numObstaculo[2] == 4 || numObstaculo[2] == 5 || numObstaculo[2] == 6) {
            poluicoesCircle[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2, alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2);
        } else if (numObstaculo[2] == 7 || numObstaculo[2] == 8 || numObstaculo[2] == 9 || numObstaculo[2] == 10) {
            anzoisCircle[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2, alturaObstaculo[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2));
        }

        if (numObstaculo[3] == 0) {
            piranhasVerticalRect[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 3, alturaObstaculo[3], (obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[3]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[3].set(posicaoMovimentoObstaculoHorizontal[3], alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[3]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 3));
        } else if (numObstaculo[3] == 1) {
            tubaroesRect[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 2.5));
        } else if (numObstaculo[3] == 2 || numObstaculo[3] == 3 || numObstaculo[3] == 4 || numObstaculo[3] == 5 || numObstaculo[3] == 6) {
            poluicoesCircle[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2, alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2);
        } else if (numObstaculo[3] == 7 || numObstaculo[3] == 8 || numObstaculo[3] == 9 || numObstaculo[3] == 10) {
            anzoisCircle[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2, alturaObstaculo[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2));
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
                } else {
                    alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (altura - obstaculos[numObstaculo[i]].getHeight() * ajusteAltura / 1.5));
                }

                posicaoMovimentoObstaculoHorizontal[i] = (largura * multLargura);
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
        peixes[0][0] = new Sprite(new Texture("imagens/peixe1.png"));
        peixes[0][1] = new Sprite(new Texture("imagens/peixe12.png"));
        peixes[0][2] = new Sprite(new Texture("imagens/peixe1red.png"));
        peixes[0][3] = new Sprite(new Texture("imagens/tubarao1.png"));
        peixes[0][4] = new Sprite(new Texture("imagens/tubarao12.png"));
        peixes[1][0] = new Sprite(new Texture("imagens/peixe2.png"));
        peixes[1][1] = new Sprite(new Texture("imagens/peixe22.png"));
        peixes[1][2] = new Sprite(new Texture("imagens/peixe2red.png"));
        peixes[1][3] = new Sprite(new Texture("imagens/tubarao2.png"));
        peixes[1][4] = new Sprite(new Texture("imagens/tubarao22.png"));
        peixes[2][0] = new Sprite(new Texture("imagens/peixe3.png"));
        peixes[2][1] = new Sprite(new Texture("imagens/peixe32.png"));
        peixes[2][2] = new Sprite(new Texture("imagens/peixe3red.png"));
        peixes[2][3] = new Sprite(new Texture("imagens/tubarao3.png"));
        peixes[2][4] = new Sprite(new Texture("imagens/tubarao32.png"));
    }

    private void setTextures() {
        //Texturas
        peixe = new Sprite(new Texture("imagens/peixe1.png"));
        fundo[0] = new Texture("imagens/fundo1.png");
        fundo[1] = new Texture("imagens/fundo2.png");
        fundo[2] = new Texture("imagens/fundo3.png");
        fundo[3] = new Texture("imagens/fundo4.png");
        fundo[4] = new Texture("imagens/fundo5.png");
        fundo[5] = new Texture("imagens/fundo6.png");
        fundo[6] = new Texture("imagens/fundo7.png");
        fundo[7] = new Texture("imagens/fundo8.png");
        fundo[8] = new Texture("imagens/fundo9.png");
        telaInicial = new Texture("imagens/telainicio.png");
        gameOverText = new Texture("imagens/gameover.png");
        continueText = new Texture("imagens/continue.png");
        reload = new Texture("imagens/refresh.png");
        pause = new Texture("imagens/pause.png");
        music = new Texture("imagens/music.png");
        menuBotao = new Texture("imagens/menu.png");
        simBotao = new Texture("imagens/yes.png");
        videoIcon = new Texture("imagens/video.png");
        naoBotao = new Texture("imagens/no.png");
        startGame = new Texture("imagens/startgame.png");
        next = new Texture("imagens/next.png");
        back = new Texture("imagens/back.png");
        setPeixes();
        cardumes[0] = new Sprite(new Texture("imagens/piranhas.png"));
        cardumes[1] = new Sprite(new Texture("imagens/piranhas2.png"));
        tubaroes[0] = new Sprite(new Texture("imagens/tubarao.png"));
        tubaroes[1] = new Sprite(new Texture("imagens/tubaraoinimigo2.png"));
        poluicoes[0] = new Sprite(new Texture("imagens/aguasuja.png"));
        poluicoes[1] = new Sprite(new Texture("imagens/canudo.png"));
        poluicoes[2] = new Sprite(new Texture("imagens/garrafa.png"));
        poluicoes[3] = new Sprite(new Texture("imagens/pneu.png"));
        poluicoes[4] = new Sprite(new Texture("imagens/plastico.png"));
        anzois[0] = new Sprite(new Texture("imagens/anzol1.png"));
        anzois[1] = new Sprite(new Texture("imagens/anzol2.png"));
        anzois[2] = new Sprite(new Texture("imagens/anzol3.png"));
        anzois[3] = new Sprite(new Texture("imagens/anzol4.png"));
        for (int i = 0; i < minhocasScore.length; i++) {
            minhocasScore[i] = new Sprite(new Texture("imagens/minhoca.png"));
        }
        minhocaBonus = new Sprite(new Texture("imagens/minhocabonus.png"));
    }

    private void setAlgas() {
        //set Algas
        algas = new Sprite[5];
        algas[0] = new Sprite(new Texture("imagens/alga1.png"));
        algas[0].setPosition(largura / 2, -algas[0].getHeight() * ajusteAltura / 5);
        algas[0].setSize(algas[0].getWidth() * ajusteLargura, algas[0].getHeight() * ajusteAltura);
        algas[1] = new Sprite(new Texture("imagens/alga2.png"));
        algas[1].setPosition(largura / 3, -algas[1].getHeight() * ajusteAltura / 5);
        algas[1].setSize(algas[1].getWidth() * ajusteLargura, algas[1].getHeight() * ajusteAltura);
        algas[2] = new Sprite(new Texture("imagens/alga1.png"));
        algas[2].setPosition(largura / 10, -algas[2].getHeight() * ajusteAltura / 5);
        algas[2].setSize(algas[2].getWidth() * ajusteLargura, algas[2].getHeight() * ajusteAltura);
        algas[3] = new Sprite(new Texture("imagens/alga2.png"));
        algas[3].setPosition(largura - (algas[3].getTexture().getWidth() * ajusteLargura * 2), -algas[3].getHeight() * ajusteAltura / 5);
        algas[3].setSize(algas[3].getWidth() * ajusteLargura, algas[3].getHeight() * ajusteAltura);
        algas[4] = new Sprite(new Texture("imagens/alga1.png"));
        algas[4].setPosition((float) (largura / 4.5), -algas[0].getHeight() * ajusteAltura / 5);
        algas[4].setSize(algas[4].getWidth() * ajusteLargura, algas[4].getHeight() * ajusteAltura);
    }

    private void setBotoes() {
        //Sobrepor Botões
        menuSprite = new Sprite(menuBotao);
        menuSprite.setSize(menuSprite.getWidth() * ajusteLargura, menuSprite.getHeight() * ajusteAltura);
        menuSprite.setPosition((largura / 2) - (menuBotao.getWidth() * ajusteLargura / 2), (float) ((altura / 3) - menuSprite.getHeight() * ajusteAltura * 1.5));

        simSprite = new Sprite(simBotao);
        simSprite.setSize(simSprite.getWidth() * ajusteLargura, simSprite.getHeight() * ajusteAltura);
        simSprite.setPosition((float) ((largura / 2) - (simBotao.getWidth() * ajusteLargura * 1.5)), (float) ((altura / 3) - simSprite.getHeight() * ajusteAltura * 1.5));

        naoSprite = new Sprite(naoBotao);
        naoSprite.setSize(naoSprite.getWidth() * ajusteLargura, naoSprite.getHeight() * ajusteAltura);
        naoSprite.setPosition(((largura / 2) + (naoBotao.getWidth() * ajusteLargura)), (float) ((altura / 3) - naoSprite.getHeight() * ajusteAltura * 1.5));

        playSprite = new Sprite(startGame);
        playSprite.setSize(startGame.getWidth() * ajusteLargura, startGame.getHeight() * ajusteAltura);   // set size
        playSprite.setPosition((largura / 2) - (startGame.getWidth() * ajusteLargura / 2), ((altura) - (startGame.getHeight() * ajusteAltura * 3)));

        replaySprite = new Sprite(reload);
        replaySprite.setSize(150 * ajusteLargura, 150 * ajusteAltura);
        replaySprite.setPosition((largura / 2) - (reload.getWidth() * ajusteLargura), altura / 2);

        nextSprite = new Sprite(next);
        nextSprite.setSize(250 * ajusteLargura, 250 * ajusteLargura);
        nextSprite.setPosition((largura / 2) + (next.getWidth() * ajusteLargura / 2), (float) ((altura) - ((startGame.getHeight() * ajusteLargura * 4.5) + next.getHeight() * ajusteAltura * 2)));

        backSprite = new Sprite(back);
        backSprite.setSize(250 * ajusteLargura, 250 * ajusteLargura);
        backSprite.setPosition((largura / 2) - (back.getWidth() * ajusteLargura * 7), (float) ((altura) - ((startGame.getHeight() * ajusteLargura * 4.5) + back.getHeight() * ajusteAltura * 2)));
    }

    private void setSounds() {
        somFundo = Gdx.audio.newMusic(Gdx.files.internal("audios/somfundo.mp3"));
        comeSound = Gdx.audio.newSound(Gdx.files.internal("audios/nhac.mp3"));
        bateSound = Gdx.audio.newSound(Gdx.files.internal("audios/colisao.mpeg"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audios/gameover.aac"));
        bolhaSound = Gdx.audio.newSound(Gdx.files.internal("audios/bubble.mp3"));
        bolhaSound.setVolume(bolhaSound.play(), (float) 0.2);
        somFundo.setVolume((float) 0.1);
        somFundo.setLooping(true);
        comeSound.setVolume(comeSound.play(), (float) 1);
        bateSound.setVolume(bateSound.play(), (float) 0.5);
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
}