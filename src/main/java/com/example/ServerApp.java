package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class ServerApp extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("createForm.fxml"));
        stage.setTitle("Create");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    
    }

    public static void main(String[] args) {
        launch();
    }

}