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
                registrar(username,password);
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

    public static void registrar(String username, String password){
        try{
             loginService.registrarConta(username, password);
        } catch (Exception e){
            System.out.print("Erro ao tentar registrar uma conta!" + e.getMessage());
        }
    }

}
