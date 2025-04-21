package com.system.pos.pos.database;

import com.system.pos.pos.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection conexao;

    public ProdutoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void adicionarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produto (codigo, descricao, preco_compra, preco_venda, barcode, estoque_atual) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, produto.getCodigo());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoCompra());
            stmt.setDouble(4, produto.getPrecoVenda());
            stmt.setString(5, produto.getCodigoBarras());
            stmt.setInt(6, produto.getEstoqueAtual());
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
                        rs.getInt("codigo"),
                        rs.getInt("estoque_atual"),
                        "", // localizacao
                        0.0, // comissao
                        null, // validade
                        "", // marca
                        rs.getDouble("preco_compra"),
                        rs.getString("descricao"),
                        null, // fornecedor
                        rs.getDouble("preco_venda"),
                        "", // unidade
                        rs.getString("barcode"),
                        0.0, // lucro
                        null, // subCategoria
                        null, // categoria
                        0, // garantia
                        ""  // referencia
                );
                produtos.add(p);
            }
        }
        return produtos;
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE Produto SET descricao = ?, preco_compra = ?, preco_venda = ?, barcode = ?, estoque_atual = ? WHERE codigo = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, produto.getDescricao());
            stmt.setDouble(2, produto.getPrecoCompra());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setString(4, produto.getCodigoBarras());
            stmt.setInt(5, produto.getEstoqueAtual());
            stmt.setInt(6, produto.getCodigo());
            stmt.executeUpdate();
        }
    }

    public void removerProduto(int codigo) throws SQLException {
        String sql = "DELETE FROM Produto WHERE codigo = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        }
    }
}
