package com.system.pos.pos.view;

import com.system.pos.pos.service.RelatorioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class RelatorioView {

    RelatorioService relatorioService;

    @FXML
    public void gerarRelatorioClientes(ActionEvent event) {
        mostrarAlerta("Relatório de Clientes", "Relatório de clientes gerado com sucesso!");
    }

    @FXML
    public void gerarRelatorioFinanceiro(ActionEvent event) {
        mostrarAlerta("Relatório Financeiro", "Relatório financeiro gerado com sucesso!");
    }
    @FXML
    public void gerarRelatorioVendas(ActionEvent event) {
        mostrarAlerta("Relatório de Vendas", "Relatório de vendas gerado com sucesso!");
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
