package com.system.pos.pos.repository;

import com.system.pos.pos.model.Fornecedor;

import java.sql.SQLException;

public interface FornecedorRepository {
    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException;
    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException;
    public void removerFornecedor(Fornecedor fornecedor) throws SQLException;
    public Fornecedor exibirFornecedorPorId(int id) throws SQLException;

}
