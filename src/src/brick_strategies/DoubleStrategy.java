package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class DoubleStrategy extends RemoveBrickStrategyDecorator{

    private final CollisionStrategy newDecoration;
    private final GameObjectCollection gameObjectCollection;

    /**
     * Constructor
     * @param toBeDecorated CollisionStrategy that is the base of our new strategy
     * @param newDecoration
     * @param gameObjectCollection
     */
    DoubleStrategy(CollisionStrategy toBeDecorated, CollisionStrategy newDecoration,
                   GameObjectCollection gameObjectCollection){
        super(toBeDecorated);
        this.newDecoration = newDecoration;
        this.gameObjectCollection = gameObjectCollection;
    }


    /**
     * Execute the two strategies
     * @param thisObj the brick that is destroyed
     * @param otherObj the ball in collision with the brick
     * @param counter - global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        this.newDecoration.onCollision(thisObj, otherObj, counter);
    }


    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectCollection;
    }
}
