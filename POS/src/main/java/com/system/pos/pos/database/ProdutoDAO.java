package com.system.pos.pos.database;

import com.system.pos.pos.model.Produto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection conexao;

    public ProdutoDAO() {
        this.conexao = ConnectionDB.conectar();
    }
    
    public void adicionarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (codigo, nome, preco, quantidade, categoria, fornecedor, marca, referencia, localizacao, validade, unidade, subCategoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, produto.getCdProduto());
            stmt.setString(2, produto.getNome());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getCategoria().toString());
            stmt.setString(5, produto.getFornecedor());
            stmt.setString(6, produto.getMarca());
            stmt.setString(7, produto.getReferencia());
            stmt.setString(8, produto.getLocalizacao());
            stmt.setString(9,produto.getValidade().toString());
            stmt.setInt(10, produto.getUnidade());
            stmt.setInt(11, produto.getUnidade());
            stmt.setString(12, produto.getSubCategoria().toString());
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto";

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto p = new Produto(

                );
                produtos.add(p);
            }
        }
        return produtos;
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET preco = ?, localizacao = ?, validade = ? WHERE codigo = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setFloat(1, produto.getPreco());
            stmt.setString(2, produto.getLocalizacao());
            stmt.setString(3, produto.getValidade().toString());
            stmt.setInt(4, produto.getCdProduto());
            stmt.executeUpdate();
        }
    }

    public void removerProduto(int codigo) throws SQLException {
        String sql = "DELETE FROM produtos WHERE codigo = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        }
    }
}