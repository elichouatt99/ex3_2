package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;


/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator{

    private static final float PADDLE_WIDTH = 150f;
    private static final float OBJECT_HEIGHT = 15f;
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 10;
    private static final int NUM_COLLISION_TO_DISAPPEAR =3;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;

    /**
     * constructor
     * @param toBeDecorated CollisionStrategy that is the base of our new strategy
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param inputListener an InputListener instance for reading user input.
     * @param windowDimensions pixel dimensions for game window height x width
     */
    public AddPaddleStrategy(CollisionStrategy toBeDecorated,
                              ImageReader imageReader,
                              UserInputListener inputListener,
                              Vector2 windowDimensions){
        super(toBeDecorated);

        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }


    /**
     * Adds additional paddle to game and delegates to held object.
     * @param thisObj the brick that is destroyed
     * @param otherObj the ball in collision with the brick
     * @param counter - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (!MockPaddle.isInstantiated){
            Renderable mockPaddleImage = imageReader.readImage("assets/paddle.png", false);
            GameObject mockPaddle = new MockPaddle(
                    Vector2.ZERO,
                    new Vector2(PADDLE_WIDTH, OBJECT_HEIGHT),
                    mockPaddleImage,
                    inputListener,
                    windowDimensions,
                    getGameObjectCollection(),
                    MIN_DISTANCE_FROM_SCREEN_EDGE,
                    NUM_COLLISION_TO_DISAPPEAR
            );
            mockPaddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2));
            getGameObjectCollection().addGameObject(mockPaddle);
        }

    }
}
