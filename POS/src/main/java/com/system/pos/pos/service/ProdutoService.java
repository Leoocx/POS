package com.system.pos.pos.service;
import com.system.pos.pos.database.ProdutoDAO;
import com.system.pos.pos.model.Produto;
import java.sql.SQLException;
import java.util.List;

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
}