package com.system.pos.pos.database;

import com.system.pos.pos.model.Funcionario;
import com.system.pos.pos.model.Endereco;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    private final Connection connection;

    public FuncionarioDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public void adicionarFuncionario(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionarios (documento, nome, telefone, email, cargo, salario, " +
                "data_admissao, data_demissao, turno, status, cep, logradouro, numero, " +
                "complemento, bairro, localidade, uf) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setFuncionarioParameters(stmt, funcionario);
            stmt.executeUpdate();
        }
    }

    public void atualizarFuncionario(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE funcionarios SET documento = ?, nome = ?, telefone = ?, email = ?, " +
                "cargo = ?, salario = ?, data_admissao = ?, data_demissao = ?, turno = ?, " +
                "status = ?, cep = ?, logradouro = ?, numero = ?, complemento = ?, " +
                "bairro = ?, localidade = ?, uf = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setFuncionarioParameters(stmt, funcionario);
            stmt.setInt(18, funcionario.getId());
            stmt.executeUpdate();
        }
    }

    public void removerFuncionario(Funcionario funcionario) throws SQLException {
        String sql = "DELETE FROM funcionarios WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, funcionario.getId());
            stmt.executeUpdate();
        }
    }

    public List<Funcionario> showAll() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                funcionarios.add(createFuncionarioFromResultSet(rs));
            }
        }
        return funcionarios;
    }

    private void setFuncionarioParameters(PreparedStatement stmt, Funcionario funcionario)
            throws SQLException {
        Endereco endereco = funcionario.getEndereco();

        stmt.setString(1, funcionario.getDocumento());
        stmt.setString(2, funcionario.getNome());
        stmt.setString(3, funcionario.getTelefone());
        stmt.setString(4, funcionario.getEmail());
        stmt.setString(5, funcionario.getCargo());
        stmt.setBigDecimal(6, funcionario.getSalario());
        stmt.setString(7, funcionario.getDataAdmissao().toString());
        stmt.setString(8, funcionario.getDataDemissao() != null ?
                funcionario.getDataDemissao().toString() : null);
        stmt.setString(9, funcionario.getTurno());
        stmt.setString(10, funcionario.getStatus());

        // Endereço
        stmt.setString(11, endereco != null ? endereco.getCep() : null);
        stmt.setString(12, endereco != null ? endereco.getLogradouro() : null);
        stmt.setString(13, endereco != null ? endereco.getNumero() : null);
        stmt.setString(14, endereco != null ? endereco.getComplemento() : null);
        stmt.setString(15, endereco != null ? endereco.getBairro() : null);
        stmt.setString(16, endereco != null ? endereco.getLocalidade() : null);
        stmt.setString(17, endereco != null ? endereco.getUf() : null);
    }

    private Funcionario createFuncionarioFromResultSet(ResultSet rs) throws SQLException {
        Funcionario funcionario = new Funcionario(
                null, // TipoParticipante
                rs.getString("documento"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("email"),
                createEnderecoFromResultSet(rs),
                rs.getString("cargo"),
                rs.getBigDecimal("salario"),
                LocalDate.parse(rs.getString("data_admissao")),
                rs.getString("data_demissao") != null ?
                        LocalDate.parse(rs.getString("data_demissao")) : null,
                rs.getString("turno"),
                rs.getString("status")
        );
        funcionario.setId(rs.getInt("id"));
        return funcionario;
    }

    private Endereco createEnderecoFromResultSet(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setCep(rs.getString("cep"));
        endereco.setLogradouro(rs.getString("logradouro"));
        endereco.setNumero(rs.getString("numero"));
        endereco.setComplemento(rs.getString("complemento"));
        endereco.setBairro(rs.getString("bairro"));
        endereco.setLocalidade(rs.getString("localidade"));
        endereco.setUf(rs.getString("uf"));
        return endereco;
    }
}