package com.system.pos.pos.view;

import com.system.pos.pos.controller.ClientesController;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Endereco;
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

public class ClienteView {
    @FXML private TextField nome;
    @FXML private TextField telefone;
    @FXML private TextField cpf;
    @FXML private TextField email;
    @FXML private TextField cep;
    @FXML private TableView<Cliente> table;

    private ObservableList<Cliente> clientes;
    private final ClientesController clientesController;
    private final EnderecoService enderecoService;

    public ClienteView() {
        this.clientesController = new ClientesController();
        this.enderecoService = new EnderecoService();
    }

    @FXML
    public void initialize() {
        this.clientes = FXCollections.observableArrayList();
        inicializarTabela();
        carregarClientes();
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

    private void carregarClientes() {
        try {
            clientes.setAll(clientesController.listarTodos());
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Falha ao carregar clientes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cadastrarClienteBTN() {
        try {
            if (validarCampos()) {
                Endereco endereco = criarEnderecoFromInputs();
                Cliente cliente = new Cliente(
                        nome.getText(),
                        telefone.getText(),
                        cpf.getText(),
                        email.getText(),
                        endereco
                );

                clientesController.cadastrarCliente(cliente);
                carregarClientes();
                limparCampos();
                mostrarAlerta("Sucesso", "Cliente cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao cadastrar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Endereco criarEnderecoFromInputs() {
        try {
            // Busca o endereço completo pelo CEP
            return enderecoService.buscarEnderecoPorCep(cep.getText());
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao buscar endereço: " + e.getMessage(), Alert.AlertType.ERROR);
            return new Endereco(); // Retorna um endereço vazio
        }
    }

    @FXML
    private void atualizarClienteBTN() {
        Cliente selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                if (validarCampos()) {
                    atualizarClienteFromInputs(selecionado);
                    clientesController.atualizarCliente(selecionado);
                    carregarClientes();
                    limparCampos();
                    mostrarAlerta("Sucesso", "Cliente atualizado com sucesso!", Alert.AlertType.INFORMATION);
                }
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao atualizar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum cliente selecionado", Alert.AlertType.WARNING);
        }
    }

    private void atualizarClienteFromInputs(Cliente cliente) {
        cliente.setNome(nome.getText());
        cliente.setTelefone(telefone.getText());
        cliente.setEmail(email.getText());

        Endereco endereco = criarEnderecoFromInputs();
        cliente.setEndereco(endereco);
    }

    @FXML
    private void removerClienteBTN() {
        Cliente selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                clientesController.removerCliente(selecionado.getId());
                carregarClientes();
                limparCampos();
                mostrarAlerta("Sucesso", "Cliente removido com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao remover cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum cliente selecionado", Alert.AlertType.WARNING);
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
        cpf.clear();
        email.clear();
        cep.clear();
        table.getSelectionModel().clearSelection();
    }

    private void inicializarTabela() {
        TableColumn<Cliente, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> nomeColumn = new TableColumn<>("NOME");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> cpfColumn = new TableColumn<>("CPF");
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("documento"));

        TableColumn<Cliente, String> telefoneColumn = new TableColumn<>("TELEFONE");
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Cliente, String> emailColumn = new TableColumn<>("EMAIL");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Cliente, String> enderecoColumn = new TableColumn<>("ENDEREÇO");
        enderecoColumn.setCellValueFactory(cellData -> {
            Endereco endereco = cellData.getValue().getEndereco();
            return new SimpleStringProperty(endereco != null ? endereco.toString() : "");
        });

        TableColumn<Cliente, LocalDate> dataCadastroColumn = new TableColumn<>("DATA CADASTRO");
        dataCadastroColumn.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));

        table.setItems(clientes);
        table.getColumns().setAll(idColumn, nomeColumn, cpfColumn, telefoneColumn,
                emailColumn, enderecoColumn, dataCadastroColumn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCampos(newSelection);
            }
        });
    }

    private void preencherCampos(Cliente cliente) {
        nome.setText(cliente.getNome());
        telefone.setText(cliente.getTelefone());
        cpf.setText(cliente.getDocumento());
        email.setText(cliente.getEmail());

        Endereco endereco = cliente.getEndereco();
        if (endereco != null) {
            cep.setText(endereco.getCep());
        }
    }

    private boolean validarCampos() {
        if (nome.getText().isBlank() || cpf.getText().isBlank() || cep.getText().isBlank()) {
            mostrarAlerta("Aviso", "Nome, CPF e CEP são campos obrigatórios", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}