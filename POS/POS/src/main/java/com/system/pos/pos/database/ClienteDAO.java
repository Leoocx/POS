package com.system.pos.pos.database;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.TipoCliente;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private Connection conexao;

    public ClienteDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void adicionarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (nome, cpf_cnpj, telefone, email, endereco, tipo, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpfCNPJ());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEndereco().toString());
            stmt.setString(6, cliente.getTipo().toString());
            stmt.setString(7, cliente.getEmail());
            stmt.executeUpdate();
        }
    }

    public void removerCliente(Cliente cliente) throws SQLException{

    }

    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(
                        TipoCliente.valueOf(rs.getString("tipo")), // Supondo que "tipo" seja armazenado como String no banco
                        rs.getString("nome"),
                        rs.getInt("codigo"),
                        rs.getDate("dataCadastro").toLocalDate(),
                        rs.getString("telefone"),
                        rs.getString("celular"), // Adicionado o celular que estava faltando
                        rs.getString("cpfCNPJ"),
                        rs.getString("rg"), // Adicionado o RG que estava faltando
                        rs.getString("emissor"), // Adicionado o órgão emissor do RG
                        rs.getDate("dataEmissao") != null ? rs.getDate("dataEmissao").toLocalDate() : null, // Para evitar NullPointerException
                        rs.getString("email"),
                        new Endereco( // Criando um objeto Endereco
                                rs.getString("cep"),
                                rs.getString("logradouro"),
                                rs.getInt("numero"),
                                rs.getString("complemento"),
                                rs.getString("bairro"),
                                rs.getString("cidade"),
                                rs.getString("UF")
                        ),
                        rs.getString("naturalidade"), // Adicionado naturalidade
                        rs.getDate("dataNascimento") != null ? rs.getDate("dataNascimento").toLocalDate() : null, // Para evitar erro caso seja null
                        rs.getBoolean("bloqueado"),
                        rs.getString("estadoCivil")
                );

                clientes.add(c);
            }
        }
        return clientes;
    }
    public Cliente buscarClientePorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE codigo = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            TipoCliente.valueOf(rs.getString("tipo")),
                            rs.getString("nome"),
                            rs.getInt("codigo"),
                            rs.getDate("dataCadastro").toLocalDate(),
                            rs.getString("telefone"),
                            rs.getString("celular"),
                            rs.getString("cpfCNPJ"),
                            rs.getString("rg"),
                            rs.getString("emissor"),
                            rs.getDate("dataEmissao").toLocalDate(),
                            rs.getString("email"),
                            new Endereco(
                                    rs.getString("cep"),
                                    rs.getString("logradouro"),
                                    rs.getInt("numero"),
                                    rs.getString("complemento"),
                                    rs.getString("bairro"),
                                    rs.getString("cidade"),
                                    rs.getString("UF")
                            ),
                            rs.getString("naturalidade"),
                            rs.getDate("dataNascimento").toLocalDate(),
                            rs.getBoolean("bloqueado"),
                            rs.getString("estadoCivil")
                    );
                }
            }
        }
        return null; // Retorna null caso não encontre o cliente
    }
}