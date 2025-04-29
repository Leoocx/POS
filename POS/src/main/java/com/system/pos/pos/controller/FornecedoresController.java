package com.system.pos.pos.controller;

import com.system.pos.pos.database.FornecedorDAO;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.service.FornecedorService;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class FornecedoresController {

    private FornecedorDAO fornecedorDAO;

    public boolean cadastrarFornecedor(Fornecedor fornecedor) throws SQLException {
        FornecedorService.cadastrarFornecedor(fornecedor);
        System.out.println("Fornecedor cadastrado!");
        return true;
    }

    public void atualizarFornecedor(){

    }

    public void excluirFornecedor(){
        }

    public void exibirFornecedores() throws SQLException {
        fornecedorDAO.exibirFornecedores();
    }


}
