package org.group2.textprocessingtool.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.group2.textprocessingtool.model.TextEditor;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @FXML private MenuItem menuItemWordWrap;
    @FXML private MenuItem menuItemZoomIn;
    @FXML private MenuItem menuItemZoomOut;

    private TextEditor textEditor;
    private ObservableList<String> textList;
    private double zoomFactor = 1.0;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        textEditor = new TextEditor();
        textList = FXCollections.observableArrayList();
        ;
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

        if (inputText.contains(word)) {
            int occurrence = countOccurrences(inputText, word);
            showAlert("Word search", word + " occurs " + occurrence + " times in text");
        } else {
            showAlert("Word search", word + " is not in text");
        }

        // searchItem.setText("");
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

    @FXML
    private void handleCustomRegexTwo() {
        // Show an input dialog to get the regex pattern from the user
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Custom Regex");
        dialog.setHeaderText("Enter a regex pattern:");
        dialog.setContentText("Pattern:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(pattern -> {
            findAndPrintMatches(pattern);
        });
    }

    private void findAndPrintMatches(String regex) {
        List<String> matches = new ArrayList<>();
        StringBuilder finalString = new StringBuilder();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textInputArea.getText());

        // Print matches to console
        while (matcher.find()) {
            String match = matcher.group();
            matches.add(match);
        }
        System.out.println(matches.toString());
        for (String elem : matches) {
            finalString.append(elem + "\n");
        }

        if (!matches.isEmpty()) {
            showAlert("Regex", String.valueOf(finalString), "Matches Found");
        } else {
            showAlert("Regex", "", "No matches found");
        }

    }

    public void handleCustomRegex(ActionEvent actionEvent) {
        // custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Search and Replace with Regex");
        dialog.setHeaderText("Enter the regex to find and replace:");

        ButtonType searchButtonType = new ButtonType("Replace", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField findField = new TextField();
        findField.setPromptText("Find what pattern");
        TextField replaceField = new TextField();
        replaceField.setPromptText("Replace with");

        grid.add(new Label("Find what:"), 0, 0);
        grid.add(findField, 1, 0);
        grid.add(new Label("Replace with:"), 0, 1);
        grid.add(replaceField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a pair of strings when the search button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == searchButtonType) {
                return new Pair<>(findField.getText(), replaceField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            String findWord = pair.getKey();
            String replaceWord = pair.getValue();

            handleReplaceWithRegex(findWord, replaceWord);
        });
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
            showAlert("Success",
                    "Text added to collection.");
            textInputArea.clear(); // Clear the text area after adding text
            resultArea.getChildren().clear(); // Clear the result area if needed
        } else {
            showAlert("Error", "Text area is empty. Please enter some text.");
        }
    }

    private void handleReplaceWithRegex(String findWord, String replaceWord) {
        List<String> matches = new ArrayList<>();
        String[] arr = textInputArea.getText().split("\\s+");
        StringBuilder finalString = new StringBuilder();
        Pattern pattern = Pattern.compile(findWord);
        Matcher matcher = pattern.matcher(textInputArea.getText());

        while (matcher.find()) {
            String match = matcher.group();
            matches.add(match);
        }

        for (int i = 0; i < arr.length; i++) {
            if (matches.contains(arr[i])) {
                arr[i] = replaceWord;
            }
        }

        for (String elem : arr) {
            finalString.append(elem).append(" ");
        }

        textInputArea.setText(String.valueOf(finalString));
        showAlert("regex replacement", "patterns successfully replaced");
    }

    @FXML
    private void handleMenuItemWordWrapAction() {
        textInputArea.setWrapText(!textInputArea.isWrapText());
    }

    @FXML
    private void handleMenuItemZoomInAction() {
        zoomFactor += 0.1;
        textInputArea.setStyle("-fx-font-size: " + (12 * zoomFactor) + "px;");
    }

    @FXML
    private void handleMenuItemZoomOutAction() {
        zoomFactor -= 0.1;
        textInputArea.setStyle("-fx-font-size: " + (12 * zoomFactor) + "px;");
    }

}
