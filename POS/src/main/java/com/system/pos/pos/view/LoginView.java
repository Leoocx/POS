package com.system.pos.pos.view;

import com.system.pos.pos.controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoginView {

    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword; // Novo campo para confirmação
    @FXML private Button btnLogin;
    @FXML private Button btnRegister;
    @FXML private GridPane login_content;

    private Runnable onLoginSucesso;
    private boolean registerMode = false;

    @FXML
    public void initialize() {
        // Configuração inicial
        setupRegisterMode(false);
    }

    public void setOnLoginSucesso(Runnable onLoginSucesso) {
        this.onLoginSucesso = onLoginSucesso;
    }

    @FXML
    public void loginButton() {
        if (registerMode) {
            // Se estiver no modo registro, volta para o modo login
            setupRegisterMode(false);
            return;
        }

        String user = username.getText();
        String pass = password.getText();

        if (!user.isBlank() && !pass.isBlank()) {
            boolean autenticado = LoginController.login(user, pass);
            if (autenticado && onLoginSucesso != null) {
                onLoginSucesso.run();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Campos vazios", "Preencha todos os campos");
        }
    }

    @FXML
    public void registerButton() {
        if (!registerMode) {
            // Entra no modo registro
            setupRegisterMode(true);
            return;
        }

        // Processa o registro
        String user = username.getText();
        String pass = password.getText();
        String confirmPass = confirmPassword.getText();

        if (user.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Campos vazios", "Preencha todos os campos");
            return;
        }

        if (!pass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "Erro de senha", "As senhas não coincidem");
            return;
        }

        boolean registrado = LoginController.registrar(user, pass);

        if (registrado) {
            setupRegisterMode(false); // Volta para o modo login
            confirmPassword.clear(); // Limpa o campo de confirmação
        }
    }

    private void setupRegisterMode(boolean isRegisterMode) {
        this.registerMode = isRegisterMode;

        if (isRegisterMode) {
            // Modo registro - mostra campo de confirmação
            if (confirmPassword == null) {
                confirmPassword = new PasswordField();
                confirmPassword.setPromptText("Confirmar senha");
                confirmPassword.getStyleClass().addAll("field-filled", "lead-lock", "pw-viewer", "input-large");
                confirmPassword.setMinHeight(50.0);
                confirmPassword.setPrefHeight(40.0);

                // Adiciona o campo ao GridPane
                login_content.addRow(4, confirmPassword);
                GridPane.setColumnSpan(confirmPassword, Integer.MAX_VALUE);
            }

            btnLogin.setText("Cancelar");
            btnRegister.setText("Confirmar Registro");
        } else {
            // Modo login - esconde campo de confirmação
            if (confirmPassword != null) {
                login_content.getChildren().remove(confirmPassword);
            }

            btnLogin.setText("Entrar");
            btnRegister.setText("Registrar");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}