package com.example.breakout.code.model.brick;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Objects;
import java.util.Random;

/**
 * Steel brick has a 40% chance of breaking on impact
 */
public class SteelBrick extends Brick {

    private static final String NAME = "Steel Brick";
    private static final Color DEF_INNER = Color.rgb(203, 203, 201);
    private static final Color DEF_BORDER = Color.BLACK;
    private static final int STEEL_STRENGTH = 1;
    private static final double STEEL_PROBABILITY = 0.4;

    private Random rnd;
    private Shape brickFace;

    private static final String img1 = "/images/steel.png";
    private static final String img2 = "/images/steel_cracked.png";
    private Image image1;
    private Image image2;

    public SteelBrick(Point2D point, Dimension2D size) {
        super(NAME, point, size, DEF_BORDER, DEF_INNER, STEEL_STRENGTH);

        image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(img1)));
        image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(img2)));
        rnd = new Random();
        brickFace = super.brickFace;
    }

    @Override
    protected Shape makeBrickFace(Point2D pos, Dimension2D size) {
        return new Rectangle(pos.getX(), pos.getY(), size.getWidth(), size.getHeight());
    }

    @Override
    public Shape getBrick() {
        return brickFace;
    }

    @Override
    public void impact() {
        if (rnd.nextDouble() < STEEL_PROBABILITY) {
            super.impact();
        }
    }

    @Override
    public Image getImage() {
        if (isCracked())
            return image2;
        else return image1;
    }
}
