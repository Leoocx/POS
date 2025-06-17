package com.system.pos.pos.database;

import com.system.pos.pos.model.Produto;
import com.system.pos.pos.model.Categoria;
import com.system.pos.pos.model.SubCategoria;
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
        String sql = "INSERT INTO produtos (nome_produto, quantidade, preco, status, codigo_barras, categoria_id, subcategoria_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setString(3, produto.getPreco().toString());
            stmt.setString(4, produto.getStatus());
            stmt.setString(5, produto.getCodigoBarras());
            stmt.setInt(6, produto.getCategoria().getId());
            stmt.setInt(7, produto.getSubCategoria().getId());
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
        String sql = "UPDATE produtos SET nome_produto = ?, quantidade = ?, preco = ?, status = ?, " +
                "codigo_barras = ?, categoria_id = ?, subcategoria_id = ? WHERE id_produto = ?";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setString(3, produto.getPreco().toString());
            stmt.setString(4, produto.getStatus());
            stmt.setString(5, produto.getCodigoBarras());
            stmt.setInt(6, produto.getCategoria().getId());
            stmt.setInt(7, produto.getSubCategoria().getId());
            stmt.setInt(8, produto.getId());
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
                    return mapearProduto(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Produto> showAll() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }
        return produtos;
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id_produto"));
        produto.setNome(rs.getString("nome_produto"));
        produto.setQuantidade(rs.getInt("quantidade"));
        produto.setPreco(rs.getBigDecimal("preco"));
        produto.setStatus(rs.getString("status"));
        produto.setCodigoBarras(rs.getString("codigo_barras"));

        // Mapear categoria e subcategoria
        int categoriaId = rs.getInt("categoria_id");
        int subCategoriaId = rs.getInt("subcategoria_id");

        produto.setCategoria(Categoria.fromId(categoriaId));
        produto.setSubCategoria(SubCategoria.fromId(subCategoriaId));

        return produto;
    }
}