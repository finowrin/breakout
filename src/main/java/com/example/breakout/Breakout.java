package com.example.breakout;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <a href = https://en.wikipedia.org/wiki/Breakout_(video_game)>Breakout</> clone.
 * This is the main class which allows our game to run as a stand-alone java application.
 */
public class Breakout extends Application {

    private static final String startScenePath = "/fxmls/start_scene.fxml";
    private static final String title = "Breakout Clone     space = start   ←/→ = move left/right   esc = pause menu";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(startScenePath));
        Parent root = loader.load();

        primaryStage.setTitle(title);
        Scene scene1 = new Scene(root);

        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
