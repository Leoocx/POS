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
        clienteDAO.adicionarCliente(cliente);  
        System.out.println("Cliente cadastrado com sucesso!");
    }

    public void atualizarCliente(Cliente cliente) throws SQLException {
        clienteDAO.atualizarCliente(cliente);
        System.out.println("Cliente atualizado com sucesso!");
    }

    public void removerCliente(int codigo) throws SQLException {
        clienteDAO.removerCliente(codigo); 
        System.out.println("Cliente removido com sucesso!");
    }

    public Cliente buscarClientePorCodigo(int codigo) throws SQLException {
        return clienteDAO.buscarClientePorCodigo(codigo);  
    }

    public List<Cliente> listarTodos() throws SQLException {
        return clienteDAO.listarClientes(); 
    }
}
