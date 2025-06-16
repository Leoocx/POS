package com.system.pos.pos.controller;

import com.system.pos.pos.model.Produto;
import com.system.pos.pos.service.ProdutoService;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController() {
        this.produtoService = new ProdutoService();
    }

    public void adicionarProduto(Produto produto) throws SQLException {
        produtoService.adicionarProduto(produto);
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        produtoService.atualizarProduto(produto);
    }

    public void removerProduto(int id) throws SQLException {
        produtoService.removerProduto(id);
    }

    public List<Produto> listarTodos() throws SQLException {
        return produtoService.listarTodos();
    }

    public ObservableList<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    public ObservableList<Produto> buscarProdutos(String termo) {
        return produtoService.buscarProdutos(termo);
    }

    public boolean atualizarEstoque(int idProduto, int quantidadeVendida) throws SQLException {
        return produtoService.atualizarEstoque(idProduto, quantidadeVendida);
    }
}