package com.system.pos.pos.view;

import com.system.pos.pos.controller.ProdutoController;
import com.system.pos.pos.controller.FornecedoresController;
import com.system.pos.pos.database.FornecedorDAO;
import com.system.pos.pos.model.Categoria;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.model.SubCategoria;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.report.ReportPrinter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.sql.SQLException;

import static com.system.pos.pos.utils.AlertUtil.mostrarAlerta;

public class ProdutoView {

    @FXML private TextField nomeProduto;
    @FXML private TextField quantidade;
    @FXML private TextField preco;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TableView<Produto> table;
    @FXML private TextField codigoBarrasField;
    @FXML private ComboBox<Categoria> categoriaComboBox;
    @FXML private ComboBox<SubCategoria> subCategoriaComboBox;
    @FXML private ComboBox<Fornecedor> fornecedorComboBox;

    private ProdutoController produtoController;
    private FornecedoresController fornecedoresController;
    private ObservableList<Produto> produtos;
    private ObservableList<Fornecedor> fornecedores;

    @FXML
    public void initialize() {
        this.produtoController = new ProdutoController();
        this.fornecedoresController = new FornecedoresController();
        inicializarTabela();
        carregarDadosIniciais();
        configurarComponentes();
    }

    private void configurarComponentes() {
        // Configura ComboBox de status
        statusComboBox.getItems().addAll("Estoque normal", "Baixo Estoque", "Esgotado");

        // Configura ComboBox de categorias e subcategorias
        categoriaComboBox.setItems(FXCollections.observableArrayList(Categoria.values()));
        categoriaComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                subCategoriaComboBox.setItems(FXCollections.observableArrayList(newVal.getSubCategorias()));
            }
        });

        // Configura ComboBox de fornecedores
        carregarFornecedores();

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

    private void carregarFornecedores() {
        try {
            fornecedores = FXCollections.observableArrayList(fornecedoresController.listarTodos());
            fornecedorComboBox.setItems(fornecedores);
            fornecedorComboBox.setConverter(new StringConverter<Fornecedor>() {
                @Override
                public String toString(Fornecedor fornecedor) {
                    return fornecedor != null ? fornecedor.getNome() : "";
                }

                @Override
                public Fornecedor fromString(String string) {
                    return fornecedores.stream().filter(f -> f.getNome().equals(string)).findFirst().orElse(null);
                }
            });
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Falha ao carregar fornecedores: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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

        TableColumn<Produto, String> fornecedorColumn = new TableColumn<>("FORNECEDOR");
        fornecedorColumn.setCellValueFactory(cellData -> {
            Fornecedor forn = cellData.getValue().getFornecedor();
            return new SimpleStringProperty(forn != null ? forn.getNome() : "");
        });

        table.getColumns().setAll(idColumn, nomeColumn, quantidadeColumn, precoColumn,
                statusColumn, codigoBarrasColumn, categoriaColumn, subCategoriaColumn, fornecedorColumn);

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
    private void cadastraProdutoButton() {
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
    private void limparCamposBTN() {
        limparCampos();
    }

    @FXML
    private void gerarPDFButton() {
        ReportPrinter.imprimirTabela(table);
    }

    private boolean validarCampos() {
        if (nomeProduto.getText().isBlank() ||
                quantidade.getText().isBlank() ||
                preco.getText().isBlank() ||
                statusComboBox.getValue() == null ||
                categoriaComboBox.getValue() == null ||
                subCategoriaComboBox.getValue() == null ||
                fornecedorComboBox.getValue() == null) {

            mostrarAlerta("Aviso", "Todos os campos são obrigatórios, incluindo fornecedor!", Alert.AlertType.WARNING);
            return false;
        }

        try {
            new BigDecimal(preco.getText().replace(",", "."));
            Integer.parseInt(quantidade.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Valores numéricos inválidos", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private Produto criarProdutoFromInputs() {
        return new Produto(
                nomeProduto.getText(),
                Integer.parseInt(quantidade.getText()),
                new BigDecimal(preco.getText().replace(",", ".")),
                statusComboBox.getValue(),
                codigoBarrasField.getText(),
                categoriaComboBox.getValue(),
                subCategoriaComboBox.getValue(),
                fornecedorComboBox.getValue()
        );
    }

    private void atualizarProdutoFromInputs(Produto produto) {
        produto.setNome(nomeProduto.getText());
        produto.setQuantidade(Integer.parseInt(quantidade.getText()));
        produto.setPreco(new BigDecimal(preco.getText().replace(",", ".")));
        produto.setStatus(statusComboBox.getValue());
        produto.setCodigoBarras(codigoBarrasField.getText());
        produto.setCategoria(categoriaComboBox.getValue());
        produto.setSubCategoria(subCategoriaComboBox.getValue());
        produto.setFornecedor(fornecedorComboBox.getValue());
    }

    private void preencherCampos(Produto produto) {
        nomeProduto.setText(produto.getNome());
        quantidade.setText(String.valueOf(produto.getQuantidade()));
        preco.setText(String.valueOf(produto.getPreco()));
        statusComboBox.setValue(produto.getStatus());
        codigoBarrasField.setText(produto.getCodigoBarras());
        categoriaComboBox.setValue(produto.getCategoria());
        subCategoriaComboBox.setValue(produto.getSubCategoria());
        fornecedorComboBox.setValue(produto.getFornecedor());
    }

    private void limparCampos() {
        nomeProduto.clear();
        quantidade.clear();
        preco.clear();
        codigoBarrasField.clear();
        statusComboBox.getSelectionModel().clearSelection();
        categoriaComboBox.getSelectionModel().clearSelection();
        subCategoriaComboBox.getSelectionModel().clearSelection();
        fornecedorComboBox.getSelectionModel().clearSelection();
        table.getSelectionModel().clearSelection();
    }

    private void atualizarTabela() {
        try {
            produtos.setAll(produtoController.listarTodos());
            table.refresh();
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Falha ao atualizar tabela: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
