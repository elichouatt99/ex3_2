package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;

public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator{

    private WindowController windowController;
    private BrickerGameManager gameManager;
    private BallCollisionCountdownAgent ballCollisionCountdownAgent;
    private Ball ball;

    /**
     * Constructor
     * @param toBeDecorated CollisionStrategy that is the base of our new strategy
     * @param windowController controls visual rendering of the game window and object renderables.
     * @param gameManager
     */
    ChangeCameraStrategy(CollisionStrategy toBeDecorated, WindowController windowController,
                         BrickerGameManager gameManager){
        super(toBeDecorated);
        this.windowController = windowController;
        this.gameManager = gameManager;

    }

    /**
     * Change camere position on collision and delegate to held CollisionStrategy.
     * @param thisObj the brick that is destroyed
     * @param otherObj the ball in collision with the brick
     * @param counter - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        for (GameObject gameObject : getGameObjectCollection().objectsInLayer(Layer.DEFAULT))
        {
            if (gameObject.getClass() == Ball.class){
                this.ball = (Ball) gameObject;
            }
        }
        if (gameManager.getCamera() == null){
            gameManager.setCamera(
                    new Camera(
                            this.ball,
                            Vector2.ZERO, 	//follow the center of the object
                            windowController.getWindowDimensions().mult(1.2f),  //widen the frame a bit
                            windowController.getWindowDimensions()   //share the window dimensions
                    )
            );
            ballCollisionCountdownAgent = new BallCollisionCountdownAgent(this.ball, this, 4);
            getGameObjectCollection().addGameObject(ballCollisionCountdownAgent);
        }

    }

    /**
     * Return camera to normal ground position.
     */
    public void turnOffCameraChange(){
        gameManager.setCamera(null);
        getGameObjectCollection().removeGameObject(ballCollisionCountdownAgent);
    }
}