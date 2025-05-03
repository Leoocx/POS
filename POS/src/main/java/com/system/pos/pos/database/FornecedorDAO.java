package com.system.pos.pos.database;

import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.repository.FornecedorRepository;

import java.sql.*;

public class FornecedorDAO implements FornecedorRepository {

    private Connection connection;

    public FornecedorDAO() {
        this.connection = ConnectionDB.conectar();
    }
    @Override
    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO fornecedores (nome, cpf_cnpj, telefone, email, endereco) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fornecedor.getNome());
            preparedStatement.setString(2, fornecedor.getCnpj());
            preparedStatement.setString(3, fornecedor.getTelefone());
            preparedStatement.setString(4, fornecedor.getEmail());
            preparedStatement.setString(5, fornecedor.getEndereco().toString()); // ajustar para salvar como JSON ou campos separados
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "UPDATE fornecedores SET nome = ?, cpf_cnpj = ?, telefone = ?, email = ?, endereco = ?,  WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fornecedor.getNome());
            preparedStatement.setString(2, fornecedor.getCnpj());
            preparedStatement.setString(3, fornecedor.getTelefone());
            preparedStatement.setString(4, fornecedor.getEmail());
            preparedStatement.setString(5, fornecedor.getEndereco().toString());
            preparedStatement.setInt(6, fornecedor.getCodigo());
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void removerFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "DELETE FROM fornecedores WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, fornecedor.getCodigo());
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public Fornecedor exibirFornecedorPorId(int id) throws SQLException {
        String sql = "SELECT * FROM fornecedores WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setCodigo(rs.getInt("id"));
                    fornecedor.setNome(rs.getString("nome"));
                    fornecedor.setCnpj(rs.getString("cpf_cnpj"));
                    fornecedor.setTelefone(rs.getString("telefone"));
                    fornecedor.setEmail(rs.getString("email"));


                    String enderecoStr = rs.getString("endereco");
                    if (enderecoStr != null && !enderecoStr.isBlank()) {
                        String[] partes = enderecoStr.split(";");
                        if (partes.length == 7) {
                            String cep = partes[0];
                            String logradouro = partes[1];
                            int numero = Integer.parseInt(partes[2]);
                            String complemento = partes[3];
                            String bairro = partes[4];
                            String cidade = partes[5];
                            String uf = partes[6];

                            Endereco endereco = new Endereco(cep, logradouro, numero, complemento, bairro, cidade, uf);
                            fornecedor.setEndereco(endereco);
                        }
                    }
                    return fornecedor;
                }
            }
        }
        return null;
    }

}
