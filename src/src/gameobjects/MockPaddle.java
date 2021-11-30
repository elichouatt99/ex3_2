package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class MockPaddle extends Paddle{

    public static boolean isInstantiated = false;
    private final GameObjectCollection gameObjectCollection;
    private int numCollisionToDisappear;

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param renderable The renderable representing the object. Can be null, in which case
     * @param inputListener listener object for user input.
     * @param windowDimensions  dimensions of game window.
     * @param minDistanceFromEdge border for paddle movement
     */
    public MockPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
                      Vector2 windowDimensions, GameObjectCollection gameObjectCollection, int minDistanceFromEdge,
                      int numCollisionToDisappear) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.gameObjectCollection = gameObjectCollection;
        this.numCollisionToDisappear = numCollisionToDisappear;
        isInstantiated = true;
    }

    /**
     *
     * @param other object that enter in collision with the mock paddle
     * @param collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (--this.numCollisionToDisappear == 0){
            this.gameObjectCollection.removeGameObject(this);
            isInstantiated = false;
        }
    }
}
