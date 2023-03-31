package com.example.breakout.code.model.item;

import com.example.breakout.code.model.GameModel;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * An Item gives the player a bonus like destroying some bricks, adding extra balls,etc.
 * To activate an item, the player must catch it.
 */
public abstract class Item {

    private double centreX, centreY;
    private int width;
    private int speedY;

    private int speedX;
    private int height;

    /**
     * @return returns the item's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the item's width
     *
     * @param width the item's width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return returns the item's vertical speed
     */
    public int getSpeedY() {
        return speedY;
    }

    /**
     * Sets the item's vertical speed
     *
     * @param speedY the item's vertical speed
     */
    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    /**
     * @return returns the item's horizontal speed
     */
    public int getSpeedX() {
        return speedX;
    }

    /**
     * Sets the item's horizontal speed
     *
     * @param speedX the item's horizontal speed
     */
    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    /**
     * @return returns the item's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the item's height
     *
     * @param height the item's height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Constructor for the Item
     *
     * @param pos    item's position
     * @param width  item's width
     * @param height item's height
     */
    protected Item(Point2D pos, int width, int height) {
        this.centreX = pos.getX();
        this.centreY = pos.getY();
        this.width = width;
        this.height = height;
    }

    /**
     * Moves the item according to its speed
     */
    public void move() {
        centreX += speedX;
        centreY += speedY;
    }

    /**
     * @return the item's image
     */
    protected abstract Image getImage();

    /**
     * draws the item
     *
     * @param g the graphics context used for drawing
     */
    public void draw(GraphicsContext g) {
        int x = (int) (centreX - width / 2);
        int y = (int) (centreY - height / 2);
        g.drawImage(getImage(), x, y, width, height);
    }

    /**
     * @return the item's position
     */
    public Point2D getPosition() {
        return new Point2D(centreX, centreY);
    }

    /**
     * Activates the item
     *
     * @param gameModel the gameModel in which this item's effect will be applied
     */
    public abstract void activate(GameModel gameModel);
}
