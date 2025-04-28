package com.system.pos.pos.controller;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.service.ProdutoService;

import java.sql.Connection;
import java.util.List;

public class ProdutoController {
    private ProdutoService produtoService;

    public ProdutoController(Connection connection) {
        produtoService = new ProdutoService(connection);
    }

    public boolean adicionarProduto(Produto produto) {
        return produtoService.adicionarProduto(produto);
    }

    public boolean atualizarProduto(Produto produto) {
        return produtoService.atualizarProduto(produto);
    }

    public boolean removerProduto(int cdProduto) {
        Produto produto = new Produto();
        produto.setCdProduto(cdProduto);
        return produtoService.removerProduto(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    public Produto buscarProduto(int cdProduto) {
        return produtoService.buscarProduto(cdProduto);
    }
}