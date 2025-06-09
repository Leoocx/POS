package com.system.pos.pos.database;

import com.system.pos.pos.model.Produto;
import com.system.pos.pos.repository.ProdutoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements ProdutoRepository {
    private final Connection CONEXAO_DB;

    public ProdutoDAO() {
        this.CONEXAO_DB = ConnectionManager.getConnection();
    }

    @Override
    public void insertProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome_produto, quantidade, preco, status) VALUES (?,?,?,?)";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getStatus());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome_produto = ?, quantidade = ?, preco = ?, status = ? WHERE id_produto = ?";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getStatus());
            stmt.setInt(5, produto.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteProduto(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Produto searchByID(int id) throws SQLException {
        String sql = "SELECT * FROM produtos WHERE id_produto = ?";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id_produto"));
                    produto.setNome(rs.getString("nome_produto"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setStatus(rs.getString("status"));
                    return produto;
                }
            }
        }
        return null;
    }

    @Override
    public List<Produto> showAll() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome_produto"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setStatus(rs.getString("status"));
                produtos.add(produto);
            }
        }
        return produtos;
    }
}