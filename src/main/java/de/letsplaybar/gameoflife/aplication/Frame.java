package de.letsplaybar.gameoflife.aplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Frame extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.TRANSPARENT);
        Parent root = FXMLLoader.load(getClass().getResource("GameOfLife.fxml"));
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Game of Life");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Logo.png")));
        stage.show();
    }

    public void go() throws Exception {
        launch();
    }
}
