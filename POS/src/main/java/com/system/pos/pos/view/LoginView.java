package com.system.pos.pos.view;

import com.system.pos.pos.controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginView {

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void loginButton() {
        String user = username.getText();
        String pass = password.getText();

        if (!user.isBlank() && !pass.isBlank()) {
            loginController.login(user, pass);
        } else {
            System.out.println("Entre com usuário e senha.");
        }
    }
}
