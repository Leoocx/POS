package com.system.pos.pos.service;

import com.system.pos.pos.database.ProdutoDAO;
import com.system.pos.pos.model.Produto;

import java.sql.Connection;
import java.util.List;

public class ProdutoService {
    private ProdutoDAO produtoDAO;

    public ProdutoService(Connection connection) {
        produtoDAO = new ProdutoDAO();
        produtoDAO.setConnection(connection);
    }

    public boolean adicionarProduto(Produto produto) {
        return produtoDAO.inserir(produto);
    }

    public boolean atualizarProduto(Produto produto) {
        return produtoDAO.alterar(produto);
    }

    public boolean removerProduto(Produto produto) {
        return produtoDAO.remover(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoDAO.listar();
    }

    public Produto buscarProduto(int cdProduto) {
        Produto produto = new Produto();
        produto.setCdProduto(cdProduto);
        return produtoDAO.buscar(produto);
    }
}