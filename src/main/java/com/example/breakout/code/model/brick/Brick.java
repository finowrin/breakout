package com.example.breakout.code.model.brick;

import com.example.breakout.code.model.ball.Ball;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


/**
 * A brick ADT with basic physics and display properties
 */
abstract public class Brick {


    /**
     * Direction of impact, self-explanatory constants
     */
    public enum ImpactDirection {
        UP, DOWN, LEFT, RIGHT, NONE;
    }

    private String name;
    protected Shape brickFace;

    private Color border;
    private Color inner;

    private int fullStrength;
    private int strength;

    private boolean broken;


    /**
     * Constructs a Brick with the following properties
     *
     * @param name     name of the brick
     * @param pos      position of the brick
     * @param size     size of the brick size
     * @param border   border color of the brick
     * @param inner    inner color of the brick
     * @param strength strength of the brick
     */
    protected Brick(String name, Point2D pos, Dimension2D size, Color border, Color inner, int strength) {
        //rnd = new Random();
        broken = false;
        this.name = name;
        brickFace = makeBrickFace(pos, size);
        this.border = border;
        this.inner = inner;
        this.fullStrength = this.strength = strength;

    }

    /**
     * Makes a shape to define the brick
     *
     * @param pos  position of the brick
     * @param size size of the brick
     * @return a shape to define the brick according to the parameters
     */
    protected abstract Shape makeBrickFace(Point2D pos, Dimension2D size);

    /**
     * Sets impactDirection on this brick if it's not already broken.
     *
     * @param point The point of impact
     * @param dir   the direction of impact
     * @return whether the brick is broken
     */
    public boolean setImpact(Point2D point, ImpactDirection dir) {
        if (broken)
            return false;
        impact();
        return broken;
    }

    /**
     * Returns the shape of this brick
     *
     * @return the shape of this brick
     */
    public abstract Shape getBrick();

    /**
     * Returns the brick's border color
     *
     * @return the brick's border color
     */
    public Color getBorderColor() {
        return border;
    }

    /**
     * Returns the brick's fill color
     *
     * @return the brick's fill color
     */
    public Color getInnerColor() {
        return inner;
    }

    /**
     * Detect the direction of impactDirection/collision of the ball with this brick.
     *
     * @param b the ball
     * @return the direction of impactDirection
     */
    public final ImpactDirection findImpact(Ball b) {
        if (broken)
            return ImpactDirection.NONE;
        ImpactDirection out = ImpactDirection.NONE;
        if (brickFace.contains(b.getRight()))
            out = ImpactDirection.LEFT;
        else if (brickFace.contains(b.getLeft()))
            out = ImpactDirection.RIGHT;
        else if (brickFace.contains(b.getUp()))
            out = ImpactDirection.DOWN;
        else if (brickFace.contains(b.getDown()))
            out = ImpactDirection.UP;
        return out;
    }

    /**
     * Returns whether the brick is broken
     *
     * @return whether the brick is broken
     */
    public final boolean isBroken() {
        return broken;
    }

    /**
     * Repair this brick
     */
    public void repair() {
        broken = false;
        strength = fullStrength;
    }

    /**
     * The brick simply weakens on impactDirection, and if it's already too weak, it breaks.
     */
    public void impact() {
        strength--;
        broken = (strength == 0);
    }

    /**
     * Draws the brick using the given graphics context
     *
     * @param g the graphics context to be used for drawing
     */
    public void draw(GraphicsContext g) {
        brickFace.setFill(new ImagePattern(getImage()));
        Rectangle r = ((Rectangle) brickFace);
        g.drawImage(getImage(), r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * Checks whether the brick is cracked.
     * A brick is cracked if its strength is less than its total strength but it is not zero.
     *
     * @return whether the brick is cracked
     */
    public boolean isCracked() {
        return strength > 0 && strength < fullStrength;
    }

    /**
     * Returns the image of this brick
     *
     * @return
     */
    public abstract Image getImage();
}





