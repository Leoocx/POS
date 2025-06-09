package com.system.pos.pos.repository;

import com.system.pos.pos.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteRepository {

    List<Cliente> listarClientes() throws SQLException;
    Cliente buscarClientePorCodigo(int codigo) throws SQLException;
    void removerCliente(int codigo) throws SQLException;
    void atualizarCliente(Cliente cliente) throws SQLException;
    void adicionarCliente(Cliente cliente) throws SQLException;

}
