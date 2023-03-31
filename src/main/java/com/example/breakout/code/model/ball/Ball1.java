package com.example.breakout.code.model.ball;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

import java.util.Objects;

/**
 * An instance is a 2d circular Rubber ball
 */
public class Ball1 extends Ball {

    private static final String NAME = "Rubber Ball";
    private static final int DEF_RADIUS = 10;
    private static final Color DEF_INNER_COLOR = Color.rgb(255, 219, 88);
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();

    private static final String IMAGE_PATH = "/images/ball1.png";
    private Image image;

    /**
     * Makes a ball with the specified center and all other default properties
     * @param center center of the ball
     */
    public Ball1(Point2D center){
        super(center,DEF_RADIUS,DEF_RADIUS,DEF_INNER_COLOR,DEF_BORDER_COLOR);
    }

    /**
     * Makes a ball with the given properties
     * @param center center of the ball
     * @param radiusA horizontal radius of the ball
     * @param radiusB vertical radius of the ball
     * @return
     */
    @Override
    protected Shape makeBall(Point2D center, int radiusA, int radiusB) {

        double x = center.getX() - (radiusA / 2);
        double y = center.getY() - (radiusB / 2);

        return new Ellipse(x,y,radiusA,radiusB);
    }

    @Override
    public Image getImage() {
        if(image == null) {
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH)));
        }
        return image;
    }
}
