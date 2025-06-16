package com.system.pos.pos.view;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ComprovanteView {
    public static void mostrarComprovante(String comprovante) {
        TextArea textArea = new TextArea(comprovante);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Comprovante de Venda");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(textArea);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        alert.showAndWait();
    }
}