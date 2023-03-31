package com.example.breakout.code.model.paddle;

import com.example.breakout.code.model.ball.Ball;
import com.example.breakout.code.model.item.Item;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Objects;

/**
 * An instance is a 2D paddle which the player moves sideways to hit the ball and break the bricks
 */
public class Paddle {

    public static final Color BORDER_COLOR = Color.GREEN.darker().darker();
    public static final Color INNER_COLOR = Color.GREEN;

    public static final int DEF_MOVE_AMOUNT = 5;

    private Rectangle paddleFace;
    private Point2D ballPoint;
    private int moveAmount;
    private int min;
    private int max;

    private static final String IMAGE_PATH = "/images/paddle.png";
    private Image image;

    /**
     * Constructs a paddle with the given parameters
     * @param ballPoint where ball initially rests on the paddle
     * @param width width of the paddle
     * @param height height of the paddle
     * @param container the paddle's movement is constrained inside its container
     */
    public Paddle(Point2D ballPoint, int width, int height, Rectangle container) {
        this.ballPoint = ballPoint;
        moveAmount = 0;
        paddleFace = makeRectangle(width, height);
        min = (int) container.getX() + (width / 2);
        max = min + (int) container.getWidth() - width;
    }

    /**
     * Makes a rectangle to represent the paddle's bounds
     *
     * @param width  paddle width
     * @param height paddle height
     * @return
     */
    public Rectangle makeRectangle(int width, int height) {
        Point2D p = new Point2D((int) (ballPoint.getX() - (width / 2)), (int) ballPoint.getY());
        return new Rectangle(p.getX(), p.getY(), width, height);
    }

    /**
     * Returns whether the ball has collided with the paddle
     *
     * @param b the ball
     * @return whether the ball has collided with the paddle
     */
    public boolean impact(Ball b) {
        return paddleFace.contains(b.getPosition()) && paddleFace.contains(b.getDown());
    }

    /**
     * Moves the paddle
     */
    public void move() {
        double x = ballPoint.getX() + moveAmount;
        if (x < min || x > max)
            return;
        ballPoint = new Point2D(x, ballPoint.getY());
        paddleFace = new Rectangle(ballPoint.getX() - paddleFace.getWidth() / 2, ballPoint.getY(), paddleFace.getWidth(), paddleFace.getHeight());
    }

    /**
     * Moves the paddle to left
     */
    public void moveLeft() {
        moveAmount = -DEF_MOVE_AMOUNT;
    }

    /**
     * Moves the paddle to right
     */
    public void moveRight() {
        moveAmount = DEF_MOVE_AMOUNT;
    }

    /**
     * Stops the paddle
     */
    public void stop() {
        moveAmount = 0;
    }

    /**
     * Returns paddle face
     */
    public Shape getPaddleFace() {
        return paddleFace;
    }

    /**
     * Moves the paddle to a specified point
     *
     * @param p the point to which the paddle will be moved
     */
    public void moveTo(Point2D p) {
        ballPoint = p;
        paddleFace = new Rectangle(ballPoint.getX() - (int) (paddleFace.getWidth() / 2.0), ballPoint.getY(), paddleFace.getWidth(), paddleFace.getHeight());
    }

    /**
     * draws the paddle using the given graphics context
     * @param g the given graphics context
     */
    public void draw(GraphicsContext g) {
        Rectangle r = paddleFace;
        g.drawImage(getImage(), r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * Returns the image of this paddle.
     * @return the image of this paddle.
     */
    private Image getImage() {
        if(image == null) {
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH)));
        }
        return image;
    }

    public boolean caughtItem(Item item) {
        return paddleFace.contains(item.getPosition());
    }
}
