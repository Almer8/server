module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;
    requires combinatoricslib3;

    opens com.example to javafx.fxml;
    exports com.example;
    exports message;
    opens message to javafx.fxml;
}
