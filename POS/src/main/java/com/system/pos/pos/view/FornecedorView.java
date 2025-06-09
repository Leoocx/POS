 package com.system.pos.pos.view;

import com.system.pos.pos.controller.FornecedoresController;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.report.ReportPrinter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class FornecedorView {
    
    @FXML private TextField nome;
    @FXML private TextField email;
    @FXML private TextField telefone;
    @FXML private TextField cnpj;

    @FXML private TableView<Fornecedor> table;
    @FXML private Button cadastroFornecedorBTN, atualizarFornecedorBTN , deleteFornecedorBTN , clearFieldsBTN;

    private FornecedoresController fornecedorController;
    private ObservableList<Fornecedor> fornecedores;

    @FXML
    public void initialize() throws SQLException {
        this.fornecedorController = new FornecedoresController();
        this.fornecedores = FXCollections.observableArrayList();
        inicializarTabela();
        atualizarTabela();
    }

    @FXML
    public void cadastroFornecedorBTN() {
        try {
            if (validarCampos()) {
                mostrarAlerta("Aviso", "Campos obrigatórios não foram preenchidos.", Alert.AlertType.WARNING);
            } else {
                Fornecedor fornecedor = new Fornecedor(
                        nome.getText(),
                        telefone.getText(),
                        email.getText(),
                        cnpj.getText()
                       
                );
                fornecedorController.cadastrarFornecedor(fornecedor);
                atualizarTabela();
                limparCampos();
                mostrarAlerta("Sucesso", "Fornecedor cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao cadastrar Fornecedor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void atualizarFornecedorBTN() {
        Fornecedor selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                if (validarCampos()) {
                    atualizarFornecedorFromInputs(selecionado);
                    fornecedorController.atualizarFornecedor(selecionado);
                    atualizarTabela();
                    limparCampos();
                    mostrarAlerta("Sucesso", "Fornecedor atualizado com sucesso!", Alert.AlertType.INFORMATION);
                }
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao atualizar Fornecedor: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum Fornecedor selecionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void deleteFornecedorBTN() {
        Fornecedor selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                fornecedorController.excluirFornecedor(selecionado);
                atualizarTabela();
                limparCampos();
                mostrarAlerta("Sucesso", "Fornecedor removido com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao remover Fornecedor: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum Fornecedor selecionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void gerarPDFButton() {
        ReportPrinter.imprimirTabela(table);
    }

    private void inicializarTabela() {

        TableColumn<Fornecedor, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Fornecedor, String> nomeColumn = new TableColumn<>("NOME");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Fornecedor, String> telefoneColumn = new TableColumn<>("TELEFONE");
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Fornecedor, Integer> cnpjColumn = new TableColumn<>("CNPJ");
        cnpjColumn.setCellValueFactory(new PropertyValueFactory<>("cnpj"));

        TableColumn<Fornecedor, String> emailColumn = new TableColumn<>("EMAIL");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));


        table.setItems(fornecedores);
        table.getColumns().setAll(idColumn, nomeColumn, telefoneColumn, cnpjColumn, emailColumn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCampos(newSelection);
            }
        });
    }

    private boolean validarCampos() {
        return !(nome.getText().isBlank() || email.getText().isBlank() || cnpj.getText().isBlank() || telefone.getText().isBlank());
    }

    private void atualizarFornecedorFromInputs(Fornecedor fornecedor) {
        fornecedor.setNome(nome.getText());
        fornecedor.setCnpj(cnpj.getText());
        fornecedor.setTelefone(telefone.getText());
        fornecedor.setEmail(email.getText());
    }

    private void preencherCampos(Fornecedor fornecedor) {
        nome.setText(fornecedor.getNome());
        cnpj.setText(String.valueOf(fornecedor.getCnpj()));
        email.setText(fornecedor.getEmail());
        telefone.setText(fornecedor.getTelefone());
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void atualizarTabela() throws SQLException {
        if (fornecedores != null) {
            fornecedores.setAll(fornecedorController.listarTodos());
        }
    }

    @FXML
    private void clearFieldsBTN() {
        limparCampos();
    }

    private void limparCampos() {
        nome.clear();
        cnpj.clear();
        email.clear();
        telefone.clear();
        table.getSelectionModel().clearSelection();
    }
}