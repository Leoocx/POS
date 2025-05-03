package com.system.pos.pos.view;

import com.system.pos.pos.controller.ProdutoController;
import com.system.pos.pos.model.Categoria;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.model.SubCategoria;
import com.system.pos.pos.service.ProdutoService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CadastroProdutoView {
    @FXML public TextField codigo;
    @FXML public TextField unidade;
    @FXML public TextField fornecedor;
    @FXML public TextField marca;
    @FXML public TextField referencia;
    @FXML public TextField localizacao;
    @FXML public TextField validade;
    @FXML public ComboBox<String> categoria;
    @FXML public ComboBox<String> subCategoria;
    @FXML public TextField descricao;

    private ProdutoController produtoController;


    @FXML
    public void initialize() {
        categoria.getItems().addAll(Arrays.asList(Categoria.values()).stream().map(Enum::name).toArray(String[]::new));
        subCategoria.getItems().addAll(Arrays.asList(SubCategoria.values()).stream().map(Enum::name).toArray(String[]::new));
    }

    @FXML
    public void cadastrarProdutoButton() {
        try {
            if (!codigo.getText().isBlank() &&
                            !localizacao.getText().isBlank() &&
                            !validade.getText().isBlank() &&
                            !marca.getText().isBlank() &&
                            !unidade.getText().isBlank() &&
                            subCategoria.getValue() != null &&
                            categoria.getValue() != null &&
                            !referencia.getText().isBlank()
            ) {
                Produto produto = new Produto();
                produto.setCdProduto(Integer.parseInt(codigo.getText()));
                produto.setLocalizacao(localizacao.getText());
                produto.setValidade(LocalDate.parse(validade.getText()));
                produto.setMarca(marca.getText());
                produto.setUnidade(Integer.parseInt(unidade.getText()));
                produto.setSubCategoria(SubCategoria.valueOf(subCategoria.getValue()));
                produto.setCategoria(Categoria.valueOf(categoria.getValue()));
                produto.setReferencia(referencia.getText());

                produtoController.adicionarProduto(produto);
            } else {
                System.out.println("Preencha todos os campos obrigatórios.");
            }
        } catch (Exception e) {
            showAlert("Erro", "Erro ao cadastrar o produto: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}