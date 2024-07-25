package org.group2.textprocessingtool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextProcessingMainController {

    private File currentFile;

    private Stage primaryStage;

    @FXML
    public TextField searchItem;
    @FXML
    public TextField replacementWord;
    @FXML
    private TextArea textInputArea;

    public void setPrimaryStage(Stage primaryStage) {

        this.primaryStage = primaryStage;
    }

    public void handleOpen(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            getTextFromFile(file);
            currentFile = file;
        }
    }
    
     private void getTextFromFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
        textInputArea.setText(content.toString());
    }

    public void handleSaveTwo(ActionEvent actionEvent) {
    }

    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void handleUndo(ActionEvent actionEvent) {
        textInputArea.undo();
    }

    public void handleRedo(ActionEvent actionEvent) {
    }

    public void handleCut(ActionEvent actionEvent) {
    }

    public void handleCopy(ActionEvent actionEvent) {
    }

    public void handlePaste(ActionEvent actionEvent) {
    }

    public void handleFind(ActionEvent actionEvent) {
    }

    public void handleReplace(ActionEvent actionEvent) {
    }

    public void handleCustomRegexTwo(ActionEvent actionEvent) {
    }

    public void handleCustomRegex(ActionEvent actionEvent) {
    }

    public void handlePhone(ActionEvent actionEvent) {
    }

    public void handleMail(ActionEvent actionEvent) {
    }

    public void handleDate(ActionEvent actionEvent) {
    }

    public void handleAbout(ActionEvent actionEvent) {
    }


}
