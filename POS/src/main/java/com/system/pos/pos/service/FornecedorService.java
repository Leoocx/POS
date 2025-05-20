
package com.system.pos.pos.service;

import com.system.pos.pos.database.FornecedorDAO;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Fornecedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class FornecedorService {

    private FornecedorDAO fornecedorDAO;

    public FornecedorService() {
        this.fornecedorDAO = new FornecedorDAO(); 
    }

    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException {
        fornecedorDAO.adicionarFornecedor(fornecedor);
        System.out.println("Fornecedor cadastrado!");
    }

    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException {
        fornecedorDAO.atualizarFornecedor(fornecedor);
        System.out.println("Fornecedor atualizado!");
    }

    public void removerFornecedor(Fornecedor fornecedor) throws SQLException {
        fornecedorDAO.removerFornecedor(fornecedor);
        System.out.println("Fornecedor removido!");
    }
    public List<Fornecedor> listarTodos() throws SQLException {
        return fornecedorDAO.showAll(); 
    }

}

