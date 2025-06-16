package com.system.pos.pos.service;

import com.system.pos.pos.database.FornecedorDAO;
import com.system.pos.pos.model.Fornecedor;

import java.sql.SQLException;
import java.util.List;

public class FornecedorService {
    private final FornecedorDAO fornecedorDAO;

    public FornecedorService() {
        this.fornecedorDAO = new FornecedorDAO();
    }

    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException {
        validarFornecedor(fornecedor);
        fornecedorDAO.adicionarFornecedor(fornecedor);
    }

    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException {
        validarFornecedor(fornecedor);
        fornecedorDAO.atualizarFornecedor(fornecedor);
    }

    public void removerFornecedor(Fornecedor fornecedor) throws SQLException {
        fornecedorDAO.removerFornecedor(fornecedor);
    }

    public List<Fornecedor> listarTodos() throws SQLException {
        return fornecedorDAO.showAll();
    }

    private void validarFornecedor(Fornecedor fornecedor) {
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do fornecedor é obrigatório");
        }
        if (fornecedor.getDocumento() == null || fornecedor.getDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ do fornecedor é obrigatório");
        }
    }
}