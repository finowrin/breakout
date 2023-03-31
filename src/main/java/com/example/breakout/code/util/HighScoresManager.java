package com.example.breakout.code.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * Manages all operations relatd to HighScores including permanent save feature.
 * Stores high scores and the player names in two separate arrays, mapped using same index.
 */
public class HighScoresManager {
    private static final String filePath = "highscores";
    static final String[] defaultHighNames = {"A", "B", "C", "D", "E"};
    static final int[] defaultHighScores = {20, 18, 17, 12, 3};

    private String[] highNames;
    private int[] highScores;

    /**
     * Initializes a HighScoreManager with default highscores list
     */
    public HighScoresManager() {
        highScores = Arrays.copyOf(defaultHighScores, defaultHighScores.length);
        highNames = Arrays.copyOf(defaultHighNames, defaultHighNames.length);
    }

    /**
     * Initializes a HighScoreManager with the given properties
     * @param highNames names of the high scorers
     * @param highScores high scores in the sorted order(descending)
     */
    public HighScoresManager(String[] highNames, int[] highScores) {
        this.highNames = highNames;
        this.highScores = highScores;
    }

    /**
     * Saves the high scores permanently.
     */
    public void saveHighScores() {
        if (highScores == null || highNames == null) {
            highNames = defaultHighNames;
            highScores = defaultHighScores;
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(highNames);
            out.writeObject(highScores);

        } catch (Exception e) {
            System.out.println("Failed to save highscores");
        }

    }

    /**
     * loads the permanently saved high scores. If high scores cannot be loaded, default high scores are used.
     */
    public void loadHighScores() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            highNames = (String[]) (in.readObject());
            highScores = (int[]) (in.readObject());

        } catch (Exception e) {
            System.out.println("Failed to load highscores");
        }
        if (highScores == null || highNames == null) {
            highNames = defaultHighNames;
            highScores = defaultHighScores;
            saveHighScores();
        }
    }

    /**
     * Calculates the rank on the basis of the given score.
     * Treats the given score as a new entry.
     * If the score is already present in the list, the given score is ranked lower.
     * Must not be used to get the rank of an already ranked player.
     * @return rank on the basis of the current score
     */
    public int calculateRank(int score) {
        int n = highScores.length;
        for (int i = 0; i < n; i++) {
            if (score > highScores[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the names of the highest scorers
     * @return array of high scorers' names sorted according to their scores
     */
    public String[] getHighNames() {
        return highNames;
    }

    /**
     * Returns the high scores
     * @return sorted array of high scores.
     */
    public int[] getHighScores() {
        return highScores;
    }

    /**
     * Adds a new high score entry
     * @param name Name of the player
     * @param score High score
     */
    public void addHighScore(String name, int score) {
        int rank = calculateRank(score);
        if (rank < 0 || rank >= highScores.length)
            throw new IllegalArgumentException("Invalid rank");
        int n = highScores.length;
        int[] ts = new int[n];
        String[] tn = new String[n];
        for (int i = 0; i < n; i++) {
            if (i < rank) {
                ts[i] = highScores[i];
                tn[i] = highNames[i];
            } else if (i == rank) {
                ts[i] = score;
                tn[i] = name;
            } else {
                ts[i] = highScores[i - 1];
                tn[i] = highNames[i - 1];
            }
        }
        highNames = tn;
        highScores = ts;
    }
}
