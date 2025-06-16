package com.system.pos.pos.controller;

import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.service.FornecedorService;

import java.sql.SQLException;
import java.util.List;

public class FornecedoresController {
    private final FornecedorService fornecedorService;

    public FornecedoresController() {
        this.fornecedorService = new FornecedorService();
    }

    public void cadastrarFornecedor(Fornecedor fornecedor) throws SQLException {
        fornecedorService.adicionarFornecedor(fornecedor);
    }

    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException {
        fornecedorService.atualizarFornecedor(fornecedor);
    }

    public void excluirFornecedor(Fornecedor fornecedor) throws SQLException {
        fornecedorService.removerFornecedor(fornecedor);
    }

    public List<Fornecedor> listarTodos() throws SQLException {
        return fornecedorService.listarTodos();
    }
}