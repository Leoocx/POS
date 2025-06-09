package com.system.pos.pos.controller;

import com.system.pos.pos.model.Produto;
import com.system.pos.pos.service.ProdutoService;

import java.sql.SQLException;
import java.util.List;

public class ProdutoController {
    private ProdutoService produtoService;

    public ProdutoController(){
        this.produtoService=new ProdutoService();
    }

    public void adicionarProduto(Produto produto) {
        try {
            produtoService.adicionarProduto(produto);
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar produto: " + e.getMessage());
        }
    }

    public void atualizarProduto(Produto produto) {
        try {
            produtoService.atualizarProduto(produto);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void removerProduto(int id) {
        try {
            produtoService.removerProduto(id);
        } catch (SQLException e) {
            System.err.println("Erro ao remover produto: " + e.getMessage());
        }
    }

    public Produto buscarPorId(int id) {
        try {
            return produtoService.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
            return null;
        }
    }

    public List<Produto> listarTodos() {
        try {
            return produtoService.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            return null;
        }
    }
}