package com.system.pos.pos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/system/pos/pos/login.fxml"));
        URL fxml = getClass().getResource("/com/system/pos/pos/login.fxml");
        System.out.println(fxml);

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("POS System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}