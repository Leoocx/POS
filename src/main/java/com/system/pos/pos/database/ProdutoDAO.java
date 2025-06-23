package com.system.pos.pos.database;

import com.system.pos.pos.model.*;
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
        String sql = "INSERT INTO produtos (nome_produto, quantidade, preco, status, codigo_barras, categoria_id, subcategoria_id, fornecedor_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setBigDecimal(3, produto.getPreco());
            stmt.setString(4, produto.getStatus());
            stmt.setString(5, produto.getCodigoBarras());
            stmt.setInt(6, produto.getCategoria().getId());
            stmt.setInt(7, produto.getSubCategoria().getId());
            stmt.setInt(8, produto.getFornecedor().getId());
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
                "codigo_barras = ?, categoria_id = ?, subcategoria_id = ?, fornecedor_id = ? WHERE id_produto = ?";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setBigDecimal(3, produto.getPreco());
            stmt.setString(4, produto.getStatus());
            stmt.setString(5, produto.getCodigoBarras());
            stmt.setInt(6, produto.getCategoria().getId());
            stmt.setInt(7, produto.getSubCategoria().getId());
            stmt.setInt(8, produto.getFornecedor().getId());
            stmt.setInt(9, produto.getId());
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

        // Mapear categoria, subcategoria e fornecedor
        int categoriaId = rs.getInt("categoria_id");
        int subCategoriaId = rs.getInt("subcategoria_id");
        int fornecedorId = rs.getInt("fornecedor_id");

        // Verificar se os IDs são válidos antes de mapear
        if (categoriaId > 0) {
            produto.setCategoria(Categoria.fromId(categoriaId));
        }
        if (subCategoriaId > 0) {
            produto.setSubCategoria(SubCategoria.fromId(subCategoriaId));
        }
        if (fornecedorId > 0) {
            FornecedorDAO fornecedorDAO = new FornecedorDAO(); // Instanciar o DAO
            produto.setFornecedor(fromId(fornecedorId)); // Mapear fornecedor
        }

        return produto;
    }

    public Fornecedor fromId(int id) throws SQLException {
        String sql = "SELECT * FROM fornecedores WHERE id = ?";
        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarFornecedorFromResultSet(rs);
                }
            }
        }
        return null;
    }

    private Fornecedor criarFornecedorFromResultSet(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setCep(rs.getString("cep"));
        endereco.setLogradouro(rs.getString("logradouro"));
        endereco.setNumero(rs.getString("numero"));
        endereco.setComplemento(rs.getString("complemento"));
        endereco.setBairro(rs.getString("bairro"));
        endereco.setLocalidade(rs.getString("localidade"));
        endereco.setUf(rs.getString("uf"));

        Fornecedor fornecedor = new Fornecedor(
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("cnpj"),
                rs.getString("email"),
                endereco
        );
        fornecedor.setId(rs.getInt("id"));
        fornecedor.setRepresentante(rs.getString("representante"));
        fornecedor.setTelefoneRepresentante(rs.getString("telefone_representante"));
        fornecedor.setEmailRepresentante(rs.getString("email_representante"));
        fornecedor.setDataCadastro(rs.getDate("data_cadastro").toLocalDate());

        return fornecedor;
    }
}
