package com.example.breakout.code.model;

import com.example.breakout.code.model.brick.Brick;
import javafx.geometry.Dimension2D;
import javafx.scene.shape.Rectangle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class LevelFactoryTest {
    @Test
    public void makeTrapezoidLevelTest() {
        int n1 = 1, n2 = 5; //bricks in first line, bricks in last line
        Rectangle drawArea = new Rectangle(0, 0, 100, 100);
        Dimension2D brickSize = new Dimension2D(10, 10);
        Brick[] level = LevelFactory.makeTrapezoidLevel(drawArea, brickSize, n1, n2);
        assertNotNull(level);
        assertEquals(level.length, 15); // total bricks  = 1 + 2 + 3 + 4 + 5 = 15
    }
}
