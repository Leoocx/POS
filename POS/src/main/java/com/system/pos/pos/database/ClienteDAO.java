package com.system.pos.pos.database;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.TipoCliente;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private Connection conexao;

    public ClienteDAO() {
        this.conexao=ConnectionDB.conectar();
    }

    public void adicionarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (codigo ,nome, cpfCNPJ, telefone, email, endereco, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
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

    public void atualizarCliente(Cliente cliente) throws SQLException{

    }

    public void removerCliente(Cliente cliente) throws SQLException{

    }

    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(
                       rs.getString("nome"),
                       rs.getString("codigo"),
                       rs.getString("cpfCNPJ"),
                       rs.getString("email"),
                       rs.getString("endereco")
                );

                clientes.add(c);
            }
        }
        return clientes;
    }
    public Cliente buscarClientePorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE codigo = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                           rs.getString("nome"),
                           rs.getString("codigo"),
                       rs.getString("cpfCNPJ"),
                       rs.getString("email"),
                       rs.getString("endereco")
                    );
                }
            }
        }
        return null; // Retorna null caso não encontre o cliente
    }
}