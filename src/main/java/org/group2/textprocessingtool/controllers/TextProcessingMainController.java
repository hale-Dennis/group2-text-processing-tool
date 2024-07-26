package org.group2.textprocessingtool.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.group2.textprocessingtool.model.TextEditor;

import java.io.*;
import java.util.Optional;

public class TextProcessingMainController {

    private File currentFile;

    private Stage primaryStage;

    @FXML
    public TextField searchItem;
    @FXML
    public TextField replacementWord;
    @FXML
    private TextFlow resultArea;
    @FXML
    private TextArea textInputArea;

    private TextEditor textEditor;
    private ObservableList<String> textList;

    public void setPrimaryStage(Stage primaryStage) {

        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        textEditor = new TextEditor();
        textList = FXCollections.observableArrayList();;
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

    public void handleSave(ActionEvent actionEvent) {
        if (currentFile != null) {
            saveTextToFile(currentFile);
        } else {
            handleSaveAs(actionEvent);
        }
    }
    private void saveTextToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textInputArea.getText());
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }

    public void handleSaveAs(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            currentFile = file;
            saveTextToFile(file);
        }
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
    @FXML
    public void handleAdd(ActionEvent actionEvent) {
        String text = textInputArea.getText();
        if (text != null && !text.isEmpty()) {
            textEditor.addText(text);
            textList.setAll(textEditor.getContent());
            showAlert("Success", "Text added to collection.");
            textInputArea.clear(); // Clear the text area after adding text
            resultArea.getChildren().clear(); // Clear the result area if needed
        } else {
            showAlert("Error", "Text area is empty. Please enter some text.");
        }
    }


}
