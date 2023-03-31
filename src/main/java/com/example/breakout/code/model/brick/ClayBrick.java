package com.example.breakout.code.model.brick;


import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Objects;

/**
 * Clay brick is a weak brick which breaks on single impact.
 */
public class ClayBrick extends Brick {

    private static final String NAME = "Clay Brick";
    private static final Color DEF_INNER = Color.rgb(178, 34, 34).darker();
    private static final Color DEF_BORDER = Color.GRAY;
    private static final int CLAY_STRENGTH = 1;

    private static final String img1 = "/images/clay.png";
    private Image image;

    public ClayBrick(Point2D point, Dimension2D size) {
        super(NAME, point, size, DEF_BORDER, DEF_INNER, CLAY_STRENGTH);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(img1)));
    }

    @Override
    protected Shape makeBrickFace(Point2D pos, Dimension2D size) {
        return new Rectangle(pos.getX(), pos.getY(), size.getWidth(), size.getHeight());
    }

    @Override
    public Shape getBrick() {
        return super.brickFace;
    }

    @Override
    public Image getImage() {
        return image;
    }
}
