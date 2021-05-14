package com.xandi.seafish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.xandi.seafish.SeafishGame;

import java.util.Arrays;
import java.util.Random;

public class GameScreen extends BaseScreen {

    private final SeafishGame seafishGame;

    //Score
    private int meters, metersSpeed;
    private float metersScore;
    private static GlyphLayout metersLayout, recordLayout;
    private BitmapFont recordLabel, tapLabel, metersLabel;

    //Meters while shark
    private float countSharkMeters;

    //shapes
    private Sprite fishSprite, menuSprite, replaySprite, yesSprite, noSprite, pauseSprite, musicSprite;
    private Sprite[] seaweeds;

    //images
    private Texture gameOverText, reload, menuButton, yesButton, noButton, continueButton, videoButton, pauseButton, musicButton;
    private Texture[] background, ornaments;
    private Sprite[][] fishes;
    private Sprite[] sharks;
    private Sprite[] shoals;
    private Sprite[] pollution;
    private Sprite[] hooks;
    private Sprite[] wormScore;
    private Sprite[] obstacles;
    private Sprite wormBonus;
    private Sprite bubble;

    //positions
    private float[] positionMovementHorizontalObstacle;
    private float wormHorizontalBonus;
    private float horizontalBubble;
    private int[] wormDistance, wormHeight;
    private float speed;
    private float fallSpeed;
    private float verticalInitialPosition;
    private float[] verticalHookMovement;
    private float horizontalOrnamentMovement;

    private int countBackground;
    private int countSectorMeters;
    private int currentSectorObstacle;
    private int touches;
    private int touchesToRelease;
    private int countOrnament;

    //Controllers
    private boolean started; //start movements after first touch
    private boolean sector;
    private boolean collides;
    private boolean gameOver;
    private boolean obstacleCollided;
    private boolean isColliding;
    private boolean[] wormCollided;
    private boolean paused;
    private boolean isSlowShark;
    private boolean isShark;
    private boolean displayKeepPlayingScreen;
    private boolean showBonusWorm;
    private boolean showBubble;
    private boolean[] wormCollidedAux;
    private boolean goingBack;
    private boolean extraLife;
    private boolean[] hookTouchedTop;

    //Sorts
    private Random obstacleRandom;
    private int[] obstacleNumber;
    private Random heightObstacleRandom;
    private float[] obstacleHeight;
    private Random wormDistanceRandom, wormHeightRandom;
    private Random sortMetersBySector;
    private int metersBySector;
    private Random pollutionNumber;

    //Shapes for collisions
    private Circle fishCircle, bubbleCircle;
    private Rectangle wormBonusRect;
    private Rectangle[] wormsRect, piranhasVerticalRect, piranhasHorizontalRect, sharksRect;
    private Circle[] hooksCircle, pollutionCircle;

    //Seaweed rotation
    private boolean controlSeaweedRotation;

    //Sounds
    private Sound eatSound, hitSound, gameOverSound, bubbleSound, blowBubbleSound;
    private Music backgroundMusic;

    //Variations
    private int fishVariation;
    private float fishVariationAux;
    private float sharkVariation;
    private int seaweedVariation;
    private float shoalVariation;

    //Constants
    private float INCREASE_SPEED;
    private float JUMP_HEIGHT;
    private float FISH_HORIZONTAL_POSITION;
    private float TOP_HEITHG;
    private float FALL_SPEED;
    private float OBSTACLE_SPEED;
    private float TAP_X;
    private float TAP_Y;
    private float INITIAL_SPEED;
    private int INITIAL_METERS_SPEED;
    private int TOUCHES_HOOK1;
    private int TOUCHES_HOOK2;
    private int TOUCHES_HOOK3;
    private int TOUCHES_HOOK4;

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

        seafishGame.handler.showBannerAd(false);

        background = new Texture[9];
        ornaments = new Texture[7];

        //Init Sprites
        fishes = new Sprite[7][5];
        sharks = new Sprite[2];
        shoals = new Sprite[3];
        pollution = new Sprite[5];
        hooks = new Sprite[4];
        wormScore = new Sprite[10];
        wormBonus = new Sprite();
        bubble = new Sprite(new Texture("images/elements/bolha.png"));

        //Distance between worms and obstacles
        wormDistance = new int[4];
        wormHeight = new int[4];

        //release hook
        touches = 0;
        touchesToRelease = 0;

        countOrnament = 0;

        fallSpeed = 0;

        collides = true;
        gameOver = false;
        started = false;
        obstacleCollided = false;
        isColliding = false;
        paused = false;
        isSlowShark = false;
        isShark = false;
        goingBack = false;
        displayKeepPlayingScreen = false;
        sector = false;
        showBonusWorm = false;
        showBubble = false;
        extraLife = false;

        hookTouchedTop = new boolean[4];

        Arrays.fill(hookTouchedTop, Boolean.FALSE);

        countBackground = 0;
        countSectorMeters = 0;
        currentSectorObstacle = 3;

        //Init shapes
        wormCollidedAux = new boolean[4];
        wormCollided = new boolean[4];
        wormsRect = new Rectangle[4];
        piranhasVerticalRect = new Rectangle[4];
        piranhasHorizontalRect = new Rectangle[4];
        sharksRect = new Rectangle[4];
        hooksCircle = new Circle[4];
        pollutionCircle = new Circle[5];

        //Seaweed rotation
        controlSeaweedRotation = false;

        //Elements Variations
        fishVariation = 0;
        fishVariationAux = 0;
        sharkVariation = 0;
        seaweedVariation = 0;
        shoalVariation = 0;

        countSharkMeters = 0;
        obstacleHeight = new float[4];

        setTextures();

        fishCircle = new Circle();
        wormBonusRect = new Rectangle();
        bubbleCircle = new Circle();

        meters = 0;
        heightObstacleRandom = new Random();
        obstacleRandom = new Random();

        obstacles = new Sprite[11];

        speed = 10 * seafishGame.adjustWidth;
        for (int i = 0; i < obstacleHeight.length; i++) {
            if (i == 0) {
                obstacleHeight[i] = i;
            } else {
                obstacleHeight[i] = seafishGame.height / i;
            }
        }

        verticalInitialPosition = seafishGame.height / 2;
        verticalHookMovement = new float[4];

        obstacles[0] = shoals[0];
        obstacles[1] = sharks[0];
        obstacles[2] = pollution[0];
        obstacles[3] = pollution[1];
        obstacles[4] = pollution[2];
        obstacles[5] = pollution[3];
        obstacles[6] = pollution[4];
        obstacles[7] = hooks[0];
        obstacles[8] = hooks[1];
        obstacles[9] = hooks[2];
        obstacles[10] = hooks[3];

        positionMovementHorizontalObstacle = new float[4];
        horizontalOrnamentMovement = (seafishGame.width - seafishGame.differenceBetweenWidth) - ornaments[countOrnament].getWidth();

        obstacleNumber = new int[4];
        if (!paused) {
            for (int i = 0; i < positionMovementHorizontalObstacle.length; i++) {
                positionMovementHorizontalObstacle[i] = -obstacles[obstacleNumber[i]].getWidth() * seafishGame.adjustWidth;
            }
        }

        metersLabel = new BitmapFont();
        metersLabel.setColor(Color.YELLOW);
        metersLabel.getData().setScale(6 * seafishGame.adjustWidth);
        metersLayout = new GlyphLayout();
        metersLayout.setText(metersLabel, (int) metersScore + "m");

