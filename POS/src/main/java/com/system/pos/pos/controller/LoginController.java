package com.system.pos.pos.controller;

import com.system.pos.pos.service.LoginService;

public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public void login(String username, String password) {
        try {
            boolean autenticado = loginService.validarLogin(username, password);
            if (autenticado) {
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Usuário ou senha inválidos.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao tentar logar: " + e.getMessage());
        }
    }
}
