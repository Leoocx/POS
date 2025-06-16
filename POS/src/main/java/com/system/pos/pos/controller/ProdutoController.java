package com.system.pos.pos.controller;

import com.system.pos.pos.model.Categoria;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.model.SubCategoria;
import com.system.pos.pos.service.ProdutoService;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController() throws SQLException {
        this.produtoService = new ProdutoService();
    }

    public void cadastrarProduto(String nome, int quantidade, BigDecimal preco, String status,
                                 String codigoBarras, Categoria categoria, SubCategoria subCategoria) throws SQLException {
        Produto produto = new Produto(nome, quantidade, preco, status, codigoBarras, categoria, subCategoria);
        produtoService.adicionarProduto(produto);
    }

    public void atualizarProduto(int id, String nome, int quantidade, BigDecimal preco, String status,
                                 String codigoBarras, Categoria categoria, SubCategoria subCategoria) throws SQLException {
        Produto produto = new Produto(id, nome, quantidade, preco, status, codigoBarras, categoria, subCategoria);
        produtoService.atualizarProduto(produto);
    }

    public void removerProduto(int id) throws SQLException {
        produtoService.removerProduto(id);
    }

    public ObservableList<Produto> listarProdutos() throws SQLException {
        return produtoService.listarTodosProdutos();
    }

    public boolean validarDadosProduto(String nome, String quantidadeStr, String precoStr,
                                       String status, Categoria categoria, SubCategoria subCategoria) {
        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            BigDecimal preco = new BigDecimal(precoStr.replace(",", "."));

            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setQuantidade(quantidade);
            produto.setPreco(preco);
            produto.setStatus(status);
            produto.setCategoria(categoria);
            produto.setSubCategoria(subCategoria);

            return produtoService.validarProduto(produto);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void gerarRelatorio(TableView<Produto> tabela) {
        produtoService.gerarRelatorioProdutos(tabela);
    }
}