package bricker.gameobjects;

import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    private final Vector2 windowsDimensions;
    private final UserInputListener inputListener;
    private float paddleWidth;

    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowsDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.setTag(Constants.PADDLE_TAG);
        this.paddleWidth = dimensions.x();
        this.inputListener = inputListener;
        this.windowsDimensions = windowsDimensions;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementsDirection = Vector2.ZERO;
        Vector2 paddleTopLeftCorner = getTopLeftCorner();
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (paddleTopLeftCorner.x() > 0) {
                movementsDirection = movementsDirection.add(Vector2.LEFT);
            } else {
                setTopLeftCorner(new Vector2(0, paddleTopLeftCorner.y()));
            }
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (paddleTopLeftCorner.x() + paddleWidth < windowsDimensions.x()) {
                movementsDirection = movementsDirection.add(Vector2.RIGHT);
            } else {
                setTopLeftCorner(new Vector2(windowsDimensions.x() - paddleWidth,
                        paddleTopLeftCorner.y()));
            }
        }
        setVelocity(movementsDirection.mult(Constants.PADDLE_SPEED));
    }
}