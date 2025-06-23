package com.system.pos.pos.service;

import com.system.pos.pos.database.ClienteDAO;
import com.system.pos.pos.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {
    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public void cadastrarCliente(Cliente cliente) throws SQLException {
        validarCliente(cliente);
        clienteDAO.adicionarCliente(cliente);
    }

    public void atualizarCliente(Cliente cliente) throws SQLException {
        validarCliente(cliente);
        clienteDAO.atualizarCliente(cliente);
    }

    public void removerCliente(int id) throws SQLException {
        clienteDAO.removerCliente(id);
    }

    public List<Cliente> listarTodos() throws SQLException {
        return clienteDAO.listarClientes();
    }

    public Cliente buscarPorId(int id) throws SQLException {
        return clienteDAO.buscarClientePorCodigo(id);
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