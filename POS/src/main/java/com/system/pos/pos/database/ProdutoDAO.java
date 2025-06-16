package com.system.pos.pos.database;

import com.system.pos.pos.model.Produto;
import com.system.pos.pos.model.Categoria;
import com.system.pos.pos.model.SubCategoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements AutoCloseable {
    private static final String INSERT_SQL = "INSERT INTO produtos (nome_produto, quantidade, preco, status, codigo_barras, categoria_id, subcategoria_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE produtos SET nome_produto = ?, quantidade = ?, preco = ?, status = ?, codigo_barras = ?, categoria_id = ?, subcategoria_id = ? WHERE id_produto = ?";
    private static final String DELETE_SQL = "DELETE FROM produtos WHERE id_produto = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM produtos WHERE id_produto = ?";
    private static final String SELECT_ALL = "SELECT * FROM produtos";

    private final Connection connection;

    public ProdutoDAO() throws SQLException {
        this.connection = ConnectionManager.getConnection();
    }

    public int inserir(Produto produto) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(produto, stmt);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    public void atualizar(Produto produto) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            preencherStatement(produto, stmt);
            stmt.setInt(8, produto.getId());
            stmt.executeUpdate();
        }
    }

    public void remover(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Produto buscarPorId(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapearResultSet(rs) : null;
            }
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearResultSet(rs));
            }
        }
        return produtos;
    }

    private void preencherStatement(Produto produto, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, produto.getNome());
        stmt.setInt(2, produto.getQuantidade());
        stmt.setBigDecimal(3, produto.getPreco());
        stmt.setString(4, produto.getStatus());
        stmt.setString(5, produto.getCodigoBarras());
        stmt.setInt(6, produto.getCategoria().getId());
        stmt.setInt(7, produto.getSubCategoria().getId());
    }

    private Produto mapearResultSet(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id_produto"));
        produto.setNome(rs.getString("nome_produto"));
        produto.setQuantidade(rs.getInt("quantidade"));
        produto.setPreco(rs.getBigDecimal("preco"));
        produto.setStatus(rs.getString("status"));
        produto.setCodigoBarras(rs.getString("codigo_barras"));

        int categoriaId = rs.getInt("categoria_id");
        int subCategoriaId = rs.getInt("subcategoria_id");

        produto.setCategoria(Categoria.fromId(categoriaId));
        produto.setSubCategoria(SubCategoria.fromId(subCategoriaId));

        return produto;
    }

    public List<Produto> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM produtos WHERE nome_produto LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            List<Produto> produtos = new ArrayList<>();
            while (rs.next()) {
                produtos.add(mapearResultSet(rs));
            }
            return produtos;
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}