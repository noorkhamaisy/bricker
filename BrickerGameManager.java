package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {
    private Random random;
    private WindowController windowController;
    private int bricksInRow = Constants.DEFAULT_NUMBER_OF_BRICKS_IN_ROW;
    private int bricksInColumn = Constants.DEFAULT_NUMBER_OF_BRICKS_IN_COLUMN;
    private Ball mainBall;
    private LivesManager livesManager;
    private Counter brickCounter;
    private Counter extraPaddleLivesCounter;
    private UserInputListener inputListener;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksInRow,
                              int bricksInColumn) {
        super(windowTitle, windowDimensions);
        this.bricksInRow = bricksInRow;
        this.bricksInColumn = bricksInColumn;
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.random = new Random();
        this.windowController = windowController;
        this.windowController.setTargetFramerate(Constants.FRAME_RATE);
        this.brickCounter = new Counter(bricksInRow * bricksInColumn);
        this.inputListener = inputListener;
        this.extraPaddleLivesCounter = new Counter(0);

        //initializing background
        initializeBackground(imageReader);

        //initializing lives
        initializeLivesManager(imageReader);

        //initializing ball
        initializeBall(imageReader, soundReader);

        //initializing user paddle
        initializePaddle(imageReader, inputListener);

        //initializing walls
        buildWalls();

        //initializing bricks
        initializeBricks(imageReader,soundReader, bricksInRow, bricksInColumn);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            new BrickerGameManager(Constants.GAME_NAME,
                    new Vector2(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT)).run();
        } else {
            int bricksInRow = Integer.parseInt(args[0]);
            int bricksInColumn = Integer.parseInt(args[1]);
            new BrickerGameManager(Constants.GAME_NAME,
                    new Vector2(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT),
                    bricksInRow, bricksInColumn).run();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
        updateExtraBallsStrategy();
        updateChangeCameraStrategy();
        updateExtraLifeStrategy();
    }

    public void removeObject(GameObject object) {
        boolean wasRemoved = gameObjects().removeGameObject(object);
        updateCounters(object, wasRemoved);
    }

    public void removeObject(GameObject object, int layer) {
        boolean wasRemoved = gameObjects().removeGameObject(object, layer);
        updateCounters(object, wasRemoved);
    }

    public void addObject(GameObject object) {
        gameObjects().addGameObject(object);
    }

    public void addObject(GameObject object, int layer) {
        gameObjects().addGameObject(object, layer);
    }

    private void initializeBackground(ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage(Constants.BACKGROUND_IMAGE_PATH,
                false);
        Background background = new Background(Vector2.ZERO,
                new Vector2(windowController.getWindowDimensions()),
                backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        addObject(background, Layer.BACKGROUND);
    }

    private Vector2 initializeBallVelocity() {
        float ballVelocityX = Constants.BALL_SPEED_MUL_FACTOR;
        float ballVelocityY = Constants.BALL_SPEED_MUL_FACTOR;
        if(random.nextBoolean()){
            ballVelocityX *= -1;
        }
        if(random.nextBoolean()){
            ballVelocityY *= -1;
        }
        return new Vector2(ballVelocityX, ballVelocityY);
    }

    private void resetBall() {
        mainBall.setCenter(new Vector2(windowController.getWindowDimensions().mult(0.5f)));
        mainBall.setVelocity(initializeBallVelocity());
    }

    private void initializeLivesManager(ImageReader imageReader){
        Renderable heartImage = imageReader.readImage(Constants.HEARTS_IMAGE_PATH,true);
        livesManager = new LivesManager(
                new Vector2(Constants.HEARTS_X_COORDINATE, Constants.HEARTS_Y_COORDINATE),
                Constants.INITIAL_HEARTS_NUMBER,
                heartImage,
                gameObjects()
        );
    }

    private void initializeBall(ImageReader imageReader, SoundReader soundReader){
        Renderable ballImage = imageReader.readImage(Constants.BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(Constants.COLLISION_SOUND_PATH);
        mainBall = new Ball(Vector2.ZERO, new Vector2(Constants.BALL_RADIUS, Constants.BALL_RADIUS),
                ballImage, collisionSound);
        mainBall.setTag(Constants.MAIN_BALL_TAG);
        resetBall();
        addObject(mainBall);
    }

    private void initializePaddle(ImageReader imageReader,UserInputListener inputListener){
        Renderable paddleImage = imageReader.readImage(Constants.PADDLE_IMAGE_PATH,
                true);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(Constants.PADDLE_WIDTH,
                Constants.PADDLE_HEIGHT), paddleImage,
                inputListener, new Vector2(windowController.getWindowDimensions()));
        paddle.setCenter(new Vector2(
                windowController.getWindowDimensions().x() / 2,
                windowController.getWindowDimensions().y() - Constants.PADDLE_HEIGHT - 15
        ));
        addObject(paddle);
    }

    private void initializeBricks(ImageReader imageReader, SoundReader soundReader, int bricksInRow,
                                  int bricksInColumn) {
        Renderable brickImage = imageReader.readImage(Constants.BRICK_IMAGE_PATH, false);
        float brickWidth = (windowController.getWindowDimensions().x() -
                Constants.BRICKS_PADDING * (bricksInRow + 1)) / bricksInRow;
        Vector2 brickDimensions = new Vector2(brickWidth, Constants.BRICK_HEIGHT);

        StrategiesFactory strategiesFactory = new StrategiesFactory(this, imageReader,
                soundReader, inputListener, windowController, mainBall, livesManager,
                extraPaddleLivesCounter);

        for (int i = 0; i < bricksInRow; i++) {
            for (int j = 0; j < bricksInColumn; j++) {
                Vector2 currentTopLeftCorner = new Vector2(
                        i * (brickWidth + Constants.BRICKS_PADDING) + Constants.BRICKS_PADDING,
                        j * (Constants.BRICK_HEIGHT + Constants.BRICKS_PADDING) + Constants.BRICKS_PADDING);

                Brick currentBrick = new Brick(
                        currentTopLeftCorner,
                        brickDimensions,
                        brickImage,
                        strategiesFactory.createStrategy(0)
                );
                addObject(currentBrick, Layer.STATIC_OBJECTS);
            }
        }
    }

    private void buildWalls() {
        GameObject[] walls = new GameObject[] {
                new GameObject(
                        new Vector2(-Constants.WALL_WIDTH, 0),
                        new Vector2(Constants.WALL_WIDTH,
                                windowController.getWindowDimensions().y()),
                        new RectangleRenderable(Color.CYAN)),
                new GameObject(
                        new Vector2(-Constants.WALL_WIDTH, -Constants.WALL_WIDTH),
                        new Vector2(
                                windowController.getWindowDimensions().x() + 2 * Constants.WALL_WIDTH,
                                Constants.WALL_WIDTH
                        ), new RectangleRenderable(Color.CYAN)),
                new GameObject(Vector2.RIGHT.mult(windowController.getWindowDimensions().x()),
                        new Vector2(Constants.WALL_WIDTH, windowController.getWindowDimensions().y()),
                        new RectangleRenderable(Color.CYAN))
        };
        for(GameObject wall: walls) {
            wall.setTag(Constants.WALL_TAG);
            addObject(wall, Layer.STATIC_OBJECTS);
        }
    }

    private void checkForGameEnd() {
        float ballHeight = mainBall.getCenter().y();
        String gameEndPrompt = "";
        if(brickCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.VK_W) ){
            gameEndPrompt += Constants.WIN_MESSAGE;
        }
        else if (ballHeight > this.windowController.getWindowDimensions().y()) {
            if (livesManager.getLivesLeft() <= 1) {
                gameEndPrompt = Constants.LOSE_MESSAGE;
            } else {
                livesManager.decreaseLives();
                resetBall();
            }
        }
        if(!gameEndPrompt.isEmpty()){
            gameEndPrompt += Constants.PLAY_AGAIN_MESSAGE;
            if(windowController.openYesNoDialog(gameEndPrompt)){
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }

    private void updateCounters(GameObject gameObject, boolean wasRemoved) {
        if (wasRemoved && gameObject.getTag().equals(Constants.BRICK_TAG)) {
            brickCounter.decrement();
        }
    }

    private void removeOutsideOfWindowObjects(String tag) {
        for (GameObject object : gameObjects()) {
            if (object.getTag().equals(tag) && isOutsideOfWindow(object)) {
                removeObject(object);
            }
        }
    }

    private boolean isOutsideOfWindow(GameObject object) {
        return (object.getCenter().y() > windowController.getWindowDimensions().y());
    }

    private void updateExtraBallsStrategy() {
        removeOutsideOfWindowObjects(Constants.PUCK_TAG);
    }

    private void updateChangeCameraStrategy() {
        if (mainBall.getCollisionCounter() >= Constants.COLLISIONS_UNTIL_RESET_CAMERA + 1) {
            setCamera(null);
            mainBall.resetCollisionCounter();
        }
    }

    private void updateExtraLifeStrategy() {
        removeOutsideOfWindowObjects(Constants.LIFE_TAG);
    }
}