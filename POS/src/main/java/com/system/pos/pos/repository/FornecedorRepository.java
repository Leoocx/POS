package com.system.pos.pos.repository;

import com.system.pos.pos.model.Fornecedor;

import java.sql.SQLException;
import java.util.List;

public interface FornecedorRepository {
    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException;
    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException;
    public void removerFornecedor(Fornecedor fornecedor) throws SQLException;
    public List<Fornecedor> showAll() throws SQLException;

}
