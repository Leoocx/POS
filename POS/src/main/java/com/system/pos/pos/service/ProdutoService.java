package com.system.pos.pos.service;

import com.system.pos.pos.database.ProdutoDAO;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.report.ReportPrinter;
import com.system.pos.pos.repository.ProdutoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import java.util.List;
import java.sql.SQLException;
import java.math.BigDecimal;

public class ProdutoService implements ProdutoRepository {
    private final ProdutoDAO produtoDAO;

    public ProdutoService() throws SQLException {
        this.produtoDAO = new ProdutoDAO();
    }

    @Override
    public void adicionarProduto(Produto produto) throws SQLException {
        if (validarProduto(produto)) {
            produtoDAO.inserir(produto);
        }
    }

    @Override
    public void atualizarProduto(Produto produto) throws SQLException {
        if (validarProduto(produto)) {
            produtoDAO.atualizar(produto);
        }
    }

    @Override
    public void removerProduto(int id) throws SQLException {
        produtoDAO.remover(id);
    }

    @Override
    public ObservableList<Produto> listarTodosProdutos() throws SQLException {
        return FXCollections.observableArrayList(produtoDAO.listarTodos());
    }

    @Override
    public boolean atualizarEstoque(int idProduto, int quantidadeVendida) throws SQLException {
        Produto produto = produtoDAO.buscarPorId(idProduto);
        if (produto != null && produto.getQuantidade() >= quantidadeVendida) {
            produto.setQuantidade(produto.getQuantidade() - quantidadeVendida);
            produtoDAO.atualizar(produto);
            return true;
        } return false;
    }
    @Override
    public ObservableList<Produto> buscarProdutosPorNome(String nome) throws SQLException {
        return FXCollections.observableArrayList(produtoDAO.buscarPorNome(nome));
    }

    @Override
    public boolean validarProduto(Produto produto) {
        return produto != null &&
                produto.getNome() != null && !produto.getNome().isBlank() &&
                produto.getQuantidade() >= 0 &&
                produto.getPreco() != null && produto.getPreco().compareTo(BigDecimal.ZERO) > 0 &&
                produto.getCategoria() != null &&
                produto.getSubCategoria() != null;
    }

    @Override
    public void gerarRelatorioProdutos(TableView<Produto> tabela) {
        ReportPrinter.imprimirTabela(tabela);
    }
}