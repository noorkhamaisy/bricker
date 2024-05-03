package bricker.main;

public class Constants {
    public static final String GAME_NAME = "Bricker";
    public static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    public static final String BALL_IMAGE_PATH = "assets/ball.png";
    public static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    public static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";
    public static final String BRICK_IMAGE_PATH = "assets/brick.png";
    public static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    public static final String HEARTS_IMAGE_PATH = "assets/heart.png";
    public static final String PADDLE_TAG = "Paddle";
    public static final String EXTRA_PADDLE_TAG = "ExtraPaddle";
    public static final String BRICK_TAG = "Brick";
    public static final String MAIN_BALL_TAG = "mainBall";
    public static final String WALL_TAG = "Wall";
    public static final String PUCK_TAG = "Puck";
    public static final String LIFE_TAG = "Life";
    public static final String WIN_MESSAGE = "You win! ";
    public static final String LOSE_MESSAGE = "You lose! ";
    public static final String PLAY_AGAIN_MESSAGE = "Play again?";
    public static final float PADDLE_LOCATION_MUL_FACTOR = 0.5f;
    public static final int PADDLE_SPEED = 350;
    public static final int BALL_SPEED_MUL_FACTOR = 250;
    public static final int FRAME_RATE = 100;
    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 500;
    public static final int BALL_RADIUS = 20;
    public static final int PADDLE_WIDTH = 200;

    public static final int PADDLE_HEIGHT = 15;
    public static final int WALL_WIDTH = 10;
    public static final float PUCK_SIZE_FACTOR = 0.75f;
    public static final int DEFAULT_NUMBER_OF_BRICKS_IN_ROW = 7;
    public static final int DEFAULT_NUMBER_OF_BRICKS_IN_COLUMN = 8;
    public static final int BRICK_HEIGHT = 15;
    public static final int BRICKS_PADDING = WINDOW_WIDTH / 175;
    public static final int HEARTS_Y_COORDINATE = WINDOW_HEIGHT - 50;
    public static final int HEARTS_X_COORDINATE = 10;
    public static final int INITIAL_HEARTS_NUMBER = 3;
    public static final int EXTRA_PADDLE_MAXIMUM_COLLISIONS = 4;
    public static final int COLLISIONS_UNTIL_RESET_CAMERA = 4;
    public static final int MAXIMUM_HEARTS_NUMBER = 4;
    public static final int BALLS_TO_CREATE = 2;
    public static final int FALLING_HEART_VELOCITY = 100;
    public static final int LIFE_DATA_SIZE = 20;
    public static final int DOUBLE_POWER_MAXIMUM_DEPTH = 2;
    public static final int SPECIAL_STRATEGIES_NUMBER = 5;

    public static final int EXTRA_BALLS_STRATEGY_CHOSEN = 0;
    public static final int EXTRA_PADDLE_STRATEGY_CHOSEN = 1;
    public static final int CHANGE_CAMERA_STRATEGY_CHOSEN = 2;
    public static final int EXTRA_LIFE_STRATEGY_CHOSEN = 3;
    public static final int DOUBLE_POWER_STRATEGY_CHOSEN = 4;
    public static final int FIRST_STRATEGY = 0;
    public static final int SECOND_STRATEGY = 1;


}
