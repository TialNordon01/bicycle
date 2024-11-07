module main.bicycle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens main.bicycle to javafx.fxml;
    exports main.bicycle;
}