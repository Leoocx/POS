package com.system.pos.pos.view;

import com.system.pos.pos.controller.ProdutoController;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.report.ReportPrinter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import static com.system.pos.pos.utils.AlertUtil.mostrarAlerta;

public class ProdutoView {
    
    @FXML private TextField nomeProduto;
    @FXML private TextField quantidade;
    @FXML private TextField preco;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TableView<Produto> table;

    private ProdutoController produtoController;
    private ObservableList<Produto> produtos;

    @FXML
    public void initialize() {
        this.produtoController = new ProdutoController();
        inicializarTabela();
        carregarDadosIniciais();
        configurarComponentes();
    }

    private void configurarComponentes() {
        statusComboBox.getItems().addAll("Estoque normal", "Baixo Estoque");

        // Configura validação para campos numéricos
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

    private void inicializarTabela() {
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

        table.getColumns().setAll(idColumn, nomeColumn, quantidadeColumn, precoColumn, statusColumn);
       

        // Listener para seleção na tabela
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCampos(newSelection);
            }
        });
    }

    private void carregarDadosIniciais() {
        try {
            produtos = FXCollections.observableArrayList(produtoController.listarTodos());
            table.setItems(produtos);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao carregar produtos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cadastraProdutoeButton() {
        try {
            if (validarCampos()) {
                Produto produto = criarProdutoFromInputs();
                produtoController.adicionarProduto(produto);
                atualizarTabela();
                limparCampos();
                mostrarAlerta("Sucesso", "Produto cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao cadastrar produto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void atualizarProdutoBTN() {
        Produto selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                if (validarCampos()) {
                    atualizarProdutoFromInputs(selecionado);
                    produtoController.atualizarProduto(selecionado);
                    atualizarTabela();
                    limparCampos();
                    mostrarAlerta("Sucesso", "Produto atualizado com sucesso!", Alert.AlertType.INFORMATION);
                }
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao atualizar produto: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum produto selecionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void removerProdutoBTN() {
        Produto selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                produtoController.removerProduto(selecionado.getId());
                atualizarTabela();
                limparCampos();
                mostrarAlerta("Sucesso", "Produto removido com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao remover produto: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum produto selecionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clearFields() {
        limparCampos();
    }

    @FXML
    private void gerarPDFButton(){
        ReportPrinter.imprimirTabela(table);
    }


    private boolean validarCampos() {
        if (nomeProduto.getText().isBlank() || quantidade.getText().isBlank() ||
                preco.getText().isBlank() || statusComboBox.getValue() == null) {
            mostrarAlerta("Aviso", "Preencha todos os campos obrigatórios", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private Produto criarProdutoFromInputs() {
        return new Produto(
                nomeProduto.getText(),
                Integer.parseInt(quantidade.getText()),
                Double.parseDouble(preco.getText().replace(",", ".")),
                statusComboBox.getValue()
        );
    }

    private void atualizarProdutoFromInputs(Produto produto) {
        produto.setNome(nomeProduto.getText());
        produto.setQuantidade(Integer.parseInt(quantidade.getText()));
        produto.setPreco(Double.parseDouble(preco.getText().replace(",", ".")));
        produto.setStatus(statusComboBox.getValue());
    }

    private void preencherCampos(Produto produto) {
        nomeProduto.setText(produto.getNome());
        quantidade.setText(String.valueOf(produto.getQuantidade()));
        preco.setText(String.valueOf(produto.getPreco()));
        statusComboBox.setValue(produto.getStatus());
    }

    private void limparCampos() {
        nomeProduto.clear();
        quantidade.clear();
        preco.clear();
        statusComboBox.getSelectionModel().clearSelection();
        table.getSelectionModel().clearSelection();
    }

    private void atualizarTabela() {
        produtos.setAll(produtoController.listarTodos());
    }


}