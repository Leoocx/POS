package com.system.pos.pos.controller;

import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.TipoCliente;
import com.system.pos.pos.service.ClienteService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ClientesController {

    @FXML
    private TextField txtNome, txtEmail, txtTelefone, txtCPF, txtEndereco;
    @FXML
    private ComboBox<TipoCliente> comboTipo;

    @FXML
    private TableView<Cliente> tabelaClientes;

    @FXML
    private Button btnCadastrar, btnAtualizar, btnRemover;

    private ClienteService clienteService;

    public ClientesController() {
        this.clienteService = new ClienteService();
    }

    @FXML
    public void initialize() {
        try {
            comboTipo.getItems().setAll(TipoCliente.values());
            carregarClientesNaTabela();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void carregarClientesNaTabela() {
        try {
            ObservableList<Cliente> clientes = clienteService.listarClientes();
            tabelaClientes.setItems(clientes);
        } catch (SQLException e) {
            System.out.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    public void cadastrarCliente() {
        try {
            Cliente cliente = new Cliente(txtNome.getText(), txtTelefone.getText(),txtCPF.getText(), txtEmail.getText(), txtEndereco.getText());

            clienteService.cadastrarCliente(cliente);
            carregarClientesNaTabela();
            limparCampos();

        } catch (Exception ex) {
            System.out.println("Erro ao cadastrar cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void atualizarCliente() {
        try {
            Cliente cliente = new Cliente(txtNome.getText(), txtTelefone.getText(),txtCPF.getText(), txtEmail.getText(), txtEndereco.getText());


            clienteService.atualizarCliente(cliente);
            carregarClientesNaTabela();
            limparCampos();

        } catch (Exception ex) {
            System.out.println("Erro ao atualizar cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void removerCliente() {
        try {
            Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
            if (clienteSelecionado != null) {
                clienteService.removerCliente(clienteSelecionado);
                carregarClientesNaTabela();
                limparCampos();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover cliente: " + e.getMessage());
        }
    }

    public void limparCampos() {
        txtNome.clear();
        txtEmail.clear();
        txtTelefone.clear();
        txtCPF.clear();
        txtEndereco.clear();
        comboTipo.getSelectionModel().clearSelection();
    }
}
