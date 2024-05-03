package bricker.gameobjects;

import bricker.brick_strategies.ExtraLifeStrategy;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Heart extends GameObject {
    private final ExtraLifeStrategy extraLifeStrategy;

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.setTag(Constants.LIFE_TAG);
        this.extraLifeStrategy = null;
    }

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 ExtraLifeStrategy extraLifeStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.setTag(Constants.LIFE_TAG);
        this.extraLifeStrategy = extraLifeStrategy;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Constants.PADDLE_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        extraLifeStrategy.addLife(this);
    }
}
