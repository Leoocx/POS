package com.system.pos.pos.service;
import com.system.pos.pos.database.ProdutoDAO;
import com.system.pos.pos.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoService {
    private ProdutoDAO produtoDAO;

    public ProdutoService(){
        this.produtoDAO=new ProdutoDAO();
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
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    public ObservableList<Produto> buscarProdutos(String termo) {
        try {
            List<Produto> todosProdutos = produtoDAO.showAll();
            return FXCollections.observableArrayList(
                    todosProdutos.stream()
                            .filter(p -> String.valueOf(p.getId()).contains(termo) ||
                                    p.getNome().toLowerCase().contains(termo.toLowerCase()))
                            .collect(Collectors.toList())
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    public boolean atualizarEstoque(int idProduto, int quantidadeVendida) {
        try {
            Produto produto = produtoDAO.searchByID(idProduto);
            if (produto != null) {
                // Verifica se a quantidade disponível é suficiente
                if (produto.getQuantidade() >= quantidadeVendida) {
                    produto.setQuantidade(produto.getQuantidade() - quantidadeVendida);
                    produtoDAO.updateProduto(produto);
                    return true; // Atualização bem-sucedida
                } else {
                    System.out.println("Quantidade insuficiente em estoque.");
                    return false; // Quantidade insuficiente
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Produto não encontrado ou erro
    }

}