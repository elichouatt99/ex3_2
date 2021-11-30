package src.brick_strategies;


import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

import java.util.Random;

import static java.lang.Math.max;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces several pucks instead of brick once removed.
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator{

    private static final int NUM_PUNK = 3;
    private static final int INVERSE_DIRECTION = -1;
    Renderable puckImage;
    Sound collisonSound;

    /**
     * @param toBeDecorated CollisionStrategy that is the base of our new strategy
     * @param imageReader an ImageReader instance for reading images from files for rendering of objects.
     * @param soundReader a SoundReader instance for reading soundclips from files for rendering event sounds.
     */
    public PuckStrategy(CollisionStrategy toBeDecorated,
                        ImageReader imageReader,
                        SoundReader soundReader) {
        super(toBeDecorated);
        puckImage = imageReader.readImage("assets/mockBall.png", true);
        collisonSound = soundReader.readSound("assets/Bubble5_4.wav"); //TODO verifier le son
    }

    /**
     * Add pucks to game on collision and delegate to held CollisionStrategy.
     * @param thisObj the brick that is destroyed
     * @param otherObj the ball in collision with the brick
     * @param counter - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Puck[] pucks = new Puck[3];
        for (int i = 0; i < NUM_PUNK; i++) {
            float size = (float) max((int) thisObj.getDimensions().x(), (int) thisObj.getDimensions().y())/3;
            pucks[i] = new Puck(new Vector2(thisObj.getTopLeftCorner().x() + (1 + 2 * i) * thisObj.getDimensions().x() / 6,
                    thisObj.getTopLeftCorner().y() - thisObj.getDimensions().y() / 2),
                    new Vector2(size, size),
                    puckImage,
                    collisonSound);
            pucks[i].setVelocity(otherObj.getVelocity());
            float ballVelX = pucks[i].getVelocity().x();
            float ballVelY = pucks[i].getVelocity().y();
            Random rand = new Random();
            if (rand.nextBoolean()) {
                ballVelX *= INVERSE_DIRECTION;
            }
            pucks[i].setVelocity(new Vector2(ballVelX, ballVelY));
            this.getGameObjectCollection().addGameObject(pucks[i]);
        }
    }
}
