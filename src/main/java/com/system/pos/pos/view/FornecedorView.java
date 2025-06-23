package com.system.pos.pos.view;

import com.system.pos.pos.controller.FornecedoresController;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.service.EnderecoService;
import com.system.pos.pos.report.ReportPrinter;
import com.system.pos.pos.utils.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;

import static com.system.pos.pos.utils.AlertUtil.mostrarAlerta;

public class FornecedorView {
    @FXML private TextField nome;
    @FXML private TextField email;
    @FXML private TextField telefone;
    @FXML private TextField cnpj;
    @FXML private TextField representante;
    @FXML private TextField telefoneRepresentante;
    @FXML private TextField emailRepresentante;
    @FXML private TextField cep;
    @FXML private TableView<Fornecedor> table;

    private ObservableList<Fornecedor> fornecedores;
    private final FornecedoresController fornecedorController;
    private final EnderecoService enderecoService;

    public FornecedorView() {
        this.fornecedorController = new FornecedoresController();
        this.enderecoService = new EnderecoService();
    }

    @FXML
    public void initialize() {
        this.fornecedores = FXCollections.observableArrayList();
        inicializarTabela();
        carregarFornecedores();
        configurarBuscaCEP();
    }

    private void configurarBuscaCEP() {
        cep.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !cep.getText().isEmpty()) {
                buscarEnderecoPorCEP();
            }
        });
    }

    private void buscarEnderecoPorCEP() {
        try {
            Endereco endereco = enderecoService.buscarEnderecoPorCep(cep.getText());
        } catch (Exception e) {
            mostrarAlerta("Erro na busca", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void carregarFornecedores() {
        try {
            fornecedores.setAll(fornecedorController.listarTodos());
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Falha ao carregar fornecedores: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cadastroFornecedorBTN() {
        try {
            if (validarCampos()) {
                Endereco endereco = criarEnderecoFromInputs();
                Fornecedor fornecedor = new Fornecedor(
                        nome.getText(),
                        telefone.getText(),
                        cnpj.getText(),
                        email.getText(),
                        endereco
                );
                fornecedor.setRepresentante(representante.getText());
                fornecedor.setTelefoneRepresentante(telefoneRepresentante.getText());
                fornecedor.setEmailRepresentante(emailRepresentante.getText());

                fornecedorController.cadastrarFornecedor(fornecedor);
                carregarFornecedores();
                limparCampos();
                mostrarAlerta("Sucesso", "Fornecedor cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao cadastrar fornecedor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Endereco criarEnderecoFromInputs() {
        try {
            // Busca o endereço completo pelo CEP
            return enderecoService.buscarEnderecoPorCep(cep.getText());
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao buscar endereço: " + e.getMessage(), Alert.AlertType.ERROR);
            return new Endereco(); // Retorna um endereço vazio se tiver algum erro que salva no banco com valores null
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
                    carregarFornecedores();
                    limparCampos();
                    mostrarAlerta("Sucesso", "Fornecedor atualizado com sucesso!", Alert.AlertType.INFORMATION);
                }
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao atualizar fornecedor: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum fornecedor selecionado", Alert.AlertType.WARNING);
        }
    }

    private void atualizarFornecedorFromInputs(Fornecedor fornecedor) {
        fornecedor.setNome(nome.getText());
        fornecedor.setTelefone(telefone.getText());
        fornecedor.setEmail(email.getText());
        fornecedor.setRepresentante(representante.getText());
        fornecedor.setTelefoneRepresentante(telefoneRepresentante.getText());
        fornecedor.setEmailRepresentante(emailRepresentante.getText());

        Endereco endereco = criarEnderecoFromInputs();
        fornecedor.setEndereco(endereco);
    }

    @FXML
    private void deleteFornecedorBTN() {
        Fornecedor selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                fornecedorController.excluirFornecedor(selecionado);
                carregarFornecedores();
                limparCampos();
                mostrarAlerta("Sucesso", "Fornecedor removido com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao remover fornecedor: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum fornecedor selecionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void gerarPDFButton() {
        ReportPrinter.imprimirTabela(table);
    }

    @FXML
    private void limparCampos() {
        nome.clear();
        telefone.clear();
        cnpj.clear();
        email.clear();
        representante.clear();
        telefoneRepresentante.clear();
        emailRepresentante.clear();
        cep.clear();
        table.getSelectionModel().clearSelection();
    }

    private void inicializarTabela() {
        TableColumn<Fornecedor, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Fornecedor, String> nomeColumn = new TableColumn<>("NOME");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Fornecedor, String> cnpjColumn = new TableColumn<>("CNPJ");
        cnpjColumn.setCellValueFactory(new PropertyValueFactory<>("documento"));

        TableColumn<Fornecedor, String> telefoneColumn = new TableColumn<>("TELEFONE");
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Fornecedor, String> emailColumn = new TableColumn<>("EMAIL");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Fornecedor, String> enderecoColumn = new TableColumn<>("ENDEREÇO");
        enderecoColumn.setCellValueFactory(cellData -> {
            Endereco endereco = cellData.getValue().getEndereco();
            return new SimpleStringProperty(endereco != null ? endereco.toString() : "");
        });

        TableColumn<Fornecedor, LocalDate> dataCadastroColumn = new TableColumn<>("DATA CADASTRO");
        dataCadastroColumn.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));

        table.setItems(fornecedores);
        table.getColumns().setAll(idColumn, nomeColumn, cnpjColumn, telefoneColumn,
                emailColumn, enderecoColumn, dataCadastroColumn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCampos(newSelection);
            }
        });
    }

    private void preencherCampos(Fornecedor fornecedor) {
        nome.setText(fornecedor.getNome());
        cnpj.setText(fornecedor.getDocumento());
        telefone.setText(fornecedor.getTelefone());
        email.setText(fornecedor.getEmail());
        representante.setText(fornecedor.getRepresentante());
        telefoneRepresentante.setText(fornecedor.getTelefoneRepresentante());
        emailRepresentante.setText(fornecedor.getEmailRepresentante());

        Endereco endereco = fornecedor.getEndereco();
        if (endereco != null) {
            cep.setText(endereco.getCep());
        }
    }

    private boolean validarCampos() {
        if (nome.getText().isBlank() || cnpj.getText().isBlank() || cep.getText().isBlank()) {
            mostrarAlerta("Aviso", "Nome, CNPJ e CEP são campos obrigatórios", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}