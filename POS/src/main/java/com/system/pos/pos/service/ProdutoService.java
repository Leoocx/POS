package com.system.pos.pos.service;

import com.system.pos.pos.database.ProdutoDAO;
import com.system.pos.pos.model.Produto;

import java.sql.Connection;
import java.sql.SQLException;

public class ProdutoService {

    ProdutoDAO produtoDAO;
    private final Connection connection;

    public ProdutoService(Connection connection){
        this.connection=connection;
    }

    public void cadastrarProduto(Produto produto) throws SQLException {
        produtoDAO.adicionarProduto(produto);
        System.out.println("Produto cadastrado com sucesso!");
    }



}
