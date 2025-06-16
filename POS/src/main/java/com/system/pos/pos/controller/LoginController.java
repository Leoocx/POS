package com.system.pos.pos.controller;

import com.system.pos.pos.service.LoginService;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class LoginController {

    private static final LoginService loginService = new LoginService();

    public static boolean login(String username, String password) {
        try {
            boolean autenticado = loginService.validarLogin(username, password);
            if (autenticado) {
                System.out.println("Login realizado!");
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de Login");
                alert.setHeaderText("Usuário ou senha inválidos");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("Erro ao tentar logar: " + e.getMessage());
        }
        return false;
    }

    public static boolean registrar(String username, String password) {
        try {
            if (loginService.usuarioExiste(username)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Registro");
                alert.setHeaderText("Usuário já existe");
                alert.setContentText("Tente outro nome de usuário.");
                alert.showAndWait();
                return false;
            }

            loginService.registrarConta(username, password);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro");
            alert.setHeaderText("Conta criada com sucesso!");
            alert.setContentText("Você já pode fazer login.");
            alert.showAndWait();
            return true;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao registrar");
            alert.setContentText("Detalhes: " + e.getMessage());
            alert.showAndWait();
            return false;
        }
    }
}
