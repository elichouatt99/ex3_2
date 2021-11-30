package src;


import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.brick_strategies.BrickStrategyFactory;
import src.gameobjects.*;

import java.util.Random;


/**
 * this class is responsible for game initialization, holding references for game objects and calling 
 */
public class BrickerGameManager extends GameManager {

    private static final float BALL_SPEED = 200f;
    private static final float MIDDLE = 0.5f;
    private static final int NUM_OF_ROW = 5;
    private static final int NUM_BY_ROW = 8;
    private static final float BORDER_WIDTH = 5f;
    private static final int NUM_OF_LIVES = 4;
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 5;
    private static final int  WINDOW_DIMENSION_X = 700;
    private static final int  WINDOW_DIMENSION_Y = 500;
    private static final int  INVERSE_DIRECTION = -1;
    private static final float  PADDLE_WIDTH = 150f;
    private static final float  OBJECT_HEIGHT = 15f;
    private static final float  BALL_DIM = 20f;
    private static final float SPACE_BTW = 1f;
    private static final float HEART_DIM = 30f;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private final Counter brick_counter;
    private final Counter livesCounter;
    private final int numberOfLives;


    /**
     * Constructor
     * @param windowTitle
     * @param windowDimensions pixel dimensions for game window height x width
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);
        brick_counter = new Counter();
        numberOfLives = NUM_OF_LIVES;
        livesCounter = new Counter();

    }

    private void createBall(ImageReader imageReader, SoundReader soundReader){
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisonSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIM,BALL_DIM), ballImage, collisonSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        ball.setCenter(windowDimensions.mult(MIDDLE));
        gameObjects().addGameObject(ball);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()){
            ballVelX *= INVERSE_DIRECTION;
        }
        if (rand.nextBoolean()){
            ballVelY *= INVERSE_DIRECTION;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    private void createBricks(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener){
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory(this.gameObjects(), this,
                imageReader, soundReader, inputListener , windowController, windowDimensions );
        for (int i=0; i < NUM_OF_ROW; i++){
            for (int j=0; j < NUM_BY_ROW; j++) {
                float topLeftCornerX = BORDER_WIDTH + ((windowDimensions.x() - 2*BORDER_WIDTH) * j / NUM_BY_ROW);
                float topLeftCornerY = (OBJECT_HEIGHT+SPACE_BTW)*i + BORDER_WIDTH;
                gameObjects().addGameObject(
                        new Brick(
                                new Vector2(topLeftCornerX, topLeftCornerY),
                                new Vector2((windowDimensions.x()-2*BORDER_WIDTH-NUM_BY_ROW)/NUM_BY_ROW, OBJECT_HEIGHT),
                                brickImage,
                                brickStrategyFactory.getStrategy(),
                                brick_counter
                        ), Layer.STATIC_OBJECTS
                );
            }
        }
    }

    private void creatPaddle(ImageReader imageReader, UserInputListener inputListener){
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, OBJECT_HEIGHT), paddleImage, inputListener,
                windowDimensions, MIN_DISTANCE_FROM_SCREEN_EDGE);
        paddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()-OBJECT_HEIGHT-15));
        gameObjects().addGameObject(paddle);

    }

    private void createRoof(){
        gameObjects().addGameObject(
                new GameObject(
                        //anchored at top-left corner of the screen
                        Vector2.ZERO,

                        //height of border is the height of the screen
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),

                        //this game object is invisible; it doesn’t have a Renderable
                        null
                )
        );
    }

    private void createWall(){
        int[] wallsX = {0, (int)(windowDimensions.x() - BORDER_WIDTH)};
        for (int x : wallsX) {
            gameObjects().addGameObject(
                    new GameObject(
                            //anchored at top-left corner of the screen
                            new Vector2(x, 0),

                            //height of border is the height of the screen
                            new Vector2(BORDER_WIDTH, windowDimensions.y()),

                            //this game object is invisible; it doesn’t have a Renderable
                            null
                    )
            );
        }
    }

    private void createGraphicLifeCounter(ImageReader imageReader){
        Renderable heart = imageReader.readImage("assets/heart.png", true);
        gameObjects().addGameObject(
                new GraphicLifeCounter(
                        new Vector2(Vector2.ZERO.x(), windowDimensions.y()-HEART_DIM),
                        new Vector2(HEART_DIM,HEART_DIM),
                        livesCounter,
                        heart,
                        this.gameObjects(),
                        this.numberOfLives),
                Layer.BACKGROUND);
    }

    private void createNumericLifeCounter(){
        gameObjects().addGameObject(
                new NumericLifeCounter(
                        livesCounter,
                        new Vector2(windowDimensions.x()-15*10, windowDimensions.y()-25),
                        new Vector2(OBJECT_HEIGHT, OBJECT_HEIGHT),
                        this.gameObjects()
                ),
                Layer.BACKGROUND
        );
    }

    private void createBackground(ImageReader imageReader){
        GameObject background = new GameObject(
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage("assets/DARK_BG2_small.jpeg",false)
        );
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }


    /**
     *
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param soundReader a SoundReader instance for reading soundclips from files for rendering event sounds.
     * @param inputListener an InputListener instance for reading user input.
     * @param windowController controls visual rendering of the game window and object renderables.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                                UserInputListener inputListener, WindowController windowController){
        this.windowController = windowController;
        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        this.brick_counter.reset();
        this.livesCounter.reset();
        this.livesCounter.increaseBy(NUM_OF_LIVES);

        //create ball
        createBall(imageReader, soundReader);

        //create paddle
        creatPaddle(imageReader, inputListener);

        //create walls
        createWall();

        //create roof
        createRoof();

        //create bricks
        createBricks(imageReader, soundReader, inputListener);

        //create background
        createBackground(imageReader);

        //create NumericLifeCounter
        createNumericLifeCounter();

        //create graphicLifeCounter
        createGraphicLifeCounter(imageReader);

    }

    /**
     *
     * @param deltaTime time between updates. For internal use by game engine. You do not need to call this
     *                  method yourself.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForEndGame();
        for (GameObject object: gameObjects().objectsInLayer(Layer.DEFAULT)){
            if (object instanceof Ball){
                repositionBall(object);
            }
        }

    }

    private void checkForEndGame() {
        float ballHeight = ball.getCenter().y();
        String prompt= "";
        if (brick_counter.value() == 0){
            prompt = "you win!";
        }
        if (ballHeight > windowDimensions.y()){
            livesCounter.decrement();
            if (livesCounter.value() == 0){
                prompt = "you lose!";
            }
            else{
                ball.setCenter(windowDimensions.mult(MIDDLE));            }
        }
        if (!prompt.isEmpty()){
            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            }
            else
                windowController.closeWindow();
        }
    }

    public void repositionBall(GameObject ball){
        if (ball.getCenter().x() > windowDimensions.x()){
            ball.setCenter(new Vector2(windowDimensions.x()-5,ball.getCenter().y()));
            ball.setVelocity(new Vector2(-ball.getVelocity().x(), ball.getVelocity().y()));
        }
        if (ball.getCenter().x() < 0){
            ball.setCenter(new Vector2(5, ball.getCenter().y()));
            ball.setVelocity(new Vector2(-ball.getVelocity().x(), ball.getVelocity().y()));
        }
        if (ball.getCenter().y() < 0){
            ball.setCenter(new Vector2(ball.getCenter().x(), 5));
            ball.setVelocity(new Vector2(ball.getVelocity().x(), -ball.getVelocity().y()));
        }
        if (ball.getClass() == Puck.class && ball.getCenter().y() > windowDimensions.y()){
            gameObjects().removeGameObject(ball);
        }
    }

    /**
     * Entry point for game. Should contain:
     * 1. An instantiation call to BrickerGameManager constructor.
     * 2. A call to run() method of instance of BrickerGameManager.
     * @param args command line arguments
     */
    public static void main(String[] args){
        new BrickerGameManager("Bricker", new Vector2(WINDOW_DIMENSION_X, WINDOW_DIMENSION_Y)).run();
    }

}
