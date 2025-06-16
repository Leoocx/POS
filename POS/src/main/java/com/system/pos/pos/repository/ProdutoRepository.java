package com.system.pos.pos.repository;

import com.system.pos.pos.model.Produto;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.List;

public interface ProdutoRepository {
    void adicionarProduto(Produto produto) throws SQLException;

    void atualizarProduto(Produto produto) throws SQLException;

    void removerProduto(int id) throws SQLException;

    ObservableList<Produto> listarTodosProdutos() throws SQLException;

    boolean atualizarEstoque(int idProduto, int quantidadeVendida) throws SQLException;

    List<Produto> buscarProdutosPorNome(String nome) throws SQLException;

    boolean validarProduto(Produto produto);

    void gerarRelatorioProdutos(TableView<Produto> tabela);
}