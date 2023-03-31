module com.example.breakout {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.example.breakout to javafx.fxml;
    exports com.example.breakout;
    opens com.example.breakout.code.controller to javafx.fxml;
    exports com.example.breakout.code.controller to javafx.fxml;
}