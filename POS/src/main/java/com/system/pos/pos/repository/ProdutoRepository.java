package com.system.pos.pos.repository;

import com.system.pos.pos.model.Produto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProdutoRepository {
    void insertProduto(Produto produto) throws SQLException;
    void updateProduto(Produto produto) throws SQLException;
    void deleteProduto(int id) throws SQLException;
    Produto searchByID(int id) throws SQLException;
    List<Produto> showAll() throws SQLException;
}