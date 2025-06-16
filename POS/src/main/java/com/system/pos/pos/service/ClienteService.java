package com.system.pos.pos.service;

import com.system.pos.pos.database.ClienteDAO;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Endereco;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Optional;

public class ClienteService {
    private final ClienteDAO clienteDAO;
    private final ObservableList<Cliente> clientesObservable;

    public ClienteService() throws SQLException {
        this.clienteDAO = new ClienteDAO();
        this.clientesObservable = FXCollections.observableArrayList();
        this.clientesObservable.setAll(clienteDAO.listarTodos());
    }

    public ObservableList<Cliente> listarTodosObservable() {
        return clientesObservable;
    }

    public Optional<Cliente> cadastrarCliente(Cliente cliente) {
        try {
            validarCliente(cliente);
            clienteDAO.adicionarCliente(cliente);
            atualizarListaClientes();
            return Optional.of(cliente);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Cliente> atualizarCliente(int id, String nome, String telefone, String cpf, String email, Endereco endereco) {
        try {
            Cliente cliente = new Cliente(nome, telefone, cpf, email, endereco);
            cliente.setId(id);
            validarCliente(cliente);
            clienteDAO.atualizarCliente(cliente);
            atualizarListaClientes();
            return Optional.of(cliente);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean removerCliente(int id) {
        try {
            clienteDAO.removerCliente(id);
            atualizarListaClientes();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void atualizarListaClientes() {
        try {
            clientesObservable.setAll(clienteDAO.listarTodos());
        } catch (SQLException e) {
            // Tratar exceção
        }
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        if (cliente.getDocumento() == null || cliente.getDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do cliente é obrigatório");
        }
    }
}
