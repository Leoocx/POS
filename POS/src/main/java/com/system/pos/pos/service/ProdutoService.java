package com.system.pos.pos.service;

import com.system.pos.pos.database.ProdutoDAO;
import com.system.pos.pos.model.Produto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProdutoService {
    private ProdutoDAO produtoDAO;

    public boolean adicionarProduto(Produto produto) throws SQLException{
        produtoDAO.adicionarProduto(produto);
        return true;
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        produtoDAO.atualizarProduto(produto);
    }

    public void removerProduto(int codigo) throws SQLException {
        produtoDAO.removerProduto(codigo);
    }

    public List<Produto> listarProdutos() throws SQLException {
        return produtoDAO.listarProdutos();
    }

}