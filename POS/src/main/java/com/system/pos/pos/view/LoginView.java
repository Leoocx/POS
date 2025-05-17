package com.system.pos.pos.view;

import com.system.pos.pos.controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginView {

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    private Runnable onLoginSucesso;

    public void setOnLoginSucesso(Runnable onLoginSucesso) {
        this.onLoginSucesso = onLoginSucesso;
    }
    public void loginButton() {
        String user = username.getText();
        String pass = password.getText();

        if (!user.isBlank() && !pass.isBlank()) {
            boolean autenticado = LoginController.login(user, pass);
            if (autenticado && onLoginSucesso != null) {
                onLoginSucesso.run();
            }
        } else {
            System.out.println("Entre com usuário e senha.");
        }
    }




    public void registrarButton(){
        String user = username.getText();
        String pass = password.getText();

        LoginController.registrar(user, pass);

    }
}
