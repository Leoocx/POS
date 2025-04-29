package com.system.pos.pos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

import com.system.pos.pos.database.CaixaDAO;
import com.system.pos.pos.database.ConnectionDB;
import com.system.pos.pos.database.DatabaseInitialize;
import com.system.pos.pos.database.FornecedorDAO;
import com.system.pos.pos.database.ProdutoDAO;
import com.system.pos.pos.database.VendaDAO;
import com.system.pos.pos.model.Fornecedor;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/system/pos/pos/login.fxml"));

        URL fxml = getClass().getResource("/com/system/pos/pos/login.fxml");
        System.out.println(fxml);

        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("Sistema de Ponto de Venda (POS) ");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}