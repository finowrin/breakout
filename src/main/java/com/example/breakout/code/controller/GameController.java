package com.example.breakout.code.controller;

import com.example.breakout.code.model.GameModel;
import com.example.breakout.code.view.GameCanvasView;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller component of the MVC architecture of our game.
 * Controls the in-game actions based on UI events(through FXML elements) and updates the GameModel accordingly.
 */
public class GameController implements Initializable {
    @FXML
    private TextField hsInputField; // for high score name input
    @FXML
    private VBox hsBox; // for high score display
    @FXML
    private Pane hsDisplay;
    @FXML
    private Pane hsInput;
    @FXML
    private Canvas canvas;
    @FXML
    private Label topLabel;
    @FXML
    private HBox debugPanel;
    @FXML
    private VBox pauseMenu;

    private GameCanvasView canvasView;
    private GameModel gameModel;

    private AnimationTimer gameTimer;
    private String message;
    private static final String noMoreLevelsText = "No more levels left.";

    /**
     * Sets the game model for this game controller
     *
     * @param gameModel the game model
     */
    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * Setup the Animation timer to control the frames for the game
     */
    private void setupTimer() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gameModel.move();
                gameModel.findImpacts();
                message = String.format("Bricks: %d Balls %d", gameModel.getBrickCount(), gameModel.getBallCount());

                if (gameModel.isBallLost()) {
                    if (gameModel.ballEnd()) {
                        gameOver(gameModel.getRank());
                    }
                    gameModel.ballReset();
                    gameTimer.stop();
                } else if (gameModel.isDone()) {
                    if (gameModel.hasLevel()) {
                        message = "Go to Next Level";
                        gameTimer.stop();
                        reset();
                        gameModel.nextLevel();
                    } else {
                        message = "ALL WALLS DESTROYED";
                        gameTimer.stop();
                        gameOver(gameModel.getRank());
                    }
                }
                topLabel.setText(message);
            }
        };
        gameTimer.start();
    }

    /**
     * Stops the animation timer and updates the UI based on the user's score.
     * If the user has a high rank, his name is saved. Finally high scores are displayed.
     *
     * @param rank the player's rank; -1 means not in high score list
     */
    private void gameOver(int rank) {
        gameTimer.stop();
        canvas.setFocusTraversable(false);
        canvas.setEffect(new GaussianBlur());
        if (rank == -1) {
            hsDisplay();
        } else {
            hsInput.setVisible(true);
            hsInput.requestFocus();
        }
        message = "Game over";
    }

    /**
     * Displays the high scores screen
     */
    private void hsDisplay() {
        Pair<String, Integer>[] highScores;
        String[] bestNames = gameModel.getHighNames();
        int[] bestScores = gameModel.getHighScores();
        int rank = gameModel.getRank();

        GridPane grid = new GridPane();

        for (int row = 0; row < bestScores.length; row++) {
            Label l1 = new Label();
            Label l2 = new Label();

            l1.setText(bestNames[row]);
            l2.setText("" + bestScores[row]);

            if (row == rank) {
                l1.setId("impHsEntry");
                l2.setId("impHsEntry");
            }

            l1.getStyleClass().add("highName");
            l1.prefWidthProperty().bind(hsBox.widthProperty().multiply(0.5));
            l2.getStyleClass().add("highScore");
            l2.prefWidthProperty().bind(hsBox.widthProperty().multiply(0.5));

            grid.add(l1, 0, row);
            grid.add(l2, 1, row);
        }
        gameModel.saveHighScores();
        hsBox.getChildren().add(grid);
        hsDisplay.setVisible(true);
        hsDisplay.requestFocus();
    }

    /**
     * Resets the game - both the model and the view.
     */
    private void reset() {
        if (gameModel != null) {
            gameModel.wallReset();
            gameModel.ballReset();
        }
        topLabel.setText("Press Space to Start");
        debugPanel.setVisible(false);
        pauseMenu.setVisible(false);
        hsBox.getChildren().removeIf(node -> node.getId() == null || !node.getId().equals("hsHeading"));
        hsDisplay.setVisible(false);
        hsInput.setVisible(false);
        canvas.setEffect(null);
        canvas.setFocusTraversable(true);
        pauseMenu.translateXProperty().set(canvas.getWidth());
    }

    /**
     * Pauses the game and displays the pause screen
     */
    private void pause() {
        gameTimer.stop();
        pauseMenu.setVisible(true);
        canvas.setEffect(new GaussianBlur());
        canvas.setFocusTraversable(false);
        pauseMenu.requestFocus();
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(pauseMenu.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        timeline.getKeyFrames().add(kf);

        timeline.play();
    }

    /**
     * starts the game.
     */
    public void start() {
        canvasView = new GameCanvasView(canvas);
        canvas.setFocusTraversable(true);
        gameModel.addObserver(canvasView);
        setupTimer();
        gameModel.nextLevel();
    }

    private void showDebugPanel() {
        gameTimer.stop(); //pause the gameplay in debug mode
        debugPanel.requestFocus();
        debugPanel.setVisible(true);
    }

    @FXML
    private void hideDebugPanel() {
        debugPanel.setVisible(false);
        canvas.requestFocus();
        gameTimer.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reset();
    }

    @FXML
    private void hsUpdate(ActionEvent event) {
        hsInput.setVisible(false);
        String name = hsInputField.getText();
        gameModel.addHighScore(name, gameModel.getScore());
        hsDisplay();
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT:
                gameModel.getPlayer().moveLeft();
                break;
            case RIGHT:
                gameModel.getPlayer().moveRight();
                break;
            case SPACE:
                gameTimer.start();
                break;
            case ESCAPE:
                pause();
                break;
            case F1:
                if (keyEvent.isAltDown() && keyEvent.isShiftDown())
                    showDebugPanel();
                break;
        }

    }

    @FXML
    private void keyReleased(KeyEvent keyEvent) {
        gameModel.getPlayer().stop();
    }

    /**
     * Continues the game if it is paused.
     */
    @FXML
    private void continueGame() {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(pauseMenu.translateXProperty(), canvas.getWidth(), Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            pauseMenu.setVisible(false);
            canvas.setEffect(null);
            canvas.setFocusTraversable(true);
            gameTimer.start();
        });
        timeline.play();
    }

    /**
     * Restarts the game by resetting the wall and the locations of ball and paddle
     */
    @FXML
    private void restartGame() {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(pauseMenu.translateXProperty(), canvas.getWidth(), Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            reset();
        });
        timeline.play();
    }

    /**
     * Exits the game
     */
    @FXML
    private void exit() {
        System.exit(0);
    }

    /**
     * Resets the number of balls the player has
     */
    @FXML
    private void resetBalls() {
        gameModel.resetBallCount();
    }

    /**
     * Loads the next level, if this is not the last level.
     */
    @FXML
    private void skipLevel() {
        if (gameModel.hasLevel()) {
            gameModel.nextLevel();
        } else {
            topLabel.setText(noMoreLevelsText);
        }
    }
}
