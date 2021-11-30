//quand j'utilisais j'appellais le constructor CollisionStrategie, je dois maintenant appeler le constructor
// Remove... tout en laissant la reference en tant que CollisionStrategie

package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * Concrete brick strategy implmenting CollisionStrategy interface. Removes holding brick on collision.
 */
public class RemoveBrickStrategy extends Object implements CollisionStrategy{

    private final GameObjectCollection gameObjectCollection;

    /**
     * Constructor
     * @param gameObjectCollection global game object collection
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection){
        this.gameObjectCollection = gameObjectCollection;
    }

    /**
     * Removes brick from game object collection on collision.
     * @param thisObj the brick that is destroyed
     * @param otherObj the ball in collision with the brick
     * @param counter - global brick counter.
     *
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        if (gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)){
            counter.decrement();
        }
    }

    /**
     * All collision strategy objects should hold a reference to the global game object collection
     * and be able to return it.
     * @return global game object collection whose reference is held in object.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectCollection;
    }
}
