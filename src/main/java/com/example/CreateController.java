package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class CreateController {
    @FXML
    Label errorLabel;
    @FXML 
    TextField portField;
    @FXML
    Button okButton;

    DataSingleton data = DataSingleton.getInstance();

    Integer getPort(){
        try{
            return Integer.parseInt(portField.getText());
        }
        catch(Exception e){
            errorLabel.setText("Port is unacceptable!");
        }
        return null;
        
    }
    @FXML
    void CreateServer()throws Exception{

        ThreadedServer server = new ThreadedServer(getPort());
        data.setServer(server);
        errorLabel.setTextFill(Color.BLACK);
        errorLabel.setText("Waiting for client...");

        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("serverView.fxml"));
        stage.setTitle("Conversations");
        stage.setScene(new Scene(root));
        Thread serverThread = new Thread(() -> {
            try {
                server.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();

    }
}
