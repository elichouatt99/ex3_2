package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Clock extends GameObject {

    private WindowController windowController;
    private float newTimeScale;
    private GameObjectCollection gameObjectCollection;

    public Clock(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, WindowController windowController,
                 float newTimeScale, GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable);
        this.windowController = windowController;
        this.newTimeScale = newTimeScale;
        this.gameObjectCollection = gameObjectCollection;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && other.getTag().equals("paddle");
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (shouldCollideWith(other)){
            windowController.setTimeScale(this.newTimeScale);
            gameObjectCollection.removeGameObject(this);

        }
    }
}
