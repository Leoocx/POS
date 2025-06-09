package com.system.pos.pos.database;

import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.repository.FornecedorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO implements FornecedorRepository {

    private Connection connection;

    public FornecedorDAO() {
        this.connection = ConnectionManager.getConnection();
    }


    @Override
    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException {
        int enderecoId = salvarEndereco(fornecedor.getEndereco());

        String sql = "INSERT INTO fornecedores (nome, cnpj, telefone, email, endereco_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setInt(5, enderecoId);
            stmt.executeUpdate();
        }
    }
    @Override
    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "UPDATE fornecedores SET nome = ?, cnpj = ?, telefone = ?, email = ?,  WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fornecedor.getNome());
            preparedStatement.setString(2, fornecedor.getCnpj());
            preparedStatement.setString(3, fornecedor.getTelefone());
            preparedStatement.setString(4, fornecedor.getEmail());
            preparedStatement.setInt(5, fornecedor.getId());
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void removerFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "DELETE FROM fornecedores WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, fornecedor.getId());
            preparedStatement.executeUpdate();
        }
    }
    

    @Override
    public List<Fornecedor> showAll() throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM fornecedores";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setId(rs.getInt("id"));
                    fornecedor.setNome(rs.getString("nome"));
                    fornecedor.setCnpj(rs.getString("cnpj"));
                    fornecedor.setTelefone(rs.getString("telefone"));
                    fornecedor.setEmail(rs.getString("email"));
                    fornecedores.add(fornecedor);
            }
        }
        return fornecedores;
    }

    @Override
    public int salvarEndereco(Endereco endereco) throws SQLException {
        String sql = "INSERT INTO enderecos (cep, logradouro, numero, complemento, bairro, cidade, uf) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getLogradouro());
            stmt.setString(3, endereco.getNumero());
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidade());
            stmt.setString(7, endereco.getUF());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1); // id gerado
                } else {
                    throw new SQLException("ID do endereço não gerado.");
                }
            }
        }
    }

}
