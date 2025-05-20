package com.system.pos.pos.database;

import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.repository.FornecedorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO implements FornecedorRepository {

    private Connection connection;

    public FornecedorDAO() {
        this.connection = ConnectionDB.conectar();
    }
    @Override
    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO fornecedores (nome, cnpj, telefone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fornecedor.getNome());
            preparedStatement.setString(2, fornecedor.getCnpj());
            preparedStatement.setString(3, fornecedor.getTelefone());
            preparedStatement.setString(4, fornecedor.getEmail());
            preparedStatement.executeUpdate();
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

}
