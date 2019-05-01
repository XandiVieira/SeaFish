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

    //Metros enquanto tubarão
    private float contaMetrosTubarao;

    //Formas para sobrepor botoes
    private Sprite peixe, menuSprite, playSprite, replaySprite, pauseSprite, nextSprite, backSprite, simSprite, naoSprite;
    private Sprite[] algas;
    //imagens
    private Texture telaInicial, gameOverText, reload, pause, startGame, next, back, menuBotao, simBotao, naoBotao, continueText, videoIcon;
    private Texture[] fundo;
    private Sprite[][] peixes;
    private Sprite[] tubaroes;
    private Sprite[] cardumes;
    private Sprite[] minhocasScore;
    private Sprite[] obstaculos;

    private float[] posicaoMovimentoObstaculoHorizontal;
    private int[] distanciaMinhoca, alturaMinhoca;
    private float velocidade;
    private float velocidadeQueda;
    private float posicaoInicialVertical;

    private int estado; //0=menu - 1=iniciado
    private int numMinhocas;
    private int contaPartidas = 1;
    private int contaFundo;
    private boolean colide;
    private boolean gameOver;
    //Começar os movimentos depois do primeiro toque
    private boolean iniciado;
    private boolean colidiuObstaculo;
    private boolean isColiding;
    private boolean[] colidiuMinhoca;
    private boolean pausa;
    private boolean isSlowShark;
    private boolean isShark;
    private boolean mostraTelaSegueJogo;
    private boolean isRewarded;
    private boolean isRewardedYet;

    private boolean[] minhocaColidiu;

    //Sorteios
    private Random obstaculoRandom;
    private int[] numObstaculo;
    private Random alturaObstaculoRandom;
    private float alturaObstaculo[];
    private Random distanciaMinhocaRandom, alturaMinhocaRandom;

    //Formas para colisões
    private Circle peixeCircle;
    private Rectangle peixeRect;
    private Rectangle[] minhocasRect, piranhasVerticalRect, piranhasHorizontalRect, tubaroesRect;
    private Circle[] anzoisCircle, aguasSujasCirc;
    //Numero do peixe
    private int i;
    private boolean controlaRot;
    private boolean controlaRot2;
    private int toquesParaSoltar;

    private Sound comeSound, bateSound, gameOverSound, bolhaSound;
    private Music somFundo;

    private int variacaoPeixe;
    private float variacaoPeixeAux;
    private float variacaoTubarao;
    private float variacaoCardume;

    @Override
    public void create() {

        //Dimensões padrão
        double alturaPadrao = 1080;
        double larguraPadrao = 1920;

        fundo = new Texture[3];

        //Inicializa os Sprites
        peixes = new Sprite[3][5];
        tubaroes = new Sprite[3];
        cardumes = new Sprite[3];
        Sprite[] aguas = new Sprite[1];
        Sprite[] anzois = new Sprite[1];
        minhocasScore = new Sprite[10];

        //Distancia da minhoca do obstáculo
        distanciaMinhoca = new int[4];
        alturaMinhoca = new int[4];

        //Sair do anzol
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
        mostraTelaSegueJogo = false;
        isRewarded = false;
        isRewardedYet = false;

        numMinhocas = 1;
        contaFundo = 0;

        //Inicializa formas
        minhocaColidiu = new boolean[4];
        colidiuMinhoca = new boolean[4];
        minhocasRect = new Rectangle[4];
        piranhasVerticalRect = new Rectangle[4];
        piranhasHorizontalRect = new Rectangle[4];
        tubaroesRect = new Rectangle[4];
        anzoisCircle = new Circle[4];
        aguasSujasCirc = new Circle[4];

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
        //Texturas
        peixe = new Sprite(new Texture("imagens/peixe1.png"));
        fundo[0] = new Texture("imagens/fundo1.png");
        fundo[1] = new Texture("imagens/fundo2.png");
        fundo[2] = new Texture("imagens/fundo3.png");
        telaInicial = new Texture("imagens/telainicio.png");
        gameOverText = new Texture("imagens/gameover.png");
        continueText = new Texture("imagens/continue.png");
        reload = new Texture("imagens/refresh.png");
        pause = new Texture("imagens/pause.png");
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
        aguas[0] = new Sprite(new Texture("imagens/aguasuja.png"));
        anzois[0] = new Sprite(new Texture("imagens/anzol.png"));
        for (int i = 0; i < minhocasScore.length; i++) {
            minhocasScore[i] = new Sprite(new Texture("imagens/minhoca.png"));
        }

        peixeCircle = new Circle();
        peixeRect = new Rectangle();

        largura = Gdx.graphics.getWidth();
        altura = Gdx.graphics.getHeight();

        ajusteAltura = (float) (altura / alturaPadrao);
        ajusteLargura = (float) (largura / larguraPadrao);


        metragem = 0;
        alturaObstaculoRandom = new Random();
        obstaculoRandom = new Random();

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

        obstaculos = new Sprite[4];

        velocidade = 10 * ajusteLargura;
        for (int i = 0; i < alturaObstaculo.length; i++) {
            if (i == 0) {
                alturaObstaculo[i] = i;
            } else {
                alturaObstaculo[i] = altura / i;
            }
        }

        posicaoInicialVertical = altura / 2;

        obstaculos[0] = cardumes[0];
        obstaculos[1] = tubaroes[0];
        obstaculos[2] = aguas[0];
        obstaculos[3] = anzois[0];

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
        metros.setColor(Color.WHITE);
        metros.getData().setScale(6 * ajusteLargura);
        metrosScore = 0;
        velocidadeMetros = 100;

        distanciaMinhocaRandom = new Random();
        alturaMinhocaRandom = new Random();

        for (int i = 0; i < piranhasHorizontalRect.length; i++) {
            piranhasVerticalRect[i] = new Rectangle();
            piranhasHorizontalRect[i] = new Rectangle();
            aguasSujasCirc[i] = new Circle();
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
        pauseSprite.setPosition(largura-(pause.getWidth()*ajusteLargura*3), (float) (altura - (minhocasScore[0].getHeight() * ajusteAltura * 3.5)));

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
    public void render() {
        super.render();

        //Limpa tela
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Varia o peixe
        variacaoPeixeAux += (float) 0.05;
        variaPeixe();

        if (estado == 0) {

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

            batch.end();

            controlaRotacaoAlga(false);

            selecionaPeixe();

            //Tela de jogo
        } else if (estado == 1) {
            handler.showBannerAd(false);
            //Impede que peixe vá pra baixo do chão (bug)
            if (posicaoInicialVertical < 10) {
                posicaoInicialVertical = altura / 2;
            }

            if (!gameOver) {
                variacaoTubarao += 0.01;
                variacaoCardume += 0.05;


                variaCardume();
                variaTubarao();

                //controla a velocidade de queda do peixe
                if (iniciado) {
                    if (velocidade < 33) {
                        velocidade += (float) 0.1 / 100;
                    }
                }

                controlaRotacaoAlga(true);

                sorteiaProximoObstaculo();

                //faz o peixe subir a cada toque na tela
                if (Gdx.input.justTouched() && !pausa) {
                    if(!somFundo.isPlaying()){
                        somFundo.play();
                    }
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
                    //shapeRenderer.rect((largura / 12), posicaoInicialVertical, peixes[i][0].getWidth()*ajusteLargura, peixes[i][0].getHeight()*ajusteAltura);
                } else {
                    peixeCircle.set((largura / 12) + peixes[i][0].getWidth() * ajusteLargura / 2, (posicaoInicialVertical + peixes[i][0].getHeight() * ajusteAltura / 2), ((peixes[i][0].getHeight() * ajusteLargura / 2) - peixes[i][0].getHeight() * ajusteAltura / 12));
                    //shapeRenderer.circle((largura / 12), posicaoInicialVertical, peixes[i][0].getHeight()*ajusteLargura/2);
                }
                /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                if (peixes[i][0].getWidth()*ajusteLargura > peixes[i][0].getHeight()*ajusteAltura * 1.5) {
                    shapeRenderer.rect((largura / 12), posicaoInicialVertical, peixes[i][0].getWidth()*ajusteLargura, peixes[i][0].getHeight()*ajusteAltura);
                } else {
                    shapeRenderer.circle((largura / 12) + peixes[i][0].getWidth()*ajusteLargura / 2, (posicaoInicialVertical + peixes[i][0].getHeight()*ajusteAltura / 2), ((peixes[i][0].getHeight()*ajusteLargura / 2) - peixes[i][0].getHeight()*ajusteAltura / 12));
                }
                shapeRenderer.end();*/

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
            batch.draw(pause, largura-(pause.getWidth()*ajusteLargura*3), (float) (altura - (minhocasScore[0].getHeight() * ajusteAltura * 3.5)), 100 * ajusteLargura, 100 * ajusteAltura);
            for (int i = 0; i < obstaculos.length; i++) {
                batch.draw(obstaculos[numObstaculo[i]], posicaoMovimentoObstaculoHorizontal[i], alturaObstaculo[i], obstaculos[numObstaculo[i]].getWidth() * ajusteLargura, obstaculos[numObstaculo[i]].getHeight() * ajusteAltura);
            }

            //Set retangulos colisoes minhocas
            for (int i = 0; i < minhocasRect.length; i++) {
                if (!isSlowShark && alturaMinhoca[i]>altura/13) {
                    minhocasRect[i].set(posicaoMovimentoObstaculoHorizontal[i] - distanciaMinhoca[i], alturaMinhoca[i], minhocasScore[i].getWidth() * ajusteLargura, minhocasScore[i].getHeight() * ajusteAltura);

                    if (!minhocaColidiu[i] && !pausa) {
                        batch.draw(minhocasScore[i], posicaoMovimentoObstaculoHorizontal[i] - distanciaMinhoca[i], alturaMinhoca[i], minhocasScore[i].getWidth() * ajusteLargura, minhocasScore[i].getHeight() * ajusteAltura);
                    }
                }
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

            batch.end();

            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle((largura / 12) + peixes[i][0].getWidth()*ajusteLargura / 2, (posicaoInicialVertical + peixes[i][0].getHeight()*ajusteAltura / 2), ((peixes[i][0].getHeight()*ajusteLargura / 2) - peixes[i][0].getHeight()*ajusteAltura / 12));
            shapeRenderer.rect((largura / 12), posicaoInicialVertical, peixes[i][0].getWidth()*ajusteLargura, peixes[i][0].getHeight()*ajusteAltura);
            shapeRenderer.end();*/
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
                        contaPartidas++;
                        i = peixe;
                    }
                    if (menuSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                        create();
                        contaPartidas++;
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
                if(pauseSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
                    somFundo.pause();
                    iniciado = false;
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

    private void variaPeixe() {
        if(!isShark){
            if (variacaoPeixeAux > 2) {
                variacaoPeixeAux = 0;
            } else if (variacaoPeixeAux >= 1) {
                variacaoPeixe = 0;
            } else {
                variacaoPeixe = 1;
            }
        }else if (isSlowShark){
            Gdx.app.log("Variação", " "+variacaoPeixe);
            if (variacaoPeixeAux > 2) {
                variacaoPeixeAux = 0;
            } else if (variacaoPeixeAux >= 1) {
                variacaoPeixe = 1;
            } else {
                variacaoPeixe = 3;
            }
        }else if(isShark){
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
            if (!mostraTelaSegueJogo) {
                pausa = false;
            }
        }
    }

    private void voltaProInicio(float circle) {
        if (Gdx.input.justTouched()) {
            iniciado = true;
        }
        if(iniciado) {
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
                toquesParaSoltar++;
                if (toquesParaSoltar >= metrosScore / 8) {
                    posicaoInicialVertical -= 30;
                    posicaoMovimentoObstaculoHorizontal[0] = largura;
                    posicaoMovimentoObstaculoHorizontal[1] = (float) (largura * 1.5);
                    posicaoMovimentoObstaculoHorizontal[2] = largura * 2;
                    posicaoMovimentoObstaculoHorizontal[3] = (float) (largura * 2.5);
                    gameOver = false;
                    colide = true;
                    posicaoInicialVertical = altura / 2;
                    toquesParaSoltar = 0;

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
                    } else if(metrosScore >= 250) {
                        velocidade = 12;
                        velocidadeMetros = 90;
                    }else if(metrosScore <= 5){
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
                if(!isSlowShark) {
                    numMinhocas++;
                    comeSound.play(0.9f);
                    colidiuMinhoca[numMinhoca] = true;
                }
            } else if(!isShark) {
                numMinhocas = 1;
                colide = false;
                mudaFundo();
            }
        }
    }

    private void mudaFundo() {
        if(contaFundo<=1){
            contaFundo++;
        }else{
            contaFundo=0;
        }
    }

    private void aumentaVelocidadeJogo() {
        //sharkMusic.play();
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
            isShark  = false;
        }
    }

    private void testaColisao() {
        if (Intersector.overlaps(peixeCircle, piranhasHorizontalRect[0]) || Intersector.overlaps(peixeCircle, piranhasHorizontalRect[1]) ||
                Intersector.overlaps(peixeCircle, piranhasHorizontalRect[2]) || Intersector.overlaps(peixeCircle, piranhasHorizontalRect[3]) ||
                Intersector.overlaps(peixeCircle, piranhasVerticalRect[0]) || Intersector.overlaps(peixeCircle, piranhasVerticalRect[1]) ||
                Intersector.overlaps(peixeCircle, piranhasVerticalRect[2]) || Intersector.overlaps(peixeCircle, piranhasVerticalRect[3]) ||
                Intersector.overlaps(peixeCircle, tubaroesRect[0]) || Intersector.overlaps(peixeCircle, tubaroesRect[1]) ||
                Intersector.overlaps(peixeCircle, tubaroesRect[2]) || Intersector.overlaps(peixeCircle, tubaroesRect[3]) ||
                Intersector.overlaps(peixeCircle, aguasSujasCirc[0]) || Intersector.overlaps(peixeCircle, aguasSujasCirc[1]) ||
                Intersector.overlaps(peixeCircle, aguasSujasCirc[2]) || Intersector.overlaps(peixeCircle, aguasSujasCirc[3])) {

            isColiding = true;

            if (!colidiuObstaculo) {
                bateSound.play(1f);
                numMinhocas -= 3;
                colidiuObstaculo = true;
                if (numMinhocas < 0) {
                    if (!isRewardedYet && mostraVideoPremiado()) {
                        iniciado = false;
                        pausa = true;
                        mostraTelaSegueJogo = true;
                    } else {
                        gameOver();
                        if (contaPartidas == 2) {
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
        } else {
            colidiuObstaculo = false;
            isColiding = false;
        }
        if(isColiding){
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
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth()*ajusteLargura / 3, alturaObstaculo[0], (obstaculos[numObstaculo[0]].getWidth()*ajusteLargura / 3), obstaculos[numObstaculo[0]].getHeight()*ajusteAltura);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[0], alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight()*ajusteAltura / 3, obstaculos[numObstaculo[0]].getWidth()* ajusteLargura, (obstaculos[numObstaculo[0]].getHeight()*ajusteAltura / 3));
            shapeRenderer.end();*/
        } else if (numObstaculo[0] == 1) {
            tubaroesRect[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[0]].getHeight() * ajusteAltura / 2.5));
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth()*ajusteLargura / 9, (float) (alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight()*ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[0]].getWidth()* ajusteLargura/1.5), (float) (obstaculos[numObstaculo[0]].getHeight()*ajusteAltura / 2.5));
            shapeRenderer.end();*/
        } else if (numObstaculo[0] == 2) {
            aguasSujasCirc[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2, alturaObstaculo[0] + obstaculos[numObstaculo[0]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2);
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(posicaoMovimentoObstaculoHorizontal[0]+ obstaculos[numObstaculo[0]].getWidth()*ajusteLargura/2, alturaObstaculo[0]+ obstaculos[numObstaculo[0]].getHeight()*ajusteAltura/2, obstaculos[numObstaculo[0]].getWidth()*ajusteLargura/2);
            shapeRenderer.end();*/
        } else if (numObstaculo[0] == 3) {
            anzoisCircle[0].set(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2, alturaObstaculo[0] + obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[0]].getWidth() * ajusteLargura / 2));
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(posicaoMovimentoObstaculoHorizontal[0] + obstaculos[numObstaculo[0]].getWidth()*ajusteLargura / 2, alturaObstaculo[0] + obstaculos[numObstaculo[0]].getWidth()*ajusteLargura / 2, (obstaculos[numObstaculo[0]].getWidth()*ajusteLargura / 2));
            shapeRenderer.end();*/
        }

        if (numObstaculo[1] == 0) {
            piranhasVerticalRect[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 3, alturaObstaculo[1], (obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[1]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[1].set(posicaoMovimentoObstaculoHorizontal[1], alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[1]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 3));
           /* shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth()*ajusteLargura / 3, alturaObstaculo[1], (obstaculos[numObstaculo[1]].getWidth()*ajusteLargura / 3), obstaculos[numObstaculo[1]].getHeight()*ajusteAltura);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[1], alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight()*ajusteAltura / 3, obstaculos[numObstaculo[1]].getWidth()*ajusteLargura, (obstaculos[numObstaculo[1]].getHeight()*ajusteAltura / 3));
            shapeRenderer.end();*/
        } else if (numObstaculo[1] == 1) {
            tubaroesRect[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 2.5));
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth()*ajusteLargura / 9, (float) (alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight()*ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[1]].getWidth()*ajusteLargura/1.5), (float) (obstaculos[numObstaculo[1]].getHeight()*ajusteAltura / 2.5));
            shapeRenderer.end();*/
        } else if (numObstaculo[1] == 2) {
            aguasSujasCirc[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2, alturaObstaculo[1] + obstaculos[numObstaculo[1]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2);
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(posicaoMovimentoObstaculoHorizontal[1]+ obstaculos[numObstaculo[1]].getWidth()*ajusteLargura/2, alturaObstaculo[1]+ obstaculos[numObstaculo[1]].getHeight()*ajusteAltura/2, obstaculos[numObstaculo[1]].getWidth()*ajusteLargura/2);
            shapeRenderer.end();*/
        } else if (numObstaculo[1] == 3) {
            anzoisCircle[1].set(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2, alturaObstaculo[1] + obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[1]].getWidth() * ajusteLargura / 2));
           /* shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(posicaoMovimentoObstaculoHorizontal[1] + obstaculos[numObstaculo[1]].getWidth()*ajusteLargura / 2, alturaObstaculo[1] + obstaculos[numObstaculo[1]].getWidth()*ajusteLargura / 2, (obstaculos[numObstaculo[1]].getWidth()*ajusteLargura / 2));
            shapeRenderer.end();*/
        }

        if (numObstaculo[2] == 0) {
            piranhasVerticalRect[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 3, alturaObstaculo[2], (obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[2]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[2].set(posicaoMovimentoObstaculoHorizontal[2], alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[2]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 3));
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth()*ajusteLargura / 3, alturaObstaculo[2], (obstaculos[numObstaculo[2]].getWidth()*ajusteLargura / 3), obstaculos[numObstaculo[2]].getHeight()*ajusteAltura);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[2], alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight()*ajusteAltura / 3, obstaculos[numObstaculo[2]].getWidth()*ajusteLargura, (obstaculos[numObstaculo[2]].getHeight()*ajusteAltura / 3));
            shapeRenderer.end();*/
        } else if (numObstaculo[2] == 1) {
            tubaroesRect[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 2.5));
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth()*ajusteLargura / 9, (float) (alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight()*ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[2]].getWidth()*ajusteLargura/1.5), (float) (obstaculos[numObstaculo[2]].getHeight()*ajusteAltura / 2.5));
            shapeRenderer.end();*/
        } else if (numObstaculo[2] == 2) {
            aguasSujasCirc[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2, alturaObstaculo[2] + obstaculos[numObstaculo[2]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2);
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(posicaoMovimentoObstaculoHorizontal[2]+ obstaculos[numObstaculo[2]].getWidth()*ajusteLargura/2, alturaObstaculo[2]+ obstaculos[numObstaculo[2]].getHeight()*ajusteAltura/2, obstaculos[numObstaculo[2]].getWidth()*ajusteLargura/2);
            shapeRenderer.end();*/
        } else if (numObstaculo[2] == 3) {
            anzoisCircle[2].set(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2, alturaObstaculo[2] + obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[2]].getWidth() * ajusteLargura / 2));
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(posicaoMovimentoObstaculoHorizontal[2] + obstaculos[numObstaculo[2]].getWidth()*ajusteLargura / 2, alturaObstaculo[2] + obstaculos[numObstaculo[2]].getWidth()*ajusteLargura / 2, (obstaculos[numObstaculo[2]].getWidth()*ajusteLargura / 2));
            shapeRenderer.end();*/
        }

        if (numObstaculo[3] == 0) {
            piranhasVerticalRect[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 3, alturaObstaculo[3], (obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 3), obstaculos[numObstaculo[3]].getHeight() * ajusteAltura);
            piranhasHorizontalRect[3].set(posicaoMovimentoObstaculoHorizontal[3], alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 3, obstaculos[numObstaculo[3]].getWidth() * ajusteLargura, (obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 3));
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth()*ajusteLargura / 3, alturaObstaculo[3], (obstaculos[numObstaculo[3]].getWidth()*ajusteLargura / 3), obstaculos[numObstaculo[3]].getHeight()*ajusteAltura);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[3], alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight()*ajusteAltura / 3, obstaculos[numObstaculo[3]].getWidth()*ajusteLargura, (obstaculos[numObstaculo[3]].getHeight()*ajusteAltura / 3));
            shapeRenderer.end();*/
        } else if (numObstaculo[3] == 1) {
            tubaroesRect[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 9, (float) (alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 1.5), (float) (obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 2.5));
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth()*ajusteLargura / 9, (float) (alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight()*ajusteAltura / 4.5), (float) (obstaculos[numObstaculo[3]].getWidth()*ajusteLargura/1.5), (float) (obstaculos[numObstaculo[3]].getHeight()*ajusteAltura / 2.5));
            shapeRenderer.end();*/
        } else if (numObstaculo[3] == 2) {
            aguasSujasCirc[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2, alturaObstaculo[3] + obstaculos[numObstaculo[3]].getHeight() * ajusteAltura / 2, obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2);
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(posicaoMovimentoObstaculoHorizontal[3]+ obstaculos[numObstaculo[3]].getWidth()*ajusteLargura/2, alturaObstaculo[3]+ obstaculos[numObstaculo[3]].getHeight()*ajusteAltura/2, obstaculos[numObstaculo[3]].getHeight()*ajusteAltura/2);
            shapeRenderer.end();*/
        } else if (numObstaculo[3] == 3) {
            anzoisCircle[3].set(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2, alturaObstaculo[3] + obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2, (obstaculos[numObstaculo[3]].getWidth() * ajusteLargura / 2));
            /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(posicaoMovimentoObstaculoHorizontal[3] + obstaculos[numObstaculo[3]].getWidth()*ajusteLargura / 2, alturaObstaculo[3] + obstaculos[numObstaculo[3]].getWidth()*ajusteLargura / 2, (obstaculos[numObstaculo[3]].getWidth()*ajusteLargura / 2));
            shapeRenderer.end();*/
        }
    }

    private void sorteiaProximoObstaculo() {
        float multLargura = (float) 2.5;
        //Verifica se o obstáculo saiu da tela e manda o próximo obstáculo
        for (int i = 0; i < posicaoMovimentoObstaculoHorizontal.length; i++) {

            if (posicaoMovimentoObstaculoHorizontal[i] < -obstaculos[numObstaculo[i]].getWidth() * ajusteLargura) {
                //qtdObstaculos = numObstaculosRandom.nextInt(2)+1;
                numObstaculo[i] = obstaculoRandom.nextInt(10);
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
                int subtrai = 0;
                if (numObstaculo[i] == 3) {
                    subtrai = (int) (altura / 3);
                }

                posicaoMovimentoObstaculoHorizontal[i] = (largura * multLargura);
                alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (altura - obstaculos[numObstaculo[i]].getHeight() * ajusteAltura / 1.5)) + subtrai;
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
                int subtrai = 0;
                if (numObstaculo[i] == 3) {
                    subtrai = (int) (altura / 3);
                }

                posicaoMovimentoObstaculoHorizontal[i] = (-obstaculos[i].getWidth() * ajusteLargura * multLargura);
                alturaObstaculo[i] = alturaObstaculoRandom.nextInt((int) (altura - obstaculos[numObstaculo[i]].getHeight() * ajusteAltura / 1.5)) + subtrai;
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