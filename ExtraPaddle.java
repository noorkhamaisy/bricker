package bricker.gameobjects;

import bricker.brick_strategies.ExtraPaddleStrategy;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddle extends Paddle {
    private final ExtraPaddleStrategy collisionStrategy;
    private Counter extraPaddleLivesCounter;

    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowsDimensions,
                       ExtraPaddleStrategy collisionStrategy, Counter extraPaddleLivesCounter) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowsDimensions);
//        System.out.println("[DEBUG] ExtraPaddle created");
        this.setTag(Constants.EXTRA_PADDLE_TAG);
        this.collisionStrategy = collisionStrategy;
        this.extraPaddleLivesCounter = extraPaddleLivesCounter;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (!other.getTag().equals(Constants.WALL_TAG)) {
            extraPaddleLivesCounter.decrement();
            if (extraPaddleLivesCounter.value() == 0) {
                collisionStrategy.deleteExtraPaddle();
            }
        }
    }
}
