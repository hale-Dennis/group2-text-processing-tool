package org.group2.textprocessingtool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

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
        textInputArea.redo();
    }

    public void handleCut(ActionEvent actionEvent) {
    }

    public void handleCopy(ActionEvent actionEvent) {
    }

    public void handlePaste(ActionEvent actionEvent) {
    }

    public void handleFind(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search");
        dialog.setHeaderText("Enter a word to search:");
        dialog.setContentText("Word:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(word -> {
            handleSearch(word);
            System.out.println("Searching for: " + word);
        });

    }

    @FXML
    private void handleSearch(String word) {

        String inputText = textInputArea.getText();

        if(inputText.contains(word)){
            int occurrence = countOccurrences(inputText, word);
            showAlert("Word search", word + " occurs " + occurrence + " times in text" );
        }
        else{
            showAlert("Word search", word + " is not in text");
        }

        //searchItem.setText("");
    }

    public static int countOccurrences(String text, String word) {
        if (text == null || word == null || word.isEmpty()) {
            return 0;
        }

        String[] words = text.split("\\s+");
        int count = 0;

        for (String w : words) {
            if (w.equals(word)) {
                count++;
            }
        }

        return count;
    }

    private void showAlert(String title, String content, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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
