package com.example.breakout.code.model.brick;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

/**
 * A Factory class to create Brick objects
 */
public class BrickFactory {

    public enum BrickType {
        CLAY, STEEL, CEMENT
    }

    /**
     * Makes a brick with the given properties
     * @param point position of the brick
     * @param size size of the brick
     * @param type type of the brick
     * @return the created brick
     */
    public static Brick makeBrick(Point2D point, Dimension2D size, BrickType type) {
        Brick out;
        switch (type) {
            case CLAY:
                out = new ClayBrick(point, size);
                break;
            case STEEL:
                out = new SteelBrick(point, size);
                break;
            case CEMENT:
                out = new CementBrick(point, size);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown Type:%d\n", type));
        }
        return out;
    }
}
