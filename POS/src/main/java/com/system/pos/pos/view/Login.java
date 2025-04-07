package com.system.pos.pos.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    private Connection connection;

    public Login(Connection connection){
        this.connection = connection;
    }
    public Login(){}

    public void setUsername(TextField username){
        this.username = username;
    }

    public void setPassword(TextField password){
        this.password = password;
    }

    public void loginButton() throws SQLException {
        if (!username.getText().isBlank() && !password.getText().isBlank()) {
            validarLogin();
        } else {
            System.out.println("Entre com usuario e senha.");
        }
    }

    public void validarLogin() throws SQLException {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username.getText());
            ps.setString(2, password.getText());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login realizado com sucesso!");
                } else {
                    System.out.println("Usuário ou senha inválidos.");
                }
            }
        }
    }
}
