package com.system.pos.pos.controller;

import com.system.pos.pos.service.LoginService;
import javafx.fxml.FXMLLoader;

public class LoginController {

    private static final LoginService loginService = new LoginService();

    public static void login(String username, String password) {
        try {
            boolean autenticado = loginService.validarLogin(username, password);
            if (autenticado) {
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Usuário ou senha inválidos.");
                loginService.registrarConta(username, password);
            }
        } catch (Exception e) {
            System.out.println("Erro ao tentar logar: " + e.getMessage());
        }
    }

    public static void registrar(String username, String password){
        try{
             loginService.registrarConta(username, password);
        } catch (Exception e){
            System.out.print("Erro ao tentar registrar uma conta!" + e.getMessage());
        }
    }


}
