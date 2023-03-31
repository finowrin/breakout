package com.example.breakout.code.model.ball;



import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

/**
 * A Ball ADT to with basic physics and rendering properties.
 */
abstract public class Ball {

    private Shape ballFace;

    private Point2D center;
    private Point2D up;
    private Point2D down;
    private Point2D left;
    private Point2D right;

    private Color border;
    private Color inner;

    private int speedX;
    private int speedY;

    /**
     * Constructs a Ball object
     * @param center center of the ball
     * @param radiusA horizontal radius of the ball
     * @param radiusB vertical radius of the ball
     * @param inner inner color of the ball
     * @param border border color of the ball
     */
    public Ball(Point2D center, int radiusA, int radiusB, Color inner, Color border) {
        this.center = center;

        up = new Point2D(center.getX(), center.getY() - (radiusB / 2));
        down = new Point2D(center.getX(), center.getY() + (radiusB / 2));
        left = new Point2D(center.getX() - (radiusA / 2), center.getY());
        right = new Point2D(center.getX() + (radiusA / 2), center.getY());

        ballFace = makeBall(center, radiusA, radiusB);
        this.border = border;
        this.inner = inner;
        speedX = 0;
        speedY = 0;
    }

    /**
     * Makes a shape to define a ball
     * @param center center of the ball
     * @param radiusA horizontal radius of the ball
     * @param radiusB vertical radius of the ball
     * @return a shape according to the given parameters
     */
    protected abstract Shape makeBall(Point2D center, int radiusA, int radiusB);

    /**
     * Moves the ball by one unit of time according to its speed.
     */
    public void move() {
        Ellipse tmp = (Ellipse)ballFace;
        center = new Point2D((center.getX() + speedX), (center.getY() + speedY));
        double rx = tmp.getRadiusX();
        double ry = tmp.getRadiusY();

        tmp = new Ellipse((center.getX() - (rx / 2)), (center.getY() - (ry / 2)), rx, ry);
        setPoints(rx, ry);

        ballFace = tmp;
    }

    /**
     * Sets the horizontal and vertical speeds of the ball
     *
     * @param x the horizontal speed of the ball
     * @param y the vertical speed of the ball
     */
    public void setSpeed(int x, int y) {
        speedX = x;
        speedY = y;
    }

    /**
     * Sets the horizontal speed of the ball.
     *
     * @param s the horizontal speed of the ball.
     */
    public void setXSpeed(int s) {
        speedX = s;
    }

    /**
     * Sets the vertical speed of the ball.
     *
     * @param s the vertical speed of the ball.
     */
    public void setYSpeed(int s) {
        speedY = s;
    }

    /**
     * Reverses the ball's horizontal movement. If it was going up, it will go right and vice versa.
     */
    public void reverseX() {
        speedX *= -1;
    }

    /**
     * Reverses the ball's vertical movement. If it was going up, it will go down and vice versa.
     */
    public void reverseY() {
        speedY *= -1;
    }

    /**
     * Returns the color of the ball's border
     *
     * @return the color of the ball's border
     */
    public Color getBorderColor() {
        return border;
    }

    /**
     * Returns the color of the ball
     *
     * @return the color of the ball
     */
    public Color getInnerColor() {
        return inner;
    }

    /**
     * Returns the current position of the ball
     *
     * @return the current position of the ball
     */
    public Point2D getPosition() {
        return center;
    }

    /**
     * Returns the shape of the ball's face
     *
     * @return the shape of the ball's face
     */
    public Shape getBallFace() {
        return ballFace;
    }

    /**
     * Moves the ball to the point p
     *
     * @param p the point to which the ball will move
     */
    public void moveTo(Point2D p) {
        center = p;

        Ellipse tmp = (Ellipse)ballFace;
        double rx = tmp.getRadiusX();
        double ry = tmp.getRadiusY();

        tmp = new Ellipse((center.getX() - (rx / 2)), (center.getY() - (ry / 2)), rx, ry);
        ballFace = tmp;
    }

    /**
     * Sets the points of the ball based on the given width and height
     *
     * @param width  width of the ball
     * @param height height of the ball
     */
    public void setPoints(double width, double height) {
        up = new Point2D(center.getX(), center.getY() - (height / 2));
        down = new Point2D(center.getX(), center.getY() + (height / 2));

        left = new Point2D(center.getX() - (width / 2), center.getY());
        right = new Point2D(center.getX() + (width / 2), center.getY());
    }

    /**
     * Returns the current horizontal speed of the ball
     *
     * @return the current horizontal speed of the ball
     */
    public int getSpeedX() {
        return speedX;
    }

    /**
     * Returns the current vertical speed of the ball
     *
     * @return the current vertical speed of the ball
     */
    public int getSpeedY() {
        return speedY;
    }

    /**
     * Returns the upper point of the ball
     * @return upper point of the ball
     */
    public Point2D getUp() {
        return up;
    }

    /**
     * Returns the lower point of the ball
     * @return lower point of the ball
     */
    public Point2D getDown() {
        return down;
    }

    /**
     * Returns the left point of the ball
     * @return left point of the ball
     */
    public Point2D getLeft() {
        return left;
    }

    /**
     * Returns the right point of the ball
     * @return right point of the ball
     */
    public Point2D getRight() {
        return right;
    }

    /**
     * Returns image of the ball
     * @return image of the ball
     */
    public abstract Image getImage();

    /**
     * Draws the ball using the given graphics context
     * @param g the graphics context to be used for drawing
     */
    public void draw(GraphicsContext g) {
        Ellipse e = (Ellipse) ballFace;
        double x = e.getCenterX() - e.getRadiusX();
        double y = e.getCenterY() - e.getRadiusY();
        g.drawImage(getImage(), x, y, 2 * e.getRadiusX(), 2 * e.getRadiusY());
    }
}