        recordLabel = new BitmapFont();
        recordLabel.setColor(Color.YELLOW);
        recordLabel.getData().setScale(6 * seafishGame.adjustWidth);
        if (recordLayout == null) {
            recordLayout = new GlyphLayout();
            recordLayout.setText(recordLabel, "Record: " + seafishGame.record + "m");
        }

        tapLabel = new BitmapFont();
        tapLabel.setColor(Color.YELLOW);
        tapLabel.getData().setScale(5 * seafishGame.adjustWidth);
        metersScore = 0;
        metersSpeed = 100;

        wormDistanceRandom = new Random();
        wormHeightRandom = new Random();
        sortMetersBySector = new Random();
        pollutionNumber = new Random();

        for (int i = 0; i < piranhasHorizontalRect.length; i++) {
            piranhasVerticalRect[i] = new Rectangle();
            piranhasHorizontalRect[i] = new Rectangle();
            sharksRect[i] = new Rectangle();
            wormsRect[i] = new Rectangle();
            hooksCircle[i] = new Circle();
        }

        for (int i = 0; i < pollutionCircle.length; i++) {
            pollutionCircle[i] = new Circle();
        }

        for (int i = 0; i < wormCollidedAux.length; i++) {
            wormCollidedAux[i] = false;
            wormCollided[i] = false;
        }

        setSounds();

        INCREASE_SPEED = (float) 0.001 * seafishGame.adjustWidth;
        JUMP_HEIGHT = -12 * (seafishGame.adjustWidth);
        FISH_HORIZONTAL_POSITION = seafishGame.width / 12;
        TOP_HEITHG = seafishGame.height - fishSprite.getHeight() * seafishGame.adjustHeight;
        FALL_SPEED = (float) (0.7 * (seafishGame.adjustWidth));
        OBSTACLE_SPEED = 12 * seafishGame.adjustWidth;
        TAP_X = ((seafishGame.width - seafishGame.differenceBetweenWidth) / 8) + fishSprite.getWidth();
        TAP_Y = seafishGame.height - 120;
        INITIAL_SPEED = speed;
        INITIAL_METERS_SPEED = (int) (100 * seafishGame.adjustWidth);
        TOUCHES_HOOK1 = 10;
        TOUCHES_HOOK2 = 15;
        TOUCHES_HOOK3 = 20;
        TOUCHES_HOOK4 = 30;

        setButtons();
        setSeaweed();

        caughtWarms = 0;
        caughtSpecialWarms = 0;
        caughtBubbles = 0;
        caughtByHook = 0;
        turnedShark = 0;

