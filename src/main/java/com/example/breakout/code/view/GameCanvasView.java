package com.example.breakout.code.view;

import com.example.breakout.code.model.ball.Ball;
import com.example.breakout.code.model.brick.Brick;
import com.example.breakout.code.model.paddle.Paddle;
import com.example.breakout.code.model.item.Item;
import com.example.breakout.code.util.GameObserver;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Draws the game sprites on a canvas and plays sound effects
 */
public class GameCanvasView implements GameObserver {

    private Canvas canvas;
    private  MediaPlayer player;
    private static final String soundPath = "/audio/impact_sound.mp3";
    private Media impactSound;
    /**
     * @param canvas the canvas on which the game sprites will be drawn
     */
    public GameCanvasView(Canvas canvas) {
        this.canvas = canvas;
        impactSound = new Media(Objects.requireNonNull(getClass().getResource(soundPath)).toExternalForm());
        player = new MediaPlayer(impactSound);
    }

    @Override
    public void update(Ball ball, Paddle paddle, Brick[] bricks, ArrayList<Item> items) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Brick brick : bricks) {
            if (!brick.isBroken())
                brick.draw(g);
        }

        for(Item item : items) {
            item.draw(g);
        }
        ball.draw(g);
        paddle.draw(g);
    }

    @Override
    public void impact() {
        player = new MediaPlayer(impactSound);
        player.play();
    }
}
