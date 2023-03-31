package com.example.breakout.code.model;

import com.example.breakout.code.model.brick.Brick;
import com.example.breakout.code.model.brick.BrickFactory.BrickType;
import com.example.breakout.code.model.brick.ClayBrick;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static com.example.breakout.code.model.brick.BrickFactory.makeBrick;

/**
 * Factory class for vending standard Levels. A level is represented in the form of Brick array.
 */
public class LevelFactory {
    /**
     * A character to represent the corresponding brick type in the patterns.
     */
    private final static char type1 = '$', type2 = '&', type3 = '@';

    private final static String heartPattern = """
              @@  @@  \s
             @&&@@&&@ \s
             @&&&&&&@ \s
              @&&&&@  \s
               @&&@   \s
                @@
            """;

    private final static String spadePattern = """
                 &     \s
                &@&    \s
               &@@@&   \s
              &@@@@@&  \s
             &@@@@@@@& \s
            &@@@@@@@@@&\s
            &@@@&@&@@@&\s
             &&& & &&& \s
                 &     \s
                &&&    \s
               && &&
            """;

    /**
     * Makes an interlocking wall like level with only one type of bricks.
     *
     * @param drawArea       the area where the bricks will be drawn, i.e., placed
     * @param brickCnt       number of bricks
     * @param lineCnt        number of lines of bricks
     * @param brickSizeRatio ratio of brick length to its height
     * @param type           the level will consist of bricks of only this type.
     * @return the created level
     */
    public static Brick[] makeSingleTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, BrickType type) {
        // if brickCount is not divisible by line count,brickCount is adjusted to the biggest multiple of lineCount smaller then brickCount
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp = new Brick[brickCnt];

        Dimension2D brickSize = new Dimension2D((int) brickLen, (int) brickHgt);
        Point2D p;

        int i;
        for (i = 0; i < tmp.length; i++) {
            int line = i / brickOnLine;
            if (line == lineCnt)
                break;
            double x = (i % brickOnLine) * brickLen;
            x = (line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p = new Point2D(x, y);
            tmp[i] = makeBrick(p, brickSize, type);
        }

        for (double y = brickHgt; i < tmp.length; i++, y += 2 * brickHgt) {
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p = new Point2D(x, y);
            tmp[i] = new ClayBrick(p, brickSize);
        }
        return tmp;

    }

    /**
     * Makes a level with crisscross chessboard like patterns with two brick types
     *
     * @param drawArea       the area where the bricks will be drawn, i.e., placed
     * @param brickCnt       number of bricks
     * @param lineCnt        number of lines of bricks
     * @param brickSizeRatio ratio of the brick length to brick height
     * @param typeA          first brick type of the chessboard pattern
     * @param typeB          second brick type of the chessboard pattern
     * @return
     */
    public static Brick[] makeChessboardLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, BrickType typeA, BrickType typeB) {
        // if brickCount is not divisible by line count, brickCount is adjusted to the biggest multiple of lineCount smaller then brickCount
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        int centerLeft = brickOnLine / 2 - 1;
        int centerRight = brickOnLine / 2 + 1;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp = new Brick[brickCnt];

        Dimension2D brickSize = new Dimension2D((int) brickLen, (int) brickHgt);
        Point2D p;

        int i;
        for (i = 0; i < tmp.length; i++) {
            int line = i / brickOnLine;
            if (line == lineCnt)
                break;
            int posX = i % brickOnLine;
            double x = posX * brickLen;
            x = (line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p = new Point2D(x, y);

            boolean b = ((line % 2 == 0 && i % 2 == 0) || (line % 2 != 0 && posX > centerLeft && posX <= centerRight));
            tmp[i] = b ? makeBrick(p, brickSize, typeA) : makeBrick(p, brickSize, typeB);
        }

        for (double y = brickHgt; i < tmp.length; i++, y += 2 * brickHgt) {
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p = new Point2D(x, y);
            tmp[i] = makeBrick(p, brickSize, typeA);
        }
        return tmp;
    }

    /**
     * Makes a level in the form of an isosceles trapezoid.
     * If either n1 or n2 is 1, the resulting level will be triangular
     *
     * @param drawArea  the area where the bricks will be drawn, i.e., placed
     * @param brickSize size of the brick
     * @param n1        number of bricks in the uppermost line
     * @param n2        number of bricks in the lowest line
     * @return the created level
     */
    public static Brick[] makeTrapezoidLevel(Rectangle drawArea, Dimension2D brickSize, int n1, int n2) {

        ArrayList<Brick> bricks = new ArrayList<>();
        double w = drawArea.getWidth(), h = drawArea.getHeight();
        double bw = brickSize.getWidth();
        double bh = brickSize.getHeight();
        BrickType[] brickTypes = {BrickType.STEEL, BrickType.CEMENT, BrickType.CLAY};
        double y = 0;
        for (int i = n1; ; y += bh) {
            double space = (w / bw - i) * 0.5 * bw;
            BrickType bType = brickTypes[i % brickTypes.length];
            for (int j = 0; j < i; j++) {
                double x = space + j * bw;
                Brick brick = makeBrick(new Point2D(x, y), brickSize, bType);
                bricks.add(brick);
            }
            if (i == n2) break;
            if (i < n2) i++;
            else i--;
        }
        return bricks.toArray(Brick[]::new);
    }

    /**
     * Inspired by pixel art, this function makes a level based on a given String.
     * Each letter in the pattern represents a brick or an empty space of the same dimension as the bricks
     * The bricks are made so as to center the pattern in the drawing area.
     *
     * @param drawArea  the area where the bricks will be drawn, i.e., placed
     * @param brickSize size of each brick
     * @param pattern   a pattern representing the level
     * @return
     */
    private static Brick[] patternToLevel(Rectangle drawArea, Dimension2D brickSize, String pattern) {
        ArrayList<Brick> bricks = new ArrayList<>();
        String[] lines = pattern.split("\n");
        double bw = brickSize.getWidth();
        double bh = brickSize.getHeight();
        BrickType bType = BrickType.CLAY;
        double ox = (drawArea.getWidth() / brickSize.getWidth() - lines[0].length()) / 2;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == ' ') continue;
                switch (line.charAt(j)) {
                    case type1 -> {
                        bType = BrickType.CLAY;
                    }
                    case type2 -> {
                        bType = BrickType.CEMENT;
                    }
                    case type3 -> {
                        bType = BrickType.STEEL;
                    }
                }
                double x = (ox + j) * bw;
                double y = i * bh;
                Brick brick = makeBrick(new Point2D(x, y), brickSize, bType);
                bricks.add(brick);
            }
        }
        return bricks.toArray(Brick[]::new);
    }

    /**
     * Makes all the custom levels. This function exists as a segregation point between
     * the creation of the traditional levels and my custom levels.
     *
     * @param drawArea  the area where the bricks will be drawn, i.e., placed
     * @param brickSize size of the brick
     * @return the custom created levels
     */
    public static Brick[][] makeCustomLevels(Rectangle drawArea, Dimension2D brickSize) {
        int n1 = 1;
        int n2 = 10;
        Brick[] level1 = makeTrapezoidLevel(drawArea, brickSize, n1, n2);
        brickSize = new Dimension2D(brickSize.getHeight(), brickSize.getHeight());
        Brick[] level2 = patternToLevel(drawArea, brickSize, heartPattern);
        Brick[] level3 = patternToLevel(drawArea, brickSize, spadePattern);
        return new Brick[][]{level1, level2, level3};
    }
}
