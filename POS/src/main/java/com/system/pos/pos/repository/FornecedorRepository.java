package com.system.pos.pos.repository;

import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;

import java.sql.SQLException;
import java.util.List;

public interface FornecedorRepository {
    int salvarEndereco(Endereco endereco) throws SQLException;

    void adicionarFornecedor(Fornecedor fornecedor) throws SQLException;
    List<Fornecedor> showAll() throws SQLException;
    void atualizarFornecedor(Fornecedor fornecedor) throws SQLException;
    void removerFornecedor(Fornecedor fornecedor) throws SQLException;

}
