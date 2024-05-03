package bricker.gameobjects;

import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class LivesManager extends GameObject {
    private static final int PADDING = 3;
    private int heartsLeft;
    private final GameObject[] hearts;
    private final GameObjectCollection gameObjects;
    private final Renderable heartsImage;
    private TextRenderable numberText;

    public LivesManager(Vector2 topLeftCorner, int initialLivesNumber, Renderable heartsImage,
                        GameObjectCollection gameObjects) {
        super(topLeftCorner, new Vector2(
                        2 * Constants.LIFE_DATA_SIZE + 2 * PADDING,
                        Constants.MAXIMUM_HEARTS_NUMBER * (Constants.LIFE_DATA_SIZE + PADDING) + PADDING),
                null
        );
        this.heartsLeft = initialLivesNumber;
        this.hearts = new Heart[Constants.MAXIMUM_HEARTS_NUMBER];
        this.gameObjects = gameObjects;
        this.heartsImage = heartsImage;

        numberText = new TextRenderable(String.valueOf(heartsLeft));
        numberText.setColor(checkWantedColor());
        GameObject number = new GameObject(
                topLeftCorner,
                new Vector2(Constants.LIFE_DATA_SIZE, Constants.LIFE_DATA_SIZE),
                numberText
        );
        gameObjects.addGameObject(number, Layer.UI);


        for (int i = 0; i < initialLivesNumber; i++) {
            createHeart(i);
        }
    }

    public int getLivesLeft() {
        return heartsLeft;
    }

    public void decreaseLives() {
        gameObjects.removeGameObject(hearts[heartsLeft - 1], Layer.UI);
        hearts[heartsLeft - 1] = null;
        heartsLeft--;
        numberText.setString(String.valueOf(heartsLeft));
        numberText.setColor(checkWantedColor());
    }

    public void increaseLives() {
        if (heartsLeft == Constants.MAXIMUM_HEARTS_NUMBER) {
            return;
        }
        System.out.println(heartsLeft);
        System.out.println(hearts.length);
        createHeart(heartsLeft);
        gameObjects.addGameObject(hearts[heartsLeft], Layer.UI);
        heartsLeft++;
        numberText.setString(String.valueOf(heartsLeft));
        numberText.setColor(checkWantedColor());
    }

    private Color checkWantedColor() {
        if (heartsLeft >= 3) {
            return Color.green;
        } else if (heartsLeft == 2) {
            return Color.yellow;
        } else {
            return Color.red;
        }
    }

    private void createHeart(int currentHeartIndex) {
        hearts[currentHeartIndex] = new Heart(
                new Vector2(
                        getTopLeftCorner().x() + currentHeartIndex * (Constants.LIFE_DATA_SIZE + PADDING),
                        getTopLeftCorner().y() + Constants.LIFE_DATA_SIZE + 2 * PADDING
                ),
                new Vector2(Constants.LIFE_DATA_SIZE, Constants.LIFE_DATA_SIZE),
                heartsImage
        );
        gameObjects.addGameObject(hearts[currentHeartIndex], Layer.UI);
    }
}
