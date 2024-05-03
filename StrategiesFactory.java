package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.LivesManager;
import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

public class StrategiesFactory {
    private static Random random;
    private final BrickerGameManager brickerGameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final WindowController windowController;
    private final Ball mainBall;
    private final LivesManager livesManager;
    private final Counter extraPaddleLivesCounter;

    public StrategiesFactory(BrickerGameManager brickerGameManager, ImageReader imageReader,
                             SoundReader soundReader, UserInputListener inputListener,
                             WindowController windowController, Ball mainBall, LivesManager livesManager,
                             Counter extraPaddleLivesCounter) {
        random = new Random();
        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.mainBall = mainBall;
        this.livesManager = livesManager;
        this.extraPaddleLivesCounter = extraPaddleLivesCounter;
    }
    public CollisionStrategy createStrategy(int currentDoublePowerDepth) {
        if (random.nextBoolean()) {
            System.out.println("[DEBUG] BasicCollisionStrategy chosen.");
            return new BasicCollisionStrategy(brickerGameManager);
        } else {
            return randomizeSpecialStrategies(currentDoublePowerDepth);
        }
    }

    public CollisionStrategy randomizeSpecialStrategies(int currentDoublePowerDepth) {
        CollisionStrategy chosenStrategy = null;
        boolean doublePowerChosen = false;
        while (chosenStrategy == null) {
            int randomStrategy = random.nextInt(Constants.SPECIAL_STRATEGIES_NUMBER);
            switch (randomStrategy) {
                case Constants.EXTRA_BALLS_STRATEGY_CHOSEN:
                    chosenStrategy = createExtraBallStrategy();
                    break;
                case Constants.EXTRA_PADDLE_STRATEGY_CHOSEN:
                    chosenStrategy = createExtraPaddleStrategy();
                    break;
                case Constants.CHANGE_CAMERA_STRATEGY_CHOSEN:
                    chosenStrategy = createChangeCameraStrategy();
                    break;
                case Constants.EXTRA_LIFE_STRATEGY_CHOSEN:
                    chosenStrategy = createExtraLifeStrategy();
                    break;
                case Constants.DOUBLE_POWER_STRATEGY_CHOSEN:
                    doublePowerChosen = true;
                    if (currentDoublePowerDepth < Constants.DOUBLE_POWER_MAXIMUM_DEPTH) {
                        chosenStrategy = createDoublePowerStrategy(currentDoublePowerDepth);
                    }
                    break;
                default:
                    chosenStrategy = null;
            }
        }
        System.out.println("[DEBUG] Chosen strategy: " + chosenStrategy.getClass().getSimpleName());
        System.out.println("[DEBUG] Current double power depth: " + currentDoublePowerDepth);
        return chosenStrategy;
    }

    private CollisionStrategy createExtraBallStrategy() {
        return new ExtraBallsStrategy(
                brickerGameManager,
                new Vector2(mainBall.getDimensions().mult(Constants.PUCK_SIZE_FACTOR)),
                imageReader.readImage(Constants.PUCK_IMAGE_PATH, true),
                soundReader.readSound(Constants.COLLISION_SOUND_PATH)
        );
    }

    private CollisionStrategy createExtraPaddleStrategy() {
        return new ExtraPaddleStrategy(
                brickerGameManager,
                new Vector2(
                        windowController.getWindowDimensions().mult(
                                Constants.PADDLE_LOCATION_MUL_FACTOR
                        )
                ),
                new Vector2(Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT),
                imageReader.readImage(Constants.PADDLE_IMAGE_PATH, true),
                inputListener,
                windowController.getWindowDimensions(),
                extraPaddleLivesCounter
        );
    }

    private CollisionStrategy createChangeCameraStrategy() {
        return new ChangeCameraStrategy(brickerGameManager, windowController, mainBall);
    }
    private CollisionStrategy createExtraLifeStrategy() {
        return new ExtraLifeStrategy(
                brickerGameManager,
                new Vector2(Constants.LIFE_DATA_SIZE, Constants.LIFE_DATA_SIZE),
                imageReader.readImage(Constants.HEARTS_IMAGE_PATH, true),
                livesManager
        );
    }

    private CollisionStrategy createDoublePowerStrategy(int currentDoublePowerDepth) {
        return new DoublePowerStrategy(
                brickerGameManager,
                this,
                currentDoublePowerDepth
        );
    }
}
