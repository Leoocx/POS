package com.system.pos.pos.database;

import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.repository.FornecedorRepository;
import com.system.pos.pos.exceptions.BusinessException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO implements FornecedorRepository {
    private final Connection connection;

    public FornecedorDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    @Override
    public int salvarEndereco(Endereco endereco) throws SQLException {
        return 0;
    }

    @Override
    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO fornecedores (nome, cnpj, telefone, email, " +
                "representante, telefone_representante, email_representante, " +
                "cep, logradouro, numero, complemento, bairro, localidade, uf, data_cadastro) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preencherStatement(fornecedor, stmt);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    fornecedor.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "UPDATE fornecedores SET nome = ?, telefone = ?, email = ?, " +
                "representante = ?, telefone_representante = ?, email_representante = ?, " +
                "cep = ?, logradouro = ?, numero = ?, complemento = ?, " +
                "bairro = ?, localidade = ?, uf = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            preencherStatement(fornecedor, stmt);
            stmt.setInt(14, fornecedor.getId());
            stmt.executeUpdate();
        }
    }

    private void preencherStatement(Fornecedor fornecedor, PreparedStatement stmt) throws SQLException {
        Endereco endereco = fornecedor.getEndereco();

        stmt.setString(1, fornecedor.getNome());
        stmt.setString(2, fornecedor.getDocumento());
        stmt.setString(3, fornecedor.getTelefone());
        stmt.setString(4, fornecedor.getEmail());
        stmt.setString(5, fornecedor.getRepresentante());
        stmt.setString(6, fornecedor.getTelefoneRepresentante());
        stmt.setString(7, fornecedor.getEmailRepresentante());

        stmt.setString(8, endereco.getCep());
        stmt.setString(9, endereco.getLogradouro());
        stmt.setString(10, endereco.getNumero());
        stmt.setString(11, endereco.getComplemento());
        stmt.setString(12, endereco.getBairro());
        stmt.setString(13, endereco.getLocalidade());
        stmt.setString(14, endereco.getUf());

        stmt.setDate(15, Date.valueOf(fornecedor.getDataCadastro()));
    }

    @Override
    public void removerFornecedor(Fornecedor fornecedor) throws SQLException, BusinessException {

        if (existemProdutosVinculados(fornecedor.getId())) {
            throw new BusinessException("Não é possível excluir o fornecedor pois existem produtos vinculados a ele.");
        }

        String sql = "DELETE FROM fornecedores WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, fornecedor.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Verifica se existem produtos vinculados a este fornecedor
     */
    public boolean existemProdutosVinculados(int fornecedorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM produtos WHERE fornecedor_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, fornecedorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public List<Fornecedor> showAll() throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM fornecedores";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fornecedores.add(criarFornecedorFromResultSet(rs));
            }
        }
        return fornecedores;
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