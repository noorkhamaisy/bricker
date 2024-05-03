package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;

public class DoublePowerStrategy implements CollisionStrategy {
    private static final int NUMBER_OF_STRATEGIES_TO_HOLD = 2;
    private final BrickerGameManager brickerGameManager;
    private CollisionStrategy[] strategies;


    public DoublePowerStrategy(BrickerGameManager brickerGameManager, StrategiesFactory strategiesFactory,
                               int currentDoublePowerDepth) {
//        System.out.println("[DEBUG] DoublePowerStrategy constructor called, currentDoublePowerDepth: " + currentDoublePowerDepth);
        strategies = new CollisionStrategy[NUMBER_OF_STRATEGIES_TO_HOLD];
        this.brickerGameManager = brickerGameManager;
        strategies[Constants.FIRST_STRATEGY] =
                strategiesFactory.randomizeSpecialStrategies(currentDoublePowerDepth + 1);
        strategies[Constants.SECOND_STRATEGY] =
                strategiesFactory.randomizeSpecialStrategies(Constants.DOUBLE_POWER_MAXIMUM_DEPTH);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        for (CollisionStrategy strategy : strategies) {
            strategy.onCollision(thisObj, otherObj);
        }
    }
}