        System.gc();
    }

    @Override
    public void render(float delta) {

        //clean screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Vary fish
        fishVariationAux += (float) 0.05;
        varyFish();
        gameState();
    }

    private void gameState() {
        //Prevent fish from passing the ground
        if (verticalInitialPosition < 10) {
            verticalInitialPosition = seafishGame.height / 2;
        }

        if (!gameOver) {
            sharkVariation += 0.01;
            shoalVariation += 0.05;

            if (metersScore == countSectorMeters + 500 && !sector) {
                sector = true;
                metersBySector = sortMetersBySector.nextInt(250) + 150;
                countSectorMeters = (int) metersScore;
            }
            if (sector) {
                if (metersScore >= countSectorMeters + metersBySector) {
                    sector = false;
                    if (currentSectorObstacle <= 2) {
                        currentSectorObstacle++;
                    } else {
                        currentSectorObstacle = 0;
                    }
                }
            }

            if (horizontalOrnamentMovement < -ornaments[countOrnament].getWidth()) {
                horizontalOrnamentMovement = seafishGame.width + ornaments[countOrnament].getWidth();
            }

            VaryShoal();
            varyShark();

            //game speed
            if (started) {
                if (speed < 35) {
                    speed += INCREASE_SPEED;
                }
            }

            controlSeaweedRotation();

            sortNextTackle();

            //fish goes up on each touch
            if (Gdx.input.justTouched() && !paused && !musicSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                bubbleSound.play(0.1f);
                started = true;
                fallSpeed = JUMP_HEIGHT;
                seafishGame.batch.begin();
                seafishGame.batch.draw(fishes[seafishGame.fishNumber][fishVariation], FISH_HORIZONTAL_POSITION, verticalInitialPosition, fishes[seafishGame.fishNumber][fishVariation].getWidth() * seafishGame.adjustWidth, fishes[seafishGame.fishNumber][fishVariation].getHeight() * seafishGame.adjustHeight);
                seafishGame.batch.end();
            }

            if (Gdx.input.justTouched()) {
                if (pauseSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    started = false;
                }
                if (musicSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    if (backgroundMusic.isPlaying()) {
                        backgroundMusic.pause();
                        musicButton = new Texture("images/buttons/musicoff.png");
                    } else {
                        backgroundMusic.play();
                        musicButton = new Texture("images/buttons/music.png");
                    }
                }
            }

            fishCircle.set(FISH_HORIZONTAL_POSITION + fishes[seafishGame.fishNumber][0].getWidth() * seafishGame.adjustWidth / 2, (verticalInitialPosition + fishes[seafishGame.fishNumber][0].getHeight() * seafishGame.adjustHeight / 2), ((fishes[seafishGame.fishNumber][0].getHeight() * seafishGame.adjustWidth / 2) - fishes[seafishGame.fishNumber][0].getHeight() * seafishGame.adjustHeight / 12));

            //Prevent fish from passing the top
            if (verticalInitialPosition >= TOP_HEITHG) {
                fallSpeed = (float) 0.1;
            }

            //Increase fall speed
            if (started && !paused) {
                fallSpeed += FALL_SPEED;
                //Move obstacle
                for (int i = 0; i < positionMovementHorizontalObstacle.length; i++) {
                    positionMovementHorizontalObstacle[i] -= speed;
                }
                horizontalOrnamentMovement -= speed;
                wormHorizontalBonus -= speed;
                horizontalBubble -= speed;
                meters++;
                if (meters >= metersSpeed) {
                    metersScore++;
                    if (metersSpeed > 10) {
                        metersSpeed -= 0.5;
                    }
                    meters = 0;
                }

                //Make the fish fall
                if (verticalInitialPosition > seafishGame.height / 13 || fallSpeed < 0) {
                    verticalInitialPosition = verticalInitialPosition - fallSpeed;
                }

                if (collides) {
                    testCollision();
                } else {
                    increaseGameSpeed();
                }
            }
            if (collides) {
                testHook();
            }
        }

        seafishGame.batch.begin();
        seafishGame.batch.draw(background[countBackground], 0, 0, seafishGame.width, seafishGame.height);
        seafishGame.batch.draw(ornaments[countOrnament], horizontalOrnamentMovement, 7, ornaments[countOrnament].getWidth() * seafishGame.adjustWidth, ornaments[countOrnament].getHeight() * seafishGame.adjustHeight);
        seaweeds[seaweedVariation].draw(seafishGame.batch);
        seaweeds[2].draw(seafishGame.batch);
        seafishGame.batch.draw(fishes[seafishGame.fishNumber][fishVariation], FISH_HORIZONTAL_POSITION, verticalInitialPosition, fishes[seafishGame.fishNumber][fishVariation].getWidth() * seafishGame.adjustWidth, fishes[seafishGame.fishNumber][fishVariation].getHeight() * seafishGame.adjustHeight);
        seafishGame.batch.draw(pauseButton, (pauseButton.getWidth() * seafishGame.adjustWidth), (seafishGame.height - ((float) pauseButton.getHeight() * seafishGame.adjustHeight * 1.5f)), pauseButton.getWidth() * seafishGame.adjustWidth, pauseButton.getHeight() * seafishGame.adjustHeight);
        seafishGame.batch.draw(musicButton, (pauseButton.getWidth() * seafishGame.adjustWidth * 3f), (seafishGame.height - ((float) pauseButton.getHeight() * seafishGame.adjustHeight * 1.5f)), musicButton.getWidth() * seafishGame.adjustWidth, musicButton.getHeight() * seafishGame.adjustHeight);

        if (!goingBack) {
            upAndDownHook();
        }
        for (int i = 0; i < obstacleNumber.length; i++) {
            if (obstacleNumber[i] > 6) {
                seafishGame.batch.draw(obstacles[obstacleNumber[i]], positionMovementHorizontalObstacle[i], verticalHookMovement[i], obstacles[obstacleNumber[i]].getWidth() * seafishGame.adjustWidth, obstacles[obstacleNumber[i]].getHeight() * seafishGame.adjustHeight);
            } else {
                seafishGame.batch.draw(obstacles[obstacleNumber[i]], positionMovementHorizontalObstacle[i], obstacleHeight[i], obstacles[obstacleNumber[i]].getWidth() * seafishGame.adjustWidth, obstacles[obstacleNumber[i]].getHeight() * seafishGame.adjustHeight);
            }
        }

        for (int i = 0; i < wormsRect.length; i++) {
            if (!isSlowShark && wormHeight[i] > seafishGame.height / 13) {
                wormsRect[i].set(positionMovementHorizontalObstacle[i] - wormDistance[i], wormHeight[i], wormScore[i].getWidth() * seafishGame.adjustWidth, wormScore[i].getHeight() * seafishGame.adjustHeight);

                if (!wormCollidedAux[i] && !paused) {
                    seafishGame.batch.draw(wormScore[i], positionMovementHorizontalObstacle[i] - wormDistance[i], wormHeight[i], wormScore[i].getWidth() * seafishGame.adjustWidth, wormScore[i].getHeight() * seafishGame.adjustHeight);
                }
            }
        }

        if (metersScore >= 2500 && metersScore % 2500 == 0 && !extraLife) {
            showBubble = true;
            horizontalBubble = seafishGame.width;
        }
        if (horizontalBubble < 0) {
            showBubble = false;
        }
        if (showBubble) {
            bubbleCircle.set(horizontalBubble, seafishGame.height / 2, (float) (bubble.getTexture().getWidth() / 2) * seafishGame.adjustWidth);
            seafishGame.batch.draw(bubble, horizontalBubble, seafishGame.height / 2, bubble.getWidth() * seafishGame.adjustWidth, bubble.getHeight() * seafishGame.adjustHeight);
        }
        if (Intersector.overlaps(fishCircle, bubbleCircle) && showBubble) {
            blowBubbleSound.play();
            showBubble = false;
            extraLife = true;
            caughtBubbles++;
        }
        if (metersScore % 650 == 0 && metersScore != 0) {
            showBonusWorm = true;
            wormHorizontalBonus = seafishGame.width;
        }
        if (wormHorizontalBonus < wormBonus.getWidth() * seafishGame.adjustWidth) {
            showBonusWorm = false;
        }
        if (showBonusWorm && !isShark && !isSlowShark) {
            wormBonusRect.set(wormHorizontalBonus, seafishGame.height / 2, wormBonus.getWidth() * seafishGame.adjustWidth, wormBonus.getHeight() * seafishGame.adjustHeight);
            seafishGame.batch.draw(wormBonus, wormHorizontalBonus, seafishGame.height / 2, wormBonus.getWidth() * seafishGame.adjustWidth, wormBonus.getHeight() * seafishGame.adjustHeight);
        }

        if (Intersector.overlaps(fishCircle, wormBonusRect) && showBonusWorm) {
            showBonusWorm = false;
            collides = false;
            turnedShark++;
            caughtSpecialWarms++;
        }

        if (Intersector.overlaps(fishCircle, wormsRect[0])) {
            countWarm(0);
            wormCollidedAux[0] = true;
        } else if (Intersector.overlaps(fishCircle, wormsRect[1])) {
            countWarm(1);
            wormCollidedAux[1] = true;
        } else if (Intersector.overlaps(fishCircle, wormsRect[2])) {
            countWarm(2);
            wormCollidedAux[2] = true;
        } else if (Intersector.overlaps(fishCircle, wormsRect[3])) {
            countWarm(3);
            wormCollidedAux[3] = true;
        } else {
            wormCollided[0] = false;
            wormCollided[1] = false;
            wormCollided[2] = false;
            wormCollided[3] = false;
        }

        for (int i = 1; i <= seafishGame.wormQuantity; i++) {
            seafishGame.batch.draw(wormScore[i - 1], (float) ((seafishGame.width - seafishGame.differenceBetweenWidth) - (wormScore[i - 1].getWidth() * seafishGame.adjustWidth * i * 1.15)), (seafishGame.height - recordLayout.height - (wormScore[i - 1].getHeight() * seafishGame.adjustHeight * 1.5f)), wormScore[i - 1].getWidth() * seafishGame.adjustWidth, wormScore[i - 1].getHeight() * seafishGame.adjustHeight);
        }
        if (extraLife) {
            seafishGame.batch.draw(bubble, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2), (float) (seafishGame.height - wormBonus.getHeight() * seafishGame.adjustHeight * 1.3), bubble.getTexture().getWidth() * seafishGame.adjustWidth, bubble.getTexture().getHeight() * seafishGame.adjustHeight);
        }

        metersLabel.draw(seafishGame.batch, (int) metersScore + "m", (seafishGame.width - seafishGame.differenceBetweenWidth) / 2 - (metersLayout.width / 2), seafishGame.height - (seafishGame.height / 10));
        recordLabel.draw(seafishGame.batch, recordLayout, (seafishGame.width - seafishGame.differenceBetweenWidth - (recordLayout.width) - 15), seafishGame.height - 15);
        if (gameOver) {
            seafishGame.batch.draw(gameOverText, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (gameOverText.getWidth() * seafishGame.adjustWidth / 2), (float) (seafishGame.height - (gameOverText.getHeight() * seafishGame.adjustHeight * 2.5)), gameOverText.getWidth() * seafishGame.adjustWidth, gameOverText.getHeight() * seafishGame.adjustHeight);
            seafishGame.batch.draw(reload, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (reload.getWidth() * seafishGame.adjustWidth), seafishGame.height / 2, 150 * seafishGame.adjustWidth, 150 * seafishGame.adjustHeight);
            seafishGame.batch.draw(menuButton, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (menuButton.getWidth() * seafishGame.adjustWidth / 2), (float) ((seafishGame.height / 3) - menuSprite.getHeight() * seafishGame.adjustHeight * 1.5), menuButton.getWidth() * seafishGame.adjustWidth, menuButton.getHeight() * seafishGame.adjustHeight);
        }

        if (displayKeepPlayingScreen) {
            seafishGame.batch.draw(continueButton, ((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (continueButton.getWidth() * seafishGame.adjustWidth / 2), (float) (seafishGame.height - (continueButton.getHeight() * seafishGame.adjustHeight * 2.5)), continueButton.getWidth() * seafishGame.adjustWidth, continueButton.getHeight() * seafishGame.adjustHeight);
            seafishGame.batch.draw(yesButton, (float) (((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (yesButton.getWidth() * seafishGame.adjustWidth * 1.5)), (float) ((seafishGame.height / 3) - yesSprite.getHeight() * seafishGame.adjustHeight * 1.5), yesButton.getWidth() * seafishGame.adjustWidth, yesButton.getHeight() * seafishGame.adjustHeight);
            seafishGame.batch.draw(noButton, (((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) + (noButton.getWidth() * seafishGame.adjustWidth)), (float) ((seafishGame.height / 3) - noSprite.getHeight() * seafishGame.adjustHeight * 1.5), noButton.getWidth() * seafishGame.adjustWidth, noButton.getHeight() * seafishGame.adjustHeight);
            seafishGame.batch.draw(videoButton, (float) (((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (yesButton.getWidth() * seafishGame.adjustWidth * 1.5) + videoButton.getWidth() * seafishGame.adjustWidth * 5), (float) ((seafishGame.height / 3) - yesSprite.getHeight() * seafishGame.adjustHeight * 1.5), yesButton.getWidth() * seafishGame.adjustWidth, yesButton.getHeight() * seafishGame.adjustHeight);
        }

        if (goingBack) {
            if (fishVariation == 0) {
                tapLabel.draw(seafishGame.batch, "tap!", TAP_X, TAP_Y);
            }
        }

        seafishGame.batch.end();

        setTackles();

        //Game over buttons
        if (Gdx.input.justTouched()) {
            if (gameOver) {
                if (replaySprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    gameOver = false;
                    int peixe = seafishGame.fishNumber;
                    System.gc();
                    dispose();
                    seafishGame.setScreen(new GameScreen(seafishGame));
                    if (metersScore < 300) {
                        seafishGame.countMatches++;
                    }
                    seafishGame.fishNumber = peixe;
                }
                if (menuSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    System.gc();
                    dispose();
                    seafishGame.setScreen(new MenuScreen(seafishGame));
                    if (metersScore < 300) {
                        seafishGame.countMatches++;
                    }
                }
            }
        }

        if (Gdx.input.justTouched()) {
            if (displayKeepPlayingScreen) {
                if (yesSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    seafishGame.googleServices.showRewardedVideoAd();
                    displayKeepPlayingScreen = false;
                }
                if (noSprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    displayKeepPlayingScreen = false;
                    gameOver();
                }
            }
        }
    }

    private void upAndDownHook() {
        for (int i = 0; i < hookTouchedTop.length; i++) {
            if (verticalHookMovement[i] >= seafishGame.height - 10) {
                hookTouchedTop[i] = true;
            }

            if (verticalHookMovement[i] <= 0) {
                hookTouchedTop[i] = false;
            }

            if (started) {
                if (hookTouchedTop[i]) {
                    verticalHookMovement[i] -= (speed / 2);
                } else {
                    verticalHookMovement[i] += (speed / 2);
                }
            }
        }
    }

    private void varyShark() {
        if (sharkVariation > 1) {
            sharkVariation = 0;
        } else if (sharkVariation >= 0.5) {
            obstacles[1] = sharks[0];
        } else {
            obstacles[1] = sharks[1];
        }
    }

    private void varyFish() {
        if (!isShark) {
            if (fishVariationAux > 2) {
                fishVariationAux = 0;
            } else if (fishVariationAux >= 1) {
                fishVariation = 0;
            } else {
                fishVariation = 1;
            }
        } else if (isSlowShark) {
            if (fishVariationAux > 2) {
                fishVariationAux = 0;
            } else if (fishVariationAux >= 1) {
                fishVariation = 1;
            } else {
                fishVariation = 3;
            }
        } else {
            if (fishVariationAux > 2) {
                fishVariationAux = 0;
            } else if (fishVariationAux >= 1) {
                fishVariation = 3;
            } else {
                fishVariation = 4;
            }
        }
    }

    private void VaryShoal() {
        if (shoalVariation > 1) {
            shoalVariation = 0;
        } else if (shoalVariation >= 0.5) {
            obstacles[0] = shoals[0];
        } else {
            obstacles[0] = shoals[1];
        }
    }

    private void controlSeaweedRotation() {
        if (controlSeaweedRotation) {
            if (seaweeds[2].getRotation() <= -35 * (seafishGame.adjustWidth)) {
                controlSeaweedRotation = false;
            }
            seaweeds[2].rotate((float) -0.4 * seafishGame.adjustWidth);
        } else {
            if (seaweeds[2].getRotation() >= 20 * (seafishGame.adjustWidth)) {
                controlSeaweedRotation = true;
            }
            seaweeds[2].rotate((float) 0.4 * seafishGame.adjustWidth);
        }
        seaweeds[seaweedVariation].setPosition(positionMovementHorizontalObstacle[seaweedVariation], ((-seaweeds[seaweedVariation].getTexture().getHeight()) * seafishGame.adjustHeight) / 5);
        seaweeds[2].setPosition(positionMovementHorizontalObstacle[2], ((-seaweeds[2].getTexture().getHeight()) * seafishGame.adjustHeight) / 5);
    }

    private void testHook() {
        if (Intersector.overlaps(fishCircle, hooksCircle[0])) {
            backToStart(1);
        } else if (Intersector.overlaps(fishCircle, hooksCircle[1])) {
            backToStart(2);
        } else if (Intersector.overlaps(fishCircle, hooksCircle[2])) {
            backToStart(3);
        } else if (Intersector.overlaps(fishCircle, hooksCircle[3])) {
            backToStart(4);
        } else {
            goingBack = false;
            if (!displayKeepPlayingScreen) {
                paused = false;
            }
        }
    }

    private void backToStart(float circle) {
        if (!goingBack) {
            caughtByHook++;
        }
        goingBack = true;
        sector = false;
        countSectorMeters = 0;
        if (Gdx.input.justTouched()) {
            started = true;
        }
        if (started) {
            seafishGame.wormQuantity = 0;
            fallSpeed = 0;
            paused = true;
            if (circle == 1) {
                for (int i = 0; i < positionMovementHorizontalObstacle.length; i++) {
                    if (i == 0) {
                        positionMovementHorizontalObstacle[i] += 0;
                    } else {
                        positionMovementHorizontalObstacle[i] += speed * OBSTACLE_SPEED;
                    }
                }
            } else if (circle == 2) {
                for (int i = 0; i < positionMovementHorizontalObstacle.length; i++) {
                    if (i == 1) {
                        positionMovementHorizontalObstacle[i] += 0;
                    } else {
                        positionMovementHorizontalObstacle[i] += speed * OBSTACLE_SPEED;
                    }
                }
            } else if (circle == 3) {
                for (int i = 0; i < positionMovementHorizontalObstacle.length; i++) {
                    if (i == 2) {
                        positionMovementHorizontalObstacle[i] += 0;
                    } else {
                        positionMovementHorizontalObstacle[i] += speed * OBSTACLE_SPEED;
                    }
                }
            } else if (circle == 4) {
                for (int i = 0; i < positionMovementHorizontalObstacle.length; i++) {
                    if (i == 3) {
                        positionMovementHorizontalObstacle[i] += 0;
                    } else {
                        positionMovementHorizontalObstacle[i] += speed * OBSTACLE_SPEED;
                    }
                }
            }

            horizontalOrnamentMovement += speed * OBSTACLE_SPEED;

            if (metersScore > 0) {
                sortPreviousTackle();
            } else {
                gameOver = false;
                collides = true;
                verticalInitialPosition = verticalInitialPosition - 30;
                speed = INITIAL_SPEED;
                metersSpeed = INITIAL_METERS_SPEED;
            }

            if (Gdx.input.justTouched()) {
                switch ((int) circle - 1) {
                    case 0:
                        touchesToRelease = TOUCHES_HOOK1;
                        break;
                    case 1:
                        touchesToRelease = TOUCHES_HOOK2;
                        break;
                    case 2:
                        touchesToRelease = TOUCHES_HOOK3;
                        break;
                    case 3:
                        touchesToRelease = TOUCHES_HOOK4;
                        break;
                    default:
                        touchesToRelease = TOUCHES_HOOK1;
                }
                touches++;
                if (touches >= touchesToRelease) {
                    verticalInitialPosition -= 30;
                    positionMovementHorizontalObstacle[0] = seafishGame.width;
                    positionMovementHorizontalObstacle[1] = (float) ((seafishGame.width - seafishGame.differenceBetweenWidth) * 1.5);
                    positionMovementHorizontalObstacle[2] = seafishGame.width * 2;
                    positionMovementHorizontalObstacle[3] = (float) ((seafishGame.width - seafishGame.differenceBetweenWidth) * 2.5);
                    gameOver = false;
                    collides = true;
                    verticalInitialPosition = seafishGame.height / 2;
                    touches = 0;

                    if (metersScore >= 4000) {
                        speed = 33 * seafishGame.adjustWidth;
                        metersSpeed = (int) (INITIAL_SPEED);
                    } else if (metersScore >= 3000) {
                        speed = 30 * seafishGame.adjustWidth;
                        metersSpeed = (int) (25 * seafishGame.adjustWidth);
                    } else if (metersScore >= 25000) {
                        speed = 27 * seafishGame.adjustWidth;
                        metersSpeed = (int) (36 * seafishGame.adjustWidth);
                    } else if (metersScore >= 2000) {
                        speed = 24 * seafishGame.adjustWidth;
                        metersSpeed = (int) (47 * seafishGame.adjustWidth);
                    } else if (metersScore >= 1500) {
                        speed = 21 * seafishGame.adjustWidth;
                        metersSpeed = (int) (58 * seafishGame.adjustWidth);
                    } else if (metersScore >= 1000) {
                        speed = 18 * seafishGame.adjustWidth;
                        metersSpeed = (int) (71 * seafishGame.adjustWidth);
                    } else if (metersScore >= 500) {
                        speed = 15 * seafishGame.adjustWidth;
                        metersSpeed = (int) (82 * seafishGame.adjustWidth);
                    } else if (metersScore >= 250) {
                        speed = 12 * seafishGame.adjustWidth;
                        metersSpeed = (int) (90 * seafishGame.adjustWidth);
                    } else if (metersScore <= 5) {
                        speed = INITIAL_SPEED;
                        metersSpeed = (int) (100 * seafishGame.adjustWidth);
                    }
                }
            }
        }
    }

    private void countWarm(int warmNumber) {
        if (!wormCollided[warmNumber]) {
            if (seafishGame.wormQuantity <= 9) {
                if (!isSlowShark) {
                    seafishGame.wormQuantity++;
                    caughtWarms++;
                    eatSound.play(0.9f);
                    wormCollided[warmNumber] = true;
                }
            } else if (!isShark) {
                seafishGame.wormQuantity = 1;
                collides = false;
                changeBackground();
                turnedShark++;
            }
        }
    }

    private void changeBackground() {
        if (countBackground <= 5) {
            countBackground++;
            countOrnament++;
        } else {
            countBackground = 0;
            countOrnament = 0;
        }
    }

    private void increaseGameSpeed() {
        countSharkMeters += 0.5;
        if (countSharkMeters <= 100) {
            this.metersScore += 0.5;
            for (int i = 0; i < positionMovementHorizontalObstacle.length; i++) {
                positionMovementHorizontalObstacle[i] -= speed * (INITIAL_SPEED);
            }
            horizontalOrnamentMovement -= speed * INITIAL_SPEED;
            isShark = true;
        } else if (countSharkMeters <= 190) {
            if (countSharkMeters % 5 == 0) {
                changeBackground();
            }
            isSlowShark = true;
            isShark = true;
        } else {
            collides = true;
            countSharkMeters = 0;
            isSlowShark = false;
            isShark = false;
        }
    }

    private void testCollision() {
        if (Intersector.overlaps(fishCircle, piranhasHorizontalRect[0]) || Intersector.overlaps(fishCircle, piranhasHorizontalRect[1]) ||
                Intersector.overlaps(fishCircle, piranhasHorizontalRect[2]) || Intersector.overlaps(fishCircle, piranhasHorizontalRect[3]) ||
                Intersector.overlaps(fishCircle, piranhasVerticalRect[0]) || Intersector.overlaps(fishCircle, piranhasVerticalRect[1]) ||
                Intersector.overlaps(fishCircle, piranhasVerticalRect[2]) || Intersector.overlaps(fishCircle, piranhasVerticalRect[3])) {

            deathTackle = "images/obstacles/piranhas.png";
            actionOnCollision(2);

        } else if (Intersector.overlaps(fishCircle, sharksRect[0]) || Intersector.overlaps(fishCircle, sharksRect[1]) ||
                Intersector.overlaps(fishCircle, sharksRect[2]) || Intersector.overlaps(fishCircle, sharksRect[3])) {

            deathTackle = "images/obstacles/tubaraoinimigo2.png";
            actionOnCollision(2);

        } else if (Intersector.overlaps(fishCircle, pollutionCircle[0])) {
            deathTackle = obstacles[obstacleNumber[0]].getTexture().toString();
            actionOnCollision(4);
        } else if (Intersector.overlaps(fishCircle, pollutionCircle[1])) {
            deathTackle = obstacles[obstacleNumber[1]].getTexture().toString();
            actionOnCollision(4);
        } else if (Intersector.overlaps(fishCircle, pollutionCircle[2])) {
            deathTackle = obstacles[obstacleNumber[2]].getTexture().toString();
            actionOnCollision(4);
        } else if (Intersector.overlaps(fishCircle, pollutionCircle[3])) {
            deathTackle = obstacles[obstacleNumber[3]].getTexture().toString();
            actionOnCollision(4);
        } else if (Intersector.overlaps(fishCircle, pollutionCircle[4])) {
            deathTackle = obstacles[obstacleNumber[4]].getTexture().toString();
            actionOnCollision(4);
        } else {
            obstacleCollided = false;
            isColliding = false;
        }
        if (isColliding) {
            fishVariation = 2;
        }
    }

    private void actionOnCollision(int decreaseLives) {
        isColliding = true;
        if (!obstacleCollided) {
            Gdx.input.vibrate(50);
            seafishGame.wormQuantity -= decreaseLives;
            hitSound.play();
            obstacleCollided = true;
            if (seafishGame.wormQuantity < 0) {
                if (extraLife) {
                    extraLife = false;
                    showBubble = true;
                    seafishGame.wormQuantity = 0;
                } else {
                    if (!seafishGame.isRewardedYet && showRewardVideo()) {
                        if (seafishGame.googleServices.hasVideoLoaded()) {
                            started = false;
                            paused = true;
                            displayKeepPlayingScreen = true;
                        } else {
                            gameOver();
                        }
                    } else {
                        gameOver();
                        if (seafishGame.countMatches == 2 || metersScore >= 300) {
                            seafishGame.handler.showInterstitialAd(new Runnable() {
                                @Override
                                public void run() {
                                    seafishGame.countMatches = 0;
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
        seafishGame.saveScores((int) metersScore, fishes[seafishGame.fishNumber][fishVariation].getTexture().toString(), deathTackle, caughtWarms, caughtSpecialWarms, turnedShark, caughtBubbles, caughtByHook);
    }

    private boolean showRewardVideo() {
        return (seafishGame.record != 0) && (metersScore >= seafishGame.record - (seafishGame.record / 2.1F)) && (metersScore <= seafishGame.record);
    }

    private void setTackles() {
        if (obstacleNumber[0] == 0) {
            piranhasVerticalRect[0].set(positionMovementHorizontalObstacle[0] + obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth / 3, obstacleHeight[0], (obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth / 3), obstacles[obstacleNumber[0]].getHeight() * seafishGame.adjustHeight);
            piranhasHorizontalRect[0].set(positionMovementHorizontalObstacle[0], obstacleHeight[0] + obstacles[obstacleNumber[0]].getHeight() * seafishGame.adjustHeight / 3, obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth, (obstacles[obstacleNumber[0]].getHeight() * seafishGame.adjustHeight / 3));
        } else if (obstacleNumber[0] == 1) {
            sharksRect[0].set(positionMovementHorizontalObstacle[0] + obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth / 9, (float) (obstacleHeight[0] + obstacles[obstacleNumber[0]].getHeight() * seafishGame.adjustHeight / 4.5), (float) (obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth / 1.5), (float) (obstacles[obstacleNumber[0]].getHeight() * seafishGame.adjustHeight / 2.5));
        } else if (obstacleNumber[0] == 2 || obstacleNumber[0] == 3 || obstacleNumber[0] == 4 || obstacleNumber[0] == 5 || obstacleNumber[0] == 6) {
            pollutionCircle[0].set(positionMovementHorizontalObstacle[0] + obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth / 2, obstacleHeight[0] + obstacles[obstacleNumber[0]].getHeight() * seafishGame.adjustHeight / 2, obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth / 2);
        } else if (obstacleNumber[0] == 7 || obstacleNumber[0] == 8 || obstacleNumber[0] == 9 || obstacleNumber[0] == 10) {
            hooksCircle[0].set(positionMovementHorizontalObstacle[0] + obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth / 2, verticalHookMovement[0] + obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth / 2, (obstacles[obstacleNumber[0]].getWidth() * seafishGame.adjustWidth / 2));
        }

        if (obstacleNumber[1] == 0) {
            piranhasVerticalRect[1].set(positionMovementHorizontalObstacle[1] + obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth / 3, obstacleHeight[1], (obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth / 3), obstacles[obstacleNumber[1]].getHeight() * seafishGame.adjustHeight);
            piranhasHorizontalRect[1].set(positionMovementHorizontalObstacle[1], obstacleHeight[1] + obstacles[obstacleNumber[1]].getHeight() * seafishGame.adjustHeight / 3, obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth, (obstacles[obstacleNumber[1]].getHeight() * seafishGame.adjustHeight / 3));
        } else if (obstacleNumber[1] == 1) {
            sharksRect[1].set(positionMovementHorizontalObstacle[1] + obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth / 9, (float) (obstacleHeight[1] + obstacles[obstacleNumber[1]].getHeight() * seafishGame.adjustHeight / 4.5), (float) (obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth / 1.5), (float) (obstacles[obstacleNumber[1]].getHeight() * seafishGame.adjustHeight / 2.5));
        } else if (obstacleNumber[1] == 2 || obstacleNumber[1] == 3 || obstacleNumber[1] == 4 || obstacleNumber[1] == 5 || obstacleNumber[1] == 6) {
            pollutionCircle[1].set(positionMovementHorizontalObstacle[1] + obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth / 2, obstacleHeight[1] + obstacles[obstacleNumber[1]].getHeight() * seafishGame.adjustHeight / 2, obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth / 2);
        } else if (obstacleNumber[1] == 7 || obstacleNumber[1] == 8 || obstacleNumber[1] == 9 || obstacleNumber[1] == 10) {
            hooksCircle[1].set(positionMovementHorizontalObstacle[1] + obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth / 2, verticalHookMovement[1] + obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth / 2, (obstacles[obstacleNumber[1]].getWidth() * seafishGame.adjustWidth / 2));
        }

        if (obstacleNumber[2] == 0) {
            piranhasVerticalRect[2].set(positionMovementHorizontalObstacle[2] + obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth / 3, obstacleHeight[2], (obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth / 3), obstacles[obstacleNumber[2]].getHeight() * seafishGame.adjustHeight);
            piranhasHorizontalRect[2].set(positionMovementHorizontalObstacle[2], obstacleHeight[2] + obstacles[obstacleNumber[2]].getHeight() * seafishGame.adjustHeight / 3, obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth, (obstacles[obstacleNumber[2]].getHeight() * seafishGame.adjustHeight / 3));
        } else if (obstacleNumber[2] == 1) {
            sharksRect[2].set(positionMovementHorizontalObstacle[2] + obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth / 9, (float) (obstacleHeight[2] + obstacles[obstacleNumber[2]].getHeight() * seafishGame.adjustHeight / 4.5), (float) (obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth / 1.5), (float) (obstacles[obstacleNumber[2]].getHeight() * seafishGame.adjustHeight / 2.5));
        } else if (obstacleNumber[2] == 2 || obstacleNumber[2] == 3 || obstacleNumber[2] == 4 || obstacleNumber[2] == 5 || obstacleNumber[2] == 6) {
            pollutionCircle[2].set(positionMovementHorizontalObstacle[2] + obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth / 2, obstacleHeight[2] + obstacles[obstacleNumber[2]].getHeight() * seafishGame.adjustHeight / 2, obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth / 2);
        } else if (obstacleNumber[2] == 7 || obstacleNumber[2] == 8 || obstacleNumber[2] == 9 || obstacleNumber[2] == 10) {
            hooksCircle[2].set(positionMovementHorizontalObstacle[2] + obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth / 2, verticalHookMovement[2] + obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth / 2, (obstacles[obstacleNumber[2]].getWidth() * seafishGame.adjustWidth / 2));
        }

        if (obstacleNumber[3] == 0) {
            piranhasVerticalRect[3].set(positionMovementHorizontalObstacle[3] + obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth / 3, obstacleHeight[3], (obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth / 3), obstacles[obstacleNumber[3]].getHeight() * seafishGame.adjustHeight);
            piranhasHorizontalRect[3].set(positionMovementHorizontalObstacle[3], obstacleHeight[3] + obstacles[obstacleNumber[3]].getHeight() * seafishGame.adjustHeight / 3, obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth, (obstacles[obstacleNumber[3]].getHeight() * seafishGame.adjustHeight / 3));
        } else if (obstacleNumber[3] == 1) {
            sharksRect[3].set(positionMovementHorizontalObstacle[3] + obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth / 9, (float) (obstacleHeight[3] + obstacles[obstacleNumber[3]].getHeight() * seafishGame.adjustHeight / 4.5), (float) (obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth / 1.5), (float) (obstacles[obstacleNumber[3]].getHeight() * seafishGame.adjustHeight / 2.5));
        } else if (obstacleNumber[3] == 2 || obstacleNumber[3] == 3 || obstacleNumber[3] == 4 || obstacleNumber[3] == 5 || obstacleNumber[3] == 6) {
            pollutionCircle[3].set(positionMovementHorizontalObstacle[3] + obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth / 2, obstacleHeight[3] + obstacles[obstacleNumber[3]].getHeight() * seafishGame.adjustHeight / 2, obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth / 2);
        } else if (obstacleNumber[3] == 7 || obstacleNumber[3] == 8 || obstacleNumber[3] == 9 || obstacleNumber[3] == 10) {
            hooksCircle[3].set(positionMovementHorizontalObstacle[3] + obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth / 2, verticalHookMovement[3] + obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth / 2, (obstacles[obstacleNumber[3]].getWidth() * seafishGame.adjustWidth / 2));
        }
    }

    private void sortNextTackle() {
        float multLargura = (float) 2.5;
        //Check if obstacle left screen and send the next one
        for (int i = 0; i < positionMovementHorizontalObstacle.length; i++) {

            if (positionMovementHorizontalObstacle[i] < -obstacles[obstacleNumber[i]].getWidth() * seafishGame.adjustWidth) {
                //Sort next obstacle
                obstacleNumber[i] = obstacleRandom.nextInt(10);
                if (!sector) {
                    if (obstacleNumber[i] >= 0 && obstacleNumber[i] <= 3) {
                        obstacleNumber[i] = 0;
                    } else if (obstacleNumber[i] >= 4 && obstacleNumber[i] <= 6) {
                        obstacleNumber[i] = 1;
                    } else if (obstacleNumber[i] >= 7 && obstacleNumber[i] <= 8) {
                        obstacleNumber[i] = pollutionNumber.nextInt(5) + 2;
                    } else if (obstacleNumber[i] == 9) {
                        obstacleNumber[i] = i + 7;
                    }
                } else {
                    if (currentSectorObstacle == 3) {
                        obstacleNumber[i] = i + 7;
                    } else if (currentSectorObstacle == 2) {
                        obstacleNumber[i] = pollutionNumber.nextInt(5) + 2;
                    } else {
                        obstacleNumber[i] = currentSectorObstacle;
                    }
                }
                if (obstacleNumber[i] == 7 || obstacleNumber[i] == 8 || obstacleNumber[i] == 9 || obstacleNumber[i] == 10) {
                    obstacleHeight[i] = heightObstacleRandom.nextInt((int) (obstacles[obstacleNumber[i]].getHeight() * seafishGame.adjustHeight / 1.5));
                    verticalHookMovement[i] = obstacleHeight[i];
                } else {
                    obstacleHeight[i] = heightObstacleRandom.nextInt((int) (seafishGame.height - obstacles[obstacleNumber[i]].getHeight() * seafishGame.adjustHeight / 1.5));
                }

                positionMovementHorizontalObstacle[i] = ((seafishGame.width - seafishGame.differenceBetweenWidth) * multLargura);
                if (!isSlowShark) {
                    wormDistance[i] = wormDistanceRandom.nextInt((int) (obstacles[obstacleNumber[i]].getWidth() * seafishGame.adjustWidth * 2));
                    wormHeight[i] = (int) obstacleHeight[i] - ((wormHeightRandom.nextInt((int) (obstacles[obstacleNumber[i]].getHeight() * seafishGame.adjustHeight * 2))));
                    wormCollidedAux[i] = false;
                }
            }
            multLargura -= 0.5;
        }
    }

    private void sortPreviousTackle() {
        float multLargura = (float) 2.5;
        //Check if obstacle left screen and send another one
        for (int i = 0; i < positionMovementHorizontalObstacle.length; i++) {

            if (positionMovementHorizontalObstacle[i] > seafishGame.width + obstacles[obstacleNumber[i]].getWidth() * seafishGame.adjustWidth) {
                metersScore -= 0.5;
                //Sort next obstacle
                obstacleNumber[i] = obstacleRandom.nextInt(10);
                if (obstacleNumber[i] >= 0 && obstacleNumber[i] <= 3) {
                    obstacleNumber[i] = 0;
                } else if (obstacleNumber[i] >= 4 && obstacleNumber[i] <= 6) {
                    obstacleNumber[i] = 1;
                } else if (obstacleNumber[i] >= 7 && obstacleNumber[i] <= 8) {
                    obstacleNumber[i] = 2;
                } else if (obstacleNumber[i] == 9) {
                    obstacleNumber[i] = 3;
                }
                if (obstacleNumber[i] == 3 || obstacleNumber[i] == 4 || obstacleNumber[i] == 5 || obstacleNumber[i] == 6) {
                    obstacleHeight[i] = heightObstacleRandom.nextInt((int) (obstacles[obstacleNumber[i]].getHeight() * seafishGame.adjustHeight / 1.5));
                } else {
                    obstacleHeight[i] = heightObstacleRandom.nextInt((int) (seafishGame.height - obstacles[obstacleNumber[i]].getHeight() * seafishGame.adjustHeight / 1.5));
                }
                positionMovementHorizontalObstacle[i] = (-obstacles[i].getWidth() * seafishGame.adjustWidth * multLargura);
            }
            multLargura -= 0.5;

        }
    }

    private void setFishes() {
        fishes[0][0] = new Sprite(new Texture("images/characters/peixe1.png"));
        fishes[0][1] = new Sprite(new Texture("images/characters/peixe12.png"));
        fishes[0][2] = new Sprite(new Texture("images/characters/peixe1red.png"));
        fishes[0][3] = new Sprite(new Texture("images/characters/tubarao1.png"));
        fishes[0][4] = new Sprite(new Texture("images/characters/tubarao12.png"));
        fishes[1][0] = new Sprite(new Texture("images/characters/peixe2.png"));
        fishes[1][1] = new Sprite(new Texture("images/characters/peixe22.png"));
        fishes[1][2] = new Sprite(new Texture("images/characters/peixe2red.png"));
        fishes[1][3] = new Sprite(new Texture("images/characters/tubarao2.png"));
        fishes[1][4] = new Sprite(new Texture("images/characters/tubarao22.png"));
        fishes[2][0] = new Sprite(new Texture("images/characters/peixe3.png"));
        fishes[2][1] = new Sprite(new Texture("images/characters/peixe32.png"));
        fishes[2][2] = new Sprite(new Texture("images/characters/peixe3red.png"));
        fishes[2][3] = new Sprite(new Texture("images/characters/tubarao3.png"));
        fishes[2][4] = new Sprite(new Texture("images/characters/tubarao32.png"));

        if (seafishGame.record < 2000) {
            fishes[3][0] = new Sprite(new Texture("images/characters/sombra1.png"));
            fishes[3][1] = new Sprite(new Texture("images/characters/sombra1.png"));
        } else {
            fishes[3][0] = new Sprite(new Texture("images/characters/peixe4.png"));
            fishes[3][1] = new Sprite(new Texture("images/characters/peixe42.png"));
            fishes[3][2] = new Sprite(new Texture("images/characters/peixe4red.png"));
            fishes[3][3] = new Sprite(new Texture("images/characters/tubarao4.png"));
            fishes[3][4] = new Sprite(new Texture("images/characters/tubarao42.png"));
        }

        if (seafishGame.record < 4000) {
            fishes[4][0] = new Sprite(new Texture("images/characters/sombra2.png"));
            fishes[4][1] = new Sprite(new Texture("images/characters/sombra2.png"));
        } else {
            fishes[4][0] = new Sprite(new Texture("images/characters/peixe5.png"));
            fishes[4][1] = new Sprite(new Texture("images/characters/peixe52.png"));
            fishes[4][2] = new Sprite(new Texture("images/characters/peixe5red.png"));
            fishes[4][3] = new Sprite(new Texture("images/characters/tubarao5.png"));
            fishes[4][4] = new Sprite(new Texture("images/characters/tubarao52.png"));
        }

        if (seafishGame.record < 6000) {
            fishes[5][0] = new Sprite(new Texture("images/characters/sombra3.png"));
            fishes[5][1] = new Sprite(new Texture("images/characters/sombra3.png"));
        } else {
            fishes[5][0] = new Sprite(new Texture("images/characters/peixe6.png"));
            fishes[5][1] = new Sprite(new Texture("images/characters/peixe62.png"));
            fishes[5][2] = new Sprite(new Texture("images/characters/peixe6red.png"));
            fishes[5][3] = new Sprite(new Texture("images/characters/tubarao6.png"));
            fishes[5][4] = new Sprite(new Texture("images/characters/tubarao62.png"));
        }

        if (seafishGame.record < 8000) {
            fishes[6][0] = new Sprite(new Texture("images/characters/sombra4.png"));
            fishes[6][1] = new Sprite(new Texture("images/characters/sombra4.png"));
        } else {
            fishes[6][0] = new Sprite(new Texture("images/characters/peixe7.png"));
            fishes[6][1] = new Sprite(new Texture("images/characters/peixe72.png"));
            fishes[6][2] = new Sprite(new Texture("images/characters/peixe7red.png"));
            fishes[6][3] = new Sprite(new Texture("images/characters/tubarao7.png"));
            fishes[6][4] = new Sprite(new Texture("images/characters/tubarao72.png"));
        }
    }

    private void setTextures() {
        fishSprite = new Sprite(new Texture("images/characters/peixe1.png"));
        background[0] = new Texture("images/scenarios/fundo1.png");
        background[1] = new Texture("images/scenarios/fundo2.png");
        background[2] = new Texture("images/scenarios/fundo3.png");
        background[3] = new Texture("images/scenarios/fundo4.png");
        background[4] = new Texture("images/scenarios/fundo5.png");
        background[5] = new Texture("images/scenarios/fundo6.png");
        background[6] = new Texture("images/scenarios/fundo7.png");
        gameOverText = new Texture("images/texts/gameover.png");
        continueButton = new Texture("images/texts/continue.png");
        reload = new Texture("images/buttons/refresh.png");
        musicButton = new Texture("images/buttons/music.png");
        pauseButton = new Texture("images/buttons/pause.png");
        menuButton = new Texture("images/buttons/menu.png");
        yesButton = new Texture("images/buttons/yes.png");
        videoButton = new Texture("images/buttons/video.png");
        noButton = new Texture("images/buttons/no.png");
        setFishes();
        shoals[0] = new Sprite(new Texture("images/obstacles/piranhas.png"));
        shoals[1] = new Sprite(new Texture("images/obstacles/piranhas2.png"));
        sharks[0] = new Sprite(new Texture("images/obstacles/tubaraoinimigo2.png"));
        sharks[1] = new Sprite(new Texture("images/obstacles/tubaraoinimigo22.png"));
        pollution[0] = new Sprite(new Texture("images/obstacles/baiacu.png"));
        pollution[1] = new Sprite(new Texture("images/obstacles/canudo.png"));
        pollution[2] = new Sprite(new Texture("images/obstacles/garrafa.png"));
        pollution[3] = new Sprite(new Texture("images/obstacles/pneu.png"));
        pollution[4] = new Sprite(new Texture("images/obstacles/plastico.png"));
        hooks[0] = new Sprite(new Texture("images/obstacles/anzol1.png"));
        hooks[1] = new Sprite(new Texture("images/obstacles/anzol2.png"));
        hooks[2] = new Sprite(new Texture("images/obstacles/anzol3.png"));
        hooks[3] = new Sprite(new Texture("images/obstacles/anzol4.png"));
        for (int i = 0; i < wormScore.length; i++) {
            wormScore[i] = new Sprite(new Texture("images/elements/minhoca.png"));
        }
        wormBonus = new Sprite(new Texture("images/elements/minhocabonus.png"));
        ornaments[0] = new Texture("images/ornaments/enfeite1.png");
        ornaments[1] = new Texture("images/ornaments/enfeite2.png");
        ornaments[2] = new Texture("images/ornaments/enfeite3.png");
        ornaments[3] = new Texture("images/ornaments/enfeite4.png");
        ornaments[4] = new Texture("images/ornaments/enfeite5.png");
        ornaments[5] = new Texture("images/ornaments/enfeite6.png");
        ornaments[6] = new Texture("images/ornaments/enfeite7.png");
    }

    private void setSeaweed() {
        seaweeds = new Sprite[3];
        seaweeds[0] = new Sprite(new Texture("images/ornaments/alga1.png"));
        seaweeds[0].setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) / 10, -seaweeds[0].getHeight() / 5);
        seaweeds[0].setSize(seaweeds[0].getWidth() * seafishGame.adjustWidth, seaweeds[0].getHeight() * seafishGame.adjustHeight);

        seaweeds[1] = new Sprite(new Texture("images/ornaments/algaVaria.png"));
        seaweeds[1].setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) / 10, -seaweeds[1].getHeight() / 5);
        seaweeds[1].setSize(seaweeds[1].getWidth() * seafishGame.adjustWidth, seaweeds[1].getHeight() * seafishGame.adjustHeight);

        seaweeds[2] = new Sprite(new Texture("images/ornaments/alga2.png"));
        seaweeds[2].setPosition((seafishGame.width - seafishGame.differenceBetweenWidth) / 2, -seaweeds[2].getHeight() / 5);
        seaweeds[2].setSize(seaweeds[2].getWidth() * seafishGame.adjustWidth, seaweeds[2].getHeight() * seafishGame.adjustHeight);
    }

    private void setButtons() {
        menuSprite = new Sprite(menuButton);
        menuSprite.setSize(menuSprite.getWidth() * seafishGame.adjustWidth, menuSprite.getHeight() * seafishGame.adjustHeight);
        menuSprite.setPosition(((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (menuButton.getWidth() * seafishGame.adjustWidth / 2), (float) ((seafishGame.height / 3) - menuSprite.getHeight() * seafishGame.adjustHeight * 1.5));

        yesSprite = new Sprite(yesButton);
        yesSprite.setSize(yesSprite.getWidth() * seafishGame.adjustWidth, yesSprite.getHeight() * seafishGame.adjustHeight);
        yesSprite.setPosition((float) (((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (yesButton.getWidth() * seafishGame.adjustWidth * 1.5)), (float) ((seafishGame.height / 3) - yesSprite.getHeight() * seafishGame.adjustHeight * 1.5));

        noSprite = new Sprite(noButton);
        noSprite.setSize(noSprite.getWidth() * seafishGame.adjustWidth, noSprite.getHeight() * seafishGame.adjustHeight);
        noSprite.setPosition((((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) + (noButton.getWidth() * seafishGame.adjustWidth)), (float) ((seafishGame.height / 3) - noSprite.getHeight() * seafishGame.adjustHeight * 1.5));

        replaySprite = new Sprite(reload);
        replaySprite.setSize(150 * seafishGame.adjustWidth, 150 * seafishGame.adjustHeight);
        replaySprite.setPosition(((seafishGame.width - seafishGame.differenceBetweenWidth) / 2) - (reload.getWidth() * seafishGame.adjustWidth), seafishGame.height / 2);

        pauseSprite = new Sprite(pauseButton);
        pauseSprite.setSize(pauseButton.getWidth() * seafishGame.adjustWidth, pauseButton.getHeight() * seafishGame.adjustHeight);
        pauseSprite.setPosition(pauseButton.getWidth(), (seafishGame.height - ((float) pauseButton.getHeight() * 1.5f)));

        musicSprite = new Sprite(musicButton);
        musicSprite.setSize(musicButton.getWidth() * seafishGame.adjustWidth, musicButton.getHeight() * seafishGame.adjustHeight);
        musicSprite.setPosition(musicButton.getWidth() * 3, (seafishGame.height - ((float) pauseButton.getHeight() * 1.5f)));
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
}