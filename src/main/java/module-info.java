module org.group2.textprocessingtool {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.group2.textprocessingtool to javafx.fxml;
    exports org.group2.textprocessingtool.controllers;

    // Add the following line to export the package to javafx.graphics
    exports org.group2.textprocessingtool to javafx.graphics;
}