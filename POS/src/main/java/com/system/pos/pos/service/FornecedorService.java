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




}
