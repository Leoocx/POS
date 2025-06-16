package com.system.pos.pos.view;

import com.system.pos.pos.controller.ProdutoController;
import com.system.pos.pos.model.Categoria;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.model.SubCategoria;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ProdutoView {
    @FXML private TextField nomeProduto;
    @FXML private TextField quantidade;
    @FXML private TextField preco;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TableView<Produto> table;
    @FXML private TextField codigoBarrasField;
    @FXML private ComboBox<Categoria> categoriaComboBox;
    @FXML private ComboBox<SubCategoria> subCategoriaComboBox;

    private ProdutoController produtoController;

    @FXML
    public void initialize() throws SQLException {
        produtoController = new ProdutoController();
        configurarUI();
        carregarDados();
    }

    private void configurarUI() {
        configurarComboBoxes();
        configurarValidacaoCampos();
        configurarTabela();
    }

    private void configurarComboBoxes() {
        statusComboBox.getItems().addAll("Estoque normal", "Baixo Estoque", "Esgotado");
        categoriaComboBox.getItems().setAll(Categoria.values());
        categoriaComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                subCategoriaComboBox.getItems().setAll(newVal.getSubCategorias());
            }
        });
    }

    private void configurarValidacaoCampos() {
        quantidade.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantidade.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        preco.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                preco.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
    }

    private void configurarTabela() {
        TableColumn<Produto, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Produto, String> nomeColumn = new TableColumn<>("PRODUTO");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Produto, Integer> quantidadeColumn = new TableColumn<>("QUANTIDADE");
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        TableColumn<Produto, Double> precoColumn = new TableColumn<>("PREÇO");
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));

        TableColumn<Produto, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Produto, String> codigoBarrasColumn = new TableColumn<>("CÓDIGO DE BARRAS");
        codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));

        TableColumn<Produto, String> categoriaColumn = new TableColumn<>("CATEGORIA");
        categoriaColumn.setCellValueFactory(cellData -> {
            Categoria cat = cellData.getValue().getCategoria();
            return new SimpleStringProperty(cat != null ? cat.getDescricao() : "");
        });

        TableColumn<Produto, String> subCategoriaColumn = new TableColumn<>("SUBCATEGORIA");
        subCategoriaColumn.setCellValueFactory(cellData -> {
            SubCategoria sub = cellData.getValue().getSubCategoria();
            return new SimpleStringProperty(sub != null ? sub.getDescricao() : "");
        });

        table.getColumns().addAll(idColumn, nomeColumn, quantidadeColumn, precoColumn,
                statusColumn, codigoBarrasColumn, categoriaColumn, subCategoriaColumn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCampos(newSelection);
            }
        });
    }

    private void carregarDados() {
        try {
            table.setItems(produtoController.listarProdutos());
        } catch (Exception e) {
            exibirAlertaErro("Erro ao carregar produtos", e.getMessage());
        }
    }

    @FXML
    private void cadastrarProduto() {
        if (validarCampos()) {
            try {
                produtoController.cadastrarProduto(
                        nomeProduto.getText(),
                        Integer.parseInt(quantidade.getText()),
                        new BigDecimal(preco.getText().replace(",", ".")),
                        statusComboBox.getValue(),
                        codigoBarrasField.getText(),
                        categoriaComboBox.getValue(),
                        subCategoriaComboBox.getValue()
                );
                carregarDados();
                limparCampos();
                exibirAlertaSucesso("Produto cadastrado com sucesso!");
            } catch (Exception e) {
                exibirAlertaErro("Erro ao cadastrar produto", e.getMessage());
            }
        }
    }

    @FXML
    private void atualizarProduto() {
        Produto selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null && validarCampos()) {
            try {
                produtoController.atualizarProduto(
                        selecionado.getId(),
                        nomeProduto.getText(),
                        Integer.parseInt(quantidade.getText()),
                        new BigDecimal(preco.getText().replace(",", ".")),
                        statusComboBox.getValue(),
                        codigoBarrasField.getText(),
                        categoriaComboBox.getValue(),
                        subCategoriaComboBox.getValue()
                );
                carregarDados();
                limparCampos();
                exibirAlertaSucesso("Produto atualizado com sucesso!");
            } catch (Exception e) {
                exibirAlertaErro("Erro ao atualizar produto", e.getMessage());
            }
        } else {
            exibirAlertaAviso("Nenhum produto selecionado");
        }
    }

    @FXML
    private void removerProduto() {
        Produto selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                produtoController.removerProduto(selecionado.getId());
                carregarDados();
                limparCampos();
                exibirAlertaSucesso("Produto removido com sucesso!");
            } catch (Exception e) {
                exibirAlertaErro("Erro ao remover produto", e.getMessage());
            }
        } else {
            exibirAlertaAviso("Nenhum produto selecionado");
        }
    }

    @FXML
    private void gerarRelatorio() {
        produtoController.gerarRelatorio(table);
    }

    @FXML
    private void limparCampos() {
        nomeProduto.clear();
        quantidade.clear();
        preco.clear();
        codigoBarrasField.clear();
        statusComboBox.getSelectionModel().clearSelection();
        categoriaComboBox.getSelectionModel().clearSelection();
        subCategoriaComboBox.getSelectionModel().clearSelection();
        table.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        boolean camposValidos = produtoController.validarDadosProduto(
                nomeProduto.getText(),
                quantidade.getText(),
                preco.getText(),
                statusComboBox.getValue(),
                categoriaComboBox.getValue(),
                subCategoriaComboBox.getValue()
        );

        if (!camposValidos) {
            exibirAlertaAviso("Preencha todos os campos obrigatórios corretamente");
        }

        return camposValidos;
    }

    private void preencherCampos(Produto produto) {
        nomeProduto.setText(produto.getNome());
        quantidade.setText(String.valueOf(produto.getQuantidade()));
        preco.setText(produto.getPreco().toString());
        statusComboBox.setValue(produto.getStatus());
        codigoBarrasField.setText(produto.getCodigoBarras());
        categoriaComboBox.setValue(produto.getCategoria());
        subCategoriaComboBox.setValue(produto.getSubCategoria());
    }

    private void exibirAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirAlertaSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirAlertaAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}