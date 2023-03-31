package com.example.breakout.code.util;

import com.example.breakout.code.model.ball.Ball;
import com.example.breakout.code.model.brick.Brick;
import com.example.breakout.code.model.paddle.Paddle;
import com.example.breakout.code.model.item.Item;

import java.util.ArrayList;

/**
 * All classes to be notified when the game's state changes must implement GameObserverr
 */
public interface GameObserver {

    /**
     * Called when the game state changes
     * @param ball the game ball
     * @param paddle the paddle controlled by the player
     * @param bricks bricks in the current game
     * @param items
     */
    void update(Ball ball, Paddle paddle, Brick[] bricks, ArrayList<Item> items);

    void impact();
}
