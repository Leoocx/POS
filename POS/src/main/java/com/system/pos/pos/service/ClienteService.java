package com.system.pos.pos.service;

import com.system.pos.pos.database.ClienteDAO;
import com.system.pos.pos.model.Cliente;

import java.sql.Connection;
import java.sql.SQLException;

public class ClienteService {

    static ClienteDAO clienteDAO;
    private final Connection connection;

    public ClienteService(Connection connection){
        this.connection=connection;
    }

    public static void cadastrarCliente(Cliente cliente) throws SQLException {
        clienteDAO.adicionarCliente(cliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    public static void atualizarCliente(Cliente cliente) throws SQLException{
         clienteDAO.atualizarCliente(cliente);
    }

    public void removerCliente(Cliente cliente) throws SQLException{
        clienteDAO.removerCliente(cliente);
    }

    public void buscarClientePorCodigo(int codigo) throws SQLException{
        clienteDAO.buscarClientePorCodigo(codigo);
    }

}
