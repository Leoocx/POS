package com.system.pos.pos.utils;

import javafx.scene.control.Alert;

/**
 * Utilitário para exibição de alertas
 */
public class AlertUtil {
    public static void mostrarAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}