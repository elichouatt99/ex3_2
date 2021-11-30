package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Factory class for creating Collision strategies
 */
public class BrickStrategyFactory extends Object{

    private enum Strategy {REMOVE_BRICK_STRATEGY, PUCK, CAMERA, TIME, PADDLE, DOUBLE}
    private final GameObjectCollection gameObjectCollection;
    private final BrickerGameManager gameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final WindowController windowController;
    private final Vector2 windowDimensions;
    private final RemoveBrickStrategy removeBrickStrategy;
    private int level;

    /**
     * constructor
     * @param gameObjectCollection
     * @param gameManager
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param soundReader a SoundReader instance for reading soundclips from files for rendering event sounds.
     * @param inputListener an InputListener instance for reading user input.
     * @param windowController controls visual rendering of the game window and object renderables.
     * @param windowDimensions pixel dimensions for game window height x width
     */
    public BrickStrategyFactory(GameObjectCollection gameObjectCollection,
                                BrickerGameManager gameManager,
                                ImageReader imageReader,
                                SoundReader soundReader,
                                UserInputListener inputListener,
                                WindowController windowController,
                                Vector2 windowDimensions){
        this.removeBrickStrategy = new RemoveBrickStrategy(gameObjectCollection);
        this.gameObjectCollection = gameObjectCollection;
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        this.level = 0;
    }

    /**
     * method randomly selects between 5 strategies and returns one CollisionStrategy object which is a
     * RemoveBrickStrategy decorated by one of the decorator strategies, or decorated by two randomly selected
     * strategies, or decorated by one of the decorator strategies and a pair of additional two decorator strategies.
     * @return CollisionStrategy object.
     */
    public CollisionStrategy getStrategy(){
        //choose randomly between the possible brick strategies
        Random random = new Random();
        int i;
        if (level == 0){i = random.nextInt(Strategy.values().length);}
        else if (level == 1){i = 1 + random.nextInt(Strategy.values().length-1);}
        else {i = 1 + random.nextInt(Strategy.values().length-2);}
        switch (Strategy.values()[i]){
            case REMOVE_BRICK_STRATEGY:
                return removeBrickStrategy;
            case PUCK:
                return new PuckStrategy(removeBrickStrategy, imageReader, soundReader);
            case CAMERA:
                return new ChangeCameraStrategy(removeBrickStrategy, windowController, gameManager);
            case TIME:
                return new ChangeTimeScaleStrategy(removeBrickStrategy, imageReader, windowController);
            case PADDLE:
                return new AddPaddleStrategy(removeBrickStrategy, imageReader, inputListener, windowDimensions);
            case DOUBLE:
                this.level ++;
                return new DoubleStrategy(getStrategy(), getStrategy(), gameObjectCollection);
            default:
                return null;
        }
    }
}
