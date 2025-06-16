package com.system.pos.pos.controller;

import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.service.ClienteService;

import java.sql.SQLException;
import java.util.List;

public class ClientesController {
    private final ClienteService clienteService;

    public ClientesController() {
        this.clienteService = new ClienteService();
    }

    public void cadastrarCliente(Cliente cliente) throws SQLException {
        clienteService.cadastrarCliente(cliente);
    }

    public void atualizarCliente(Cliente cliente) throws SQLException {
        clienteService.atualizarCliente(cliente);
    }

    public void removerCliente(int id) throws SQLException {
        clienteService.removerCliente(id);
    }

    public List<Cliente> listarTodos() throws SQLException {
        return clienteService.listarTodos();
    }

    public Cliente buscarPorId(int id) throws SQLException {
        return clienteService.buscarPorId(id);
    }
}