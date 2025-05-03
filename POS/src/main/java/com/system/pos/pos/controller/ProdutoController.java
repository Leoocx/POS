package com.system.pos.pos.controller;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.service.ProdutoService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProdutoController {
    private ProdutoService produtoService;

    public void adicionarProduto(Produto produto) throws SQLException {
        produtoService.adicionarProduto(produto);
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        produtoService.atualizarProduto(produto);
    }

    public void removerProduto(int cdProduto) throws SQLException {
        produtoService.removerProduto(cdProduto);
    }

    public List<Produto> listarProdutos() throws SQLException {
        return produtoService.listarProdutos();
    }
}