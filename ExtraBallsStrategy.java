package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class ExtraBallsStrategy implements CollisionStrategy {

    private final BrickerGameManager brickerGameManager;
    private final Random random;
    private final Vector2 dimensions;
    private final Renderable renderable;
    private final Sound collisionSound;
    private final ArrayList<Ball> balls;

    public ExtraBallsStrategy(BrickerGameManager brickerGameManager, Vector2 dimensions,
                              Renderable renderable, Sound collisionSound) {
        this.random = new Random();
        this.brickerGameManager = brickerGameManager;
        this.dimensions = dimensions;
        this.renderable = renderable;
        this.collisionSound = collisionSound;
        this.balls = new ArrayList<>();
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        for (int i = 0; i < Constants.BALLS_TO_CREATE ; i++) {
            Ball ball = new Ball(thisObj.getCenter(), dimensions, renderable, collisionSound);
            ball.setVelocity(initializeBallVelocity());
            ball.setTag(Constants.PUCK_TAG);
            balls.add(ball);
            brickerGameManager.addObject(ball);
        }
        brickerGameManager.removeObject(thisObj, Layer.STATIC_OBJECTS);

    }

    private Vector2 initializeBallVelocity() {
        double angle = random.nextDouble() * Math.PI;
        return new Vector2(
                (float) Math.cos(angle) * Constants.BALL_SPEED_MUL_FACTOR,
                (float) Math.sin(angle) * Constants.BALL_SPEED_MUL_FACTOR
        );
    }
}
