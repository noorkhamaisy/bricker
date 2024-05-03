package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

public class ChangeCameraStrategy implements CollisionStrategy{
    private final BrickerGameManager brickerGameManager;
    private final WindowController windowController;
    private final Ball mainBall;

    public ChangeCameraStrategy(BrickerGameManager brickerGameManager, WindowController windowController,
                                Ball mainBall){
        this.brickerGameManager = brickerGameManager;
        this.windowController = windowController;
        this.mainBall = mainBall;

    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeObject(thisObj, Layer.STATIC_OBJECTS);
        if (otherObj.getTag().equals(Constants.MAIN_BALL_TAG) && brickerGameManager.camera() == null) {
            mainBall.resetCollisionCounter();
            brickerGameManager.setCamera(new Camera(mainBall, Vector2.ZERO,
                    windowController.getWindowDimensions().mult(1.2f),
                    windowController.getWindowDimensions()));
        }
    }
}
