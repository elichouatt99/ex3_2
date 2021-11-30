package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Clock;

import java.util.Random;

public class ChangeTimeScaleStrategy extends RemoveBrickStrategyDecorator{
    private final ImageReader imageReader;
    private final WindowController windowController;
    private Renderable renderable;
    private float newTimeScale;

    private enum ClockType{QUICKEN, SLOW}
    /**
     * Constructor
     * @param toBeDecorated Collision strategy object to be decorated.
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     */
    public ChangeTimeScaleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                                   WindowController windowController) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.windowController = windowController;
    }


    /**
     * Create a object Clock that change the time scale in contact with the paddle
     * @param thisObj the brick that is destroyed
     * @param otherObj the ball in collision with the brick
     * @param counter - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Renderable quickenImage = imageReader.readImage("assets/quicken.png", true);
        Renderable slowImage = imageReader.readImage("assets/slow.png", true);
        Random random = new Random();
        int i = random.nextInt(ClockType.values().length);
        ClockType random1 = ClockType.values()[i];
        float timeScale = (float) windowController.getTimeScale();
        if (timeScale == 0.9f || (timeScale == 1f && random1 == ClockType.QUICKEN)){
            this.renderable = quickenImage;
            this.newTimeScale = 1.1f;
        }
        else if (timeScale == 1.1f || (timeScale == 1f && random1 == ClockType.SLOW)){
            this.renderable = slowImage;
            this.newTimeScale = 0.9f;
        }

        GameObject clock = new Clock(
                thisObj.getTopLeftCorner(),
                thisObj.getDimensions(),
                this.renderable,
                this.windowController,
                this.newTimeScale,
                this.getGameObjectCollection());
        clock.setVelocity(Vector2.DOWN.mult(250f));
        getGameObjectCollection().addGameObject(clock);
    }
}