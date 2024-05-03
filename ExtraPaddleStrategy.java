package bricker.brick_strategies;

import bricker.gameobjects.ExtraPaddle;
import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddleStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;
    private ExtraPaddle extraPaddle = null;
    private Vector2 initialTopLeftCorner;
    private final Vector2 dimensions;
    private final Renderable renderable;
    private final UserInputListener inputListener;
    private final Vector2 windowsDimensions;
    private final Counter extraPaddleLivesCounter;

    public ExtraPaddleStrategy(BrickerGameManager brickerGameManager, Vector2 topLeftCorner,
                               Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
                               Vector2 windowsDimensions, Counter extraPaddleLivesCounter){
        this.brickerGameManager = brickerGameManager;
        this.initialTopLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.renderable = renderable;
        this.inputListener = inputListener;
        this.windowsDimensions = windowsDimensions;
        this.extraPaddleLivesCounter = extraPaddleLivesCounter;

    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeObject(thisObj, Layer.STATIC_OBJECTS);
        if (extraPaddleLivesCounter.value() == 0) {
            this.extraPaddle = new ExtraPaddle(initialTopLeftCorner, dimensions, renderable, inputListener,
                    windowsDimensions, this, extraPaddleLivesCounter);
            brickerGameManager.addObject(extraPaddle);
            extraPaddleLivesCounter.increaseBy(Constants.EXTRA_PADDLE_MAXIMUM_COLLISIONS);
        }
    }

    public void deleteExtraPaddle() {
//        System.out.println("[DEBUG] ExtraPaddle deleted");
        brickerGameManager.removeObject(extraPaddle);
        extraPaddle = null;
    }
}
