package com.example.breakout.code.model;


import com.example.breakout.code.model.ball.Ball;
import com.example.breakout.code.model.ball.Ball1;
import com.example.breakout.code.model.brick.Brick;
import com.example.breakout.code.model.paddle.Paddle;
import com.example.breakout.code.model.item.ImpactBricksItem;
import com.example.breakout.code.model.item.Item;
import com.example.breakout.code.util.GameObserver;
import com.example.breakout.code.util.HighScoresManager;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.breakout.code.model.brick.BrickFactory.BrickType.*;

/**
 * Model component of the MVC architecture of our game.
 * The controller must make changes to the game state via GameModel.
 * GameObservers can be added to GameModel to 'observe', i.e., get notified of any state changes.
 */
public class GameModel {
    private static final int LEVELS_COUNT = 7;
    private Random rnd;
    private Rectangle area;

    private Brick[] bricks;
    private Ball ball;
    private Paddle player;

    private final static double itemProbability = 1;
    private ArrayList<Item> items;

    private Brick[][] levels;
    private int level;

    private Point2D startPoint;
    private int brickCount;
    private int ballCount;
    private boolean ballLost;
    private int score;
    private List<GameObserver> observers;
    private HighScoresManager highScoresManager;

    public static final int DEFAULT_BALLS_COUNT = 3;

    private final double SPAWN_ITEM_PROBABILITY = 0.3;
    //probability of spawing an item when the ball breaks a brick

    /**
     * Constructs a GameModel with the given properties.
     *
     * @param drawArea            the area inside which the game shall be played/rendered.
     * @param brickCount          Number of bricks for each of the traditional levels.
     *                            Note that custom levels are independent of this property.
     * @param lineCount           number of brick lines in the traditional levels.
     *                            Note that custom levels are independent of this property.
     * @param brickDimensionRatio Ratio of brick length to bright height.
     * @param ballPos             Initial position of the ball
     */
    public GameModel(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point2D ballPos) {
        this.startPoint = ballPos;
        levels = makeLevels(drawArea, brickCount, lineCount, brickDimensionRatio);
        level = 0;
        ballCount = DEFAULT_BALLS_COUNT;
        ballLost = false;
        rnd = new Random();
        observers = new ArrayList<>();
        score = 0;
        highScoresManager = new HighScoresManager();
        highScoresManager.loadHighScores();
        makeBall(ballPos);

        final int UPPER_BOUND = 5;
        int speedX, speedY;
        do {
            speedX = rnd.nextInt(UPPER_BOUND) - 2;
        } while (speedX == 0);
        do {
            speedY = -rnd.nextInt(UPPER_BOUND);
        } while (speedY == 0);

        ball.setSpeed(speedX, speedY);

        final int PADDLE_WIDTH = 150;
        int PADDLE_HEIGHT = 10;
        player = new Paddle(ballPos, PADDLE_WIDTH, PADDLE_HEIGHT, drawArea);
        area = drawArea;

        items = new ArrayList<>();
    }

    /**
     * Makes the ball
     *
     * @param ballPos position of the ball
     */
    public void makeBall(Point2D ballPos) {
        ball = new Ball1(ballPos);
    }

    /**
     * Makes/loads the playable levels
     *
     * @param drawArea            the draw area
     * @param brickCount          number of bricks
     * @param lineCount           number of lines of bricks
     * @param brickDimensionRatio Ratio of the brick's dimension to the draw area's size
     * @return an array of levels; a level is represented by an array of bricks
     */
    private Brick[][] makeLevels(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio) {
        Brick[][] levels = new Brick[LEVELS_COUNT][];
        levels[0] = LevelFactory.makeSingleTypeLevel(drawArea, brickCount, lineCount, brickDimensionRatio, CLAY);
        levels[1] = LevelFactory.makeChessboardLevel(drawArea, brickCount, lineCount, brickDimensionRatio, CLAY, CEMENT);
        levels[2] = LevelFactory.makeChessboardLevel(drawArea, brickCount, lineCount, brickDimensionRatio, CLAY, STEEL);
        levels[3] = LevelFactory.makeChessboardLevel(drawArea, brickCount, lineCount, brickDimensionRatio, STEEL, CEMENT);

        brickCount -= brickCount % lineCount;
        int brickOnLine = brickCount / lineCount;
        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickDimensionRatio;
        Dimension2D bSize = new Dimension2D((int) brickLen, (int) brickHgt);
        Brick[][] customLevels = LevelFactory.makeCustomLevels(drawArea, bSize);

        levels[4] = customLevels[0];
        levels[5] = customLevels[1];
        levels[6] = customLevels[2];
        return levels;
    }

    /**
     * Moves the player and the ball
     */
    public void move() {
        player.move();
        ball.move();
        for (Item item : items) {
            item.move();
        }
    }

    /**
     * Handles the collisions of the ball with the paddle, power ups etc. and decides if the ball is lost
     */
    public void findImpacts() {
        if (player.impact(ball)) {
            ball.reverseY();
        } else if (impactWall()) {
            // for efficiency reverse is done into method impactWall because for every brick program checks for horizontal and vertical impacts
            for (var observer : observers) {
                observer.impact();
            }
            brickCount--;
            score++;
        } else if (impactBorder()) {
            ball.reverseX();
        } else if (ball.getPosition().getY() < area.getY()) {
            ball.reverseY();
        } else if (ball.getPosition().getY() > area.getY() + area.getHeight()) {
            ballCount--;
            ballLost = true;
        }
        for (Item item : items.toArray(Item[]::new)) {
            if (player.caughtItem(item)) {
                item.activate(this);
                items.remove(item);
            }
        }
        updateObservers();
    }

    /**
     * Checks if the ball collided with any brick and changes the ball's trajectory accordingly
     *
     * @return whether ball broke any brick
     */
    public boolean impactWall() {
        for (Brick b : bricks) {
            Brick.ImpactDirection impactDirection = b.findImpact(ball);
            if (impactDirection != Brick.ImpactDirection.NONE) {
                if (Math.random() < SPAWN_ITEM_PROBABILITY) {
                    ImpactBricksItem item = new ImpactBricksItem(ball.getDown());
                    items.add(item);
                }
            }
            switch (impactDirection) {
                //Vertical Impact
                case UP -> {
                    ball.reverseY();
                    return b.setImpact(ball.getDown(), Brick.ImpactDirection.UP);
                }
                case DOWN -> {
                    ball.reverseY();
                    return b.setImpact(ball.getUp(), Brick.ImpactDirection.DOWN);
                }

                //Horizontal Impact
                case LEFT -> {
                    ball.reverseX();
                    return b.setImpact(ball.getRight(), Brick.ImpactDirection.RIGHT);
                }
                case RIGHT -> {
                    ball.reverseX();
                    return b.setImpact(ball.getLeft(), Brick.ImpactDirection.LEFT);
                }
            }
        }
        return false;
    }

    /**
     * Handles the ball's collision with the game board's border
     *
     * @return whether the ball is touching the board's border
     */
    public boolean impactBorder() {

        return (ball.getLeft().getX() < area.getX()) ||
                (ball.getRight().getX() > area.getX() + area.getWidth());
        //return ((p.getX() < area.getX() + ) ||(p.getX() > (area.getX() + area.getWidth())));
    }

    /**
     * Returns the current number of bricks
     *
     * @return the current number of bricks
     */
    public int getBrickCount() {
        return brickCount;
    }

    /**
     * Returns the number of balls the player has left
     *
     * @return the number of balls the player has left
     */
    public int getBallCount() {
        return ballCount;
    }

    /**
     * Checks whether the ball is lost
     *
     * @return whether the ball is lost
     */
    public boolean isBallLost() {
        return ballLost;
    }

    /**
     * Resets the ball and the player
     */
    public void ballReset() {
        player.moveTo(startPoint);
        ball.moveTo(startPoint);
        int speedX, speedY;
        do {
            speedX = rnd.nextInt(5) - 2;
        } while (speedX == 0);
        do {
            speedY = -rnd.nextInt(3);
        } while (speedY == 0);

        ball.setSpeed(speedX, speedY);
        ballLost = false;
        updateObservers();
    }

    /**
     * Resets the current wall, i.e, all the bricks
     */
    public void wallReset() {
        for (Brick b : bricks)
            b.repair();
        brickCount = bricks.length;
        ballCount = 3;
        updateObservers();
    }

    /**
     * Returns whether the player has lost all the balls
     *
     * @return whether the player has lost all the balls
     */
    public boolean ballEnd() {
        return ballCount == 0;
    }

    /**
     * Checks whether this level is finished
     *
     * @return whether this level is finished
     */
    public boolean isDone() {
        return brickCount == 0;
    }

    /**
     * Loads the next level
     */
    public void nextLevel() {
        bricks = levels[level++];
        this.brickCount = bricks.length;
        updateObservers();
    }

    /**
     * Checks whether any more levels are left
     *
     * @return whether any more levels are left
     */
    public boolean hasLevel() {
        return level < levels.length;
    }

    /**
     * Sets the horizontal speed of the ball
     *
     * @param speedX horizontal speed of the ball
     */
    public void setBallXSpeed(int speedX) {
        ball.setXSpeed(speedX);
    }

    /**
     * Sets the vertical speed of the ball
     *
     * @param speedY vertical speed of the ball
     */
    public void setBallYSpeed(int speedY) {
        ball.setYSpeed(speedY);
    }

    /**
     * Resets the number of balls
     */
    public void resetBallCount() {
        ballCount = DEFAULT_BALLS_COUNT;
    }

    /**
     * Adds a game observer to be notified when the game state changes
     *
     * @param gameObserver observer to be addded
     */
    public void addObserver(GameObserver gameObserver) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(gameObserver);
    }

    /**
     * Returns the current score
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Updates all the game observers
     */
    public void updateObservers() {
        for (GameObserver gameObserver : observers) {
            gameObserver.update(ball, player, bricks, items);
        }
    }

    /**
     * Returns the rank on the basis of the current score
     *
     * @return the rank of the current player
     */
    public int getRank() {
        return highScoresManager.calculateRank(score);
    }

    /**
     * Returns the names of the high scorers
     *
     * @return names of the high scorers
     */
    public String[] getHighNames() {
        return highScoresManager.getHighNames();
    }

    /**
     * Returns the high scores
     *
     * @return the high scores
     */
    public int[] getHighScores() {
        return highScoresManager.getHighScores();
    }

    /**
     * Add a new High score entry
     *
     * @param name  player's name
     * @param score player's score
     */
    public void addHighScore(String name, int score) {
        highScoresManager.addHighScore(name, score);
    }

    /**
     * Saves the high scores
     */
    public void saveHighScores() {
        highScoresManager.saveHighScores();
    }

    /**
     * Returns the player paddle
     *
     * @return the player paddle
     */
    public Paddle getPlayer() {
        return player;
    }

    public Brick[] getBricks() {
        return bricks;
    }
}