package com.example.breakout.code.model.item;

import com.example.breakout.code.model.GameModel;
import com.example.breakout.code.model.brick.Brick;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.Objects;

/**
 * This item impacts some bricks randomly according to a set probability.
 */
public class ImpactBricksItem extends Item {

    public static final int DEFAULT_WIDTH = 15, DEFAULT_HEIGHT = 15;
    public static final int DEFAULT_SPEED_X = 0;
    public static final int DEFAULT_SPEED_Y = 5;

    public static final double DEFAULT_IMPACT_PROBABILITY = 0.4;
    //probability of destroying each brick

    private String imagePath = "/images/item.png";
    private Image image;
    private double impactProbability;

    /**
     * @param pos               item's position
     * @param dim               item's dimension
     * @param impactProbability Each brick's probability of getting impacted at this item's activation
     */
    public ImpactBricksItem(Point2D pos, Dimension2D dim, double impactProbability) {
        super(pos, (int) dim.getWidth(), (int) dim.getHeight());
        this.impactProbability = impactProbability;
        this.image = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
    }

    /**
     * Creates an ImpactBricksItem at the given position, with default properties
     *
     * @param pos item's position
     */
    public ImpactBricksItem(Point2D pos) {
        this(pos, new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT), DEFAULT_IMPACT_PROBABILITY);
        setSpeedX(DEFAULT_SPEED_X);
        setSpeedY(DEFAULT_SPEED_Y);
    }

    /**
     * Activate the item in the context of the gameModel
     *
     * @param gameModel the gameModel in which this item will be activated
     */
    public void activate(GameModel gameModel) {
        for (Brick brick : gameModel.getBricks()) {
            if (brick.isBroken())
                continue;
            if (Math.random() < impactProbability) {
                brick.impact();
            }
        }
    }

    @Override
    protected Image getImage() {
        return image;
    }
}
