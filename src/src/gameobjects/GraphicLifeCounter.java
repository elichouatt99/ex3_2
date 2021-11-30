package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Display a graphic object on the game window showing as many widgets as lives left.
 */
public class GraphicLifeCounter extends GameObject {

    private final Counter livesCounter;
    private final GameObjectCollection gameObjectsCollection;
    private int numOfLives;
    private final GameObject[] hearts;

    /**
     * constructor
     * @param widgetTopLeftCorner  top left corner of left most life widgets.
     *                             Other widgets will be displayed to its right, aligned in hight.
     * @param widgetDimensions dimensions of widgets to be displayed.
     * @param livesCounter  global lives counter of game.
     * @param widgetRenderable  image to use for widgets.
     * @param gameObjectsCollection global game object collection managed by game manager.
     * @param numOfLives global setting of number of lives a player will have in a game.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable, GameObjectCollection gameObjectsCollection, int numOfLives){
        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.livesCounter = livesCounter;
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
        this.hearts = new GameObject[numOfLives];
        for (int i=0; i < numOfLives; i++){
            this.hearts[i] = new GameObject(widgetTopLeftCorner.add(new Vector2(35*i,0)), widgetDimensions, widgetRenderable);
            this.gameObjectsCollection.addGameObject(this.hearts[i],Layer.BACKGROUND);
        }
    }


    /**
     *
     * @param deltaTime  time between updates.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.livesCounter.value() < this.numOfLives){
            this.gameObjectsCollection.removeGameObject(this.hearts[livesCounter.value()], Layer.BACKGROUND);
            this.numOfLives--;
        }
    }
}
