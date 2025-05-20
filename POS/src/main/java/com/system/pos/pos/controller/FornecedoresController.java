
package com.system.pos.pos.controller;

import com.system.pos.pos.database.FornecedorDAO;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Conta;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.service.FornecedorService;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedoresController {

    private FornecedorService fornecedorService;

    public FornecedoresController(){
        this.fornecedorService=new FornecedorService();
    }

    public void cadastrarFornecedor(Fornecedor fornecedor) throws SQLException {
        try{
            fornecedorService.adicionarFornecedor(fornecedor);
        } catch(SQLException e){
            System.out.println("Erro ao tentar cadastrar fornecedor : " + e.getMessage()); 
        }
    }

    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException{
        try{
            fornecedorService.atualizarFornecedor(fornecedor);
        } catch  (SQLException e){
           System.out.println("Erro ao tentar atualizar fornecedor: "+ e.getMessage());
        }
    }

    public void excluirFornecedor(Fornecedor fornecedor) throws  SQLException{
        try{
            fornecedorService.removerFornecedor(fornecedor);
        } catch(SQLException e){
            System.out.println("Erro ao tentar excluir fornecedor : "+e.getMessage());
        }
    }
      public List<Fornecedor> listarTodos() throws SQLException {
        return fornecedorService.listarTodos();
    }

}

