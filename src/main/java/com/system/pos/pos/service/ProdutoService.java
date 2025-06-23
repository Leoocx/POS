package com.system.pos.pos.service;

import com.system.pos.pos.database.ProdutoDAO;
import com.system.pos.pos.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoService {
    private final ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    public void adicionarProduto(Produto produto) throws SQLException {
        produtoDAO.insertProduto(produto);
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        produtoDAO.updateProduto(produto);
    }

    public void removerProduto(int id) throws SQLException {
        produtoDAO.deleteProduto(id);
    }

    public Produto buscarPorId(int id) throws SQLException {
        return produtoDAO.searchByID(id);
    }

    public List<Produto> listarTodos() throws SQLException {
        return produtoDAO.showAll();
    }

    public ObservableList<Produto> listarProdutos() {
        try {
            return FXCollections.observableArrayList(produtoDAO.showAll());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos", e);
        }
    }

    public ObservableList<Produto> buscarProdutos(String termo) {
        try {
            List<Produto> todosProdutos = produtoDAO.showAll();
            return FXCollections.observableArrayList(
                    todosProdutos.stream()
                            .filter(p -> String.valueOf(p.getId()).contains(termo) ||
                                    p.getNome().toLowerCase().contains(termo.toLowerCase()) ||
                                    (p.getCodigoBarras() != null && p.getCodigoBarras().contains(termo)))
                            .collect(Collectors.toList())
            );
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos", e);
        }
    }

    public boolean atualizarEstoque(int idProduto, int quantidadeVendida) throws SQLException {
        Produto produto = produtoDAO.searchByID(idProduto);
        if (produto != null) {
            if (produto.getQuantidade() >= quantidadeVendida) {
                produto.setQuantidade(produto.getQuantidade() - quantidadeVendida);
                produtoDAO.updateProduto(produto);
                return true;
            }
        }
        return false;
    }
}