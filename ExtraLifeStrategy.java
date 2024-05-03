package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import bricker.gameobjects.LivesManager;
import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class ExtraLifeStrategy implements CollisionStrategy{
    private final BrickerGameManager brickerGameManager;

    private final Vector2 dimensions;

    private final Renderable renderable;
    private final LivesManager livesManager;

    public ExtraLifeStrategy(BrickerGameManager brickerGameManager, Vector2 dimensions,
                             Renderable renderable, LivesManager livesManager) {
        this.brickerGameManager = brickerGameManager;
        this.dimensions = dimensions;
        this.renderable = renderable;
        this.livesManager = livesManager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeObject(thisObj, Layer.STATIC_OBJECTS);
        Heart fallingHeart = new Heart(thisObj.getCenter(), dimensions, renderable,
                this);
        fallingHeart.setVelocity(new Vector2(0, Constants.FALLING_HEART_VELOCITY));
        brickerGameManager.addObject(fallingHeart);
    }

    public void addLife(GameObject fallingHeart) {
        brickerGameManager.removeObject(fallingHeart);
        livesManager.increaseLives();
    }
}
