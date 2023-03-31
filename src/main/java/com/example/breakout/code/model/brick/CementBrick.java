package com.example.breakout.code.model.brick;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Objects;

/**
 * A cement brick is twice more durable than a normal clay brick.
 */
public class CementBrick extends Brick {

    private static final String NAME = "Cement Brick";
    private static final Color DEF_INNER = Color.rgb(147, 147, 147);
    private static final Color DEF_BORDER = Color.rgb(217, 199, 175);
    private static final int CEMENT_STRENGTH = 2;

    private static final String img1 = "/images/cement.png";
    private static final String img2 = "/images/cement_cracked.png";
    private Image image1;
    private Image image2;

    public CementBrick(Point2D point, Dimension2D size) {
        super(NAME, point, size, DEF_BORDER, DEF_INNER, CEMENT_STRENGTH);
        image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(img1)));
        image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(img2)));
    }
    @Override
    protected Shape makeBrickFace(Point2D pos, Dimension2D size) {
        return new Rectangle(pos.getX(), pos.getY(), size.getWidth(), size.getHeight());
    }

    @Override
    public boolean setImpact(Point2D point, ImpactDirection dir) {
        if (super.isBroken())
            return false;
        super.impact();
        return super.isBroken();
    }

    @Override
    public Shape getBrick() {
        return brickFace;
    }

    @Override
    public void repair() {
        super.repair();
        brickFace = super.brickFace;
    }

    @Override
    public Image getImage() {
        if(isCracked())
            return image2;
        else return image1;
    }
}
