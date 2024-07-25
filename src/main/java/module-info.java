module org.group2.textprocessingtool {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.group2.textprocessingtool.controllers to javafx.fxml;
    exports org.group2.textprocessingtool;

}