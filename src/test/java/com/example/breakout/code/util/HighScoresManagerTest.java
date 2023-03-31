package com.example.breakout.code.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HighScoresManagerTest {
    final static String[] testHighNames = {"A", "B", "C", "D", "E"};
    final static int[] testHighScores = {20, 18, 17, 12, 3};

    @Test
    public void saveLoadTest() {
        HighScoresManager hsm = new HighScoresManager(testHighNames, testHighScores);
        hsm.saveHighScores();
        hsm.loadHighScores();
        assertNotNull(hsm.getHighScores());
        assertNotNull(hsm.getHighNames());
        assertEquals(hsm.getHighScores().length, testHighScores.length);
        for(int i = 0; i < testHighScores.length;i++)
            assertEquals(hsm.getHighScores()[i], testHighScores[i]);
    }

    @Test
    public void getRankTest() {
        HighScoresManager hsm = new HighScoresManager(testHighNames, testHighScores);
        assertEquals(hsm.calculateRank(22), 0);
        assertEquals(hsm.calculateRank(0), -1);
    }

    @Test
    public void addHighScoreTest() {
        HighScoresManager hsm = new HighScoresManager(testHighNames, testHighScores);
        hsm.addHighScore("TestName", 16);
        assertEquals(hsm.getHighScores()[3], 16);
    }
}
