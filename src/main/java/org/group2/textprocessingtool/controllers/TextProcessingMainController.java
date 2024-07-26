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
import javafx.util.Callback;
import org.group2.textprocessingtool.model.TextEditor;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessingMainController {

    private static final int MAX_DISPLAY_LENGTH = 50;
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
    @FXML
    private ListView<String> collectionView;
    @FXML private MenuItem menuItemWordWrap;
    @FXML private MenuItem menuItemZoomIn;
    @FXML private MenuItem menuItemZoomOut;

    private final TextEditor textEditor;
    private ObservableList<String> textList;
    private double zoomFactor = 1.0;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        textList = FXCollections.observableArrayList(textEditor.getContent());
        collectionView.setItems(textList);
        collectionView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.length() > MAX_DISPLAY_LENGTH
                                    ? item.substring(0, MAX_DISPLAY_LENGTH) + "..."
                                    : item);
                        }
                    }
                };
            }
        });

        collectionView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                textInputArea.setText(newValue);
            }
        });
    }
    public TextProcessingMainController(){
        textEditor =new TextEditor();
        textList = FXCollections.observableArrayList(textEditor.getContent());
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
            primaryStage.setTitle(currentFile.getName());
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
            primaryStage.setTitle(currentFile.getName());
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
        textInputArea.cut();
    }

    public void handleCopy(ActionEvent actionEvent) {
        textInputArea.copy();
    }

    public void handlePaste(ActionEvent actionEvent) {
        textInputArea.paste();
    }

    public void handleFind(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search");
        dialog.setHeaderText("Enter a word to search:");
        dialog.setContentText("Word:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(word -> {
            handleSearch(word);
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
        showAlert(title, content, null);
    }

    public void handleReplace(ActionEvent actionEvent) {
        // Create a custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Search and Replace");
        dialog.setHeaderText("Enter the words to find and replace:");

        // Set the button types
        ButtonType searchButtonType = new ButtonType("Replace", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        // Create the labels and text fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField findField = new TextField();
        findField.setPromptText("Find what");
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
            // Handle the search and replace action (e.g., print to console)
            handleReplaceWords(findWord, replaceWord);
        });
    }

    private void handleReplaceWords(String findWord, String replaceWord) {
        //String[] inputText = textInputArea.getText().split("(?<=\\\\s)|(?=\\\\s)");
        String[] inputText = textInputArea.getText().split("\\s+");
        for(int i=0; i<inputText.length; i++){
            if(Objects.equals(inputText[i], findWord)){
                inputText[i] = replaceWord;
            }
        }
        StringBuilder str = new StringBuilder();
        for(String word: inputText){
            str.append(word);
            str.append(" ");
        }

        textInputArea.setText(String.valueOf(str));
    }

    @FXML
    private void handleCustomRegexTwo() {
        // Show an input dialog to get the regex pattern from the user
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Custom Regex");
        dialog.setHeaderText("Enter a regex pattern:");
        dialog.setContentText("Pattern:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::findAndPrintMatches);
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
        for (String elem : matches) {
            finalString.append(elem).append("\n");
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
        findAndPrintMatches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$");
    }

    public void handleMail(ActionEvent actionEvent) {
        findAndPrintMatches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }

    public void handleDate(ActionEvent actionEvent) {
        findAndPrintMatches("[0-9]{2}/[0-9]{2}/[0-9]{4}");
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
