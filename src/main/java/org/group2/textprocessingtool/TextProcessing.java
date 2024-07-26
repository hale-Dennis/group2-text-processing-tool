package org.group2.textprocessingtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.group2.textprocessingtool.controllers.TextProcessingMainController;

import java.io.IOException;

public class TextProcessing extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TextProcessing.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600); // Adjust the size as needed
        stage.setTitle("Text Processing");

        // Get the controller and set the primary stage
        TextProcessingMainController controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}