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

    public void cadastrarCliente(Cliente cliente) throws SQLException {
        clienteDAO.adicionarCliente(cliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }


}
