package com.system.pos.pos.view;

import java.sql.SQLException;

import com.system.pos.pos.controller.ClientesController;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.report.RelatorioClientes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CadastroClienteView {

    @FXML
    private TextField nome;
    @FXML
    private TextField telefone;
    @FXML
    private TextField cpf;
    @FXML
    private TextField email;
    @FXML
    private TextField endereco;
    @FXML
    private TableView<Cliente> table;

    private ObservableList<Cliente> clientes;
    private ClientesController clientesController;

    @FXML
    public void initialize() throws SQLException {
        this.clientesController = new ClientesController();
        this.clientes = FXCollections.observableArrayList(); 
        inicializarTabela();
        atualizarTabela(); 
    }

    @FXML
    public void cadastrarClienteBTN() {
        try {
            if (nome.getText().isEmpty() || telefone.getText().isEmpty() || cpf.getText().isEmpty() || email.getText().isEmpty() || endereco.getText().isEmpty()) {
                mostrarAlerta("Aviso", "Campos obrigatórios não foram preenchidos.", Alert.AlertType.WARNING);
            } else {
                Cliente cliente = new Cliente(
                        nome.getText(),
                        telefone.getText(),
                        Integer.parseInt(cpf.getText()),
                        email.getText(),
                        endereco.getText()
                );
                clientesController.cadastrarCliente(cliente);
                atualizarTabela();
                limparCampos();
                mostrarAlerta("Sucesso", "Cliente cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao cadastrar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
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
                    atualizarTabela();
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

    @FXML
    private void removerClienteBTN() {
        Cliente selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                clientesController.removerCliente(selecionado.getId());
                atualizarTabela();
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
    public void gerarPDFButton() {
        RelatorioClientes.gerarPDF(null);
    }

    private void inicializarTabela() {

        TableColumn<Cliente, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> nomeColumn = new TableColumn<>("NOME");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> telefoneColumn = new TableColumn<>("TELEFONE");
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Cliente, Integer> cpfColumn = new TableColumn<>("CPF");
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf")); 

        TableColumn<Cliente, String> emailColumn = new TableColumn<>("EMAIL");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Cliente, String> enderecoColumn = new TableColumn<>("ENDERECO");
        enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        table.setItems(clientes);
        table.getColumns().setAll(idColumn,nomeColumn, telefoneColumn, cpfColumn, emailColumn, enderecoColumn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCampos(newSelection);
            }
        });
    }

    private boolean validarCampos() {
        return !(nome.getText().isBlank() || email.getText().isBlank() || cpf.getText().isBlank() || telefone.getText().isBlank());
    }

    private void atualizarClienteFromInputs(Cliente cliente) {
        cliente.setNome(nome.getText());
        cliente.setCpf(Integer.parseInt(cpf.getText()));
        cliente.setTelefone(telefone.getText());
        cliente.setEmail(email.getText());
        cliente.setEndereco(endereco.getText());
    }

    private void preencherCampos(Cliente cliente) {
        nome.setText(cliente.getNome());
        cpf.setText(String.valueOf(cliente.getCpf()));
        email.setText(cliente.getEmail());
        telefone.setText(cliente.getTelefone());
        endereco.setText(cliente.getEndereco());
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void atualizarTabela() throws SQLException {
        if (clientes != null) {
            clientes.setAll(clientesController.listarTodos());
        }
    }

    @FXML
    private void clearFields() {
        limparCampos();
    }
    
    private void limparCampos() {
        nome.clear();
        cpf.clear();
        email.clear();
        telefone.clear();
        endereco.clear();
        table.getSelectionModel().clearSelection();
    }
}
