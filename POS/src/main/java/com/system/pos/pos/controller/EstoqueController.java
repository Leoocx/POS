package com.system.pos.pos.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;

import com.system.pos.pos.model.Produto;
import com.system.pos.pos.model.Estoque;
import com.system.pos.pos.service.EstoqueService;

import java.time.format.DateTimeFormatter;

public class EstoqueController {

    @FXML private ComboBox<Produto> comboProduto;
    @FXML private TextField campoQuantidade;
    @FXML private TextField campoObservacao;

    @FXML private TableView<Estoque> tabelaHistorico;
    @FXML private TableColumn<Estoque, String> colDataHora;
    @FXML private TableColumn<Estoque, String> colProduto;
    @FXML private TableColumn<Estoque, String> colTipo;
    @FXML private TableColumn<Estoque, Integer> colQuantidade;
    @FXML private TableColumn<Estoque, String> colObservacao;

    private final EstoqueService estoqueService = new EstoqueService();
    private final ObservableList<Estoque> historico = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colDataHora.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        ));
        colProduto.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().getProduto().getNome()
        ));
        colProduto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduto().getNome()));
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo()));
        colQuantidade.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantidadeAlterada()).asObject());
        colObservacao.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getObservacao()));

        tabelaHistorico.setItems(historico);
    }

    @FXML
    private void registrarEntrada() {
        registrarMovimentacao("ENTRADA");
    }

    @FXML
    private void registrarSaida() {
        registrarMovimentacao("SAÍDA");
    }

    private void registrarMovimentacao(String tipo) {
        Produto produto = comboProduto.getValue();
        String qtdStr = campoQuantidade.getText();
        String obs = campoObservacao.getText();

        if (produto == null || qtdStr.isBlank()) {
            showAlert("Preencha todos os campos obrigatórios.");
            return;
        }

        try {
            int quantidade = Integer.parseInt(qtdStr);
            if (quantidade <= 0) throw new NumberFormatException();

            if (tipo.equals("ENTRADA")) {
                estoqueService.registrarEntrada(produto, quantidade, obs);
            } else {
                estoqueService.registrarSaida(produto, quantidade, obs);
            }

            historico.setAll(estoqueService.getHistorico());
            campoQuantidade.clear();
            campoObservacao.clear();

        } catch (NumberFormatException e) {
            showAlert("Digite uma quantidade válida.");
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
