package com.system.pos.pos.service;

import com.system.pos.pos.database.FornecedorDAO;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;

import java.sql.SQLException;

public class FornecedorService {

    static FornecedorDAO fornecedorDAO;

    public static void cadastrarFornecedor (Fornecedor fornecedor) throws SQLException {
        fornecedorDAO.adicionarFornecedor(fornecedor);
        System.out.println("Fornecedor cadastrado!");
    }
    public static void atualizarFornecedor(Fornecedor fornecedor) throws SQLException{
        fornecedorDAO.atualizarFornecedor(fornecedor);
        System.out.println("Fornecedor atualizado!");
    }
    public static void removerFornecedor(Fornecedor fornecedor) throws SQLException{
        fornecedorDAO.removerFornecedor(fornecedor);
        System.out.println("Fornecedor removido!");
    }

}
