package com.system.pos.pos.database;

import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private final Connection connection;

    public ClienteDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public void adicionarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, telefone, cpf, email, cep, logradouro, bairro, localidade, uf) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherStatement(cliente, stmt);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, telefone = ?, email = ?, cep = ?, logradouro = ?, bairro = ?, localidade = ?, uf = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            preencherStatement(cliente, stmt);
            stmt.setInt(9, cliente.getId());
            stmt.executeUpdate();
        }
    }

    private void preencherStatement(Cliente cliente, PreparedStatement stmt) throws SQLException {
        Endereco endereco = cliente.getEndereco();

        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getTelefone());
        stmt.setString(3, cliente.getDocumento());
        stmt.setString(4, cliente.getEmail());
        stmt.setString(5, endereco.getCep());
        stmt.setString(6, endereco.getLogradouro());
        stmt.setString(7, endereco.getBairro());
        stmt.setString(8, endereco.getLocalidade());
        stmt.setString(9, endereco.getUf());
    }

    public void removerCliente(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(criarClienteFromResultSet(rs));
            }
        }
        return clientes;
    }

    private Cliente criarClienteFromResultSet(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setCep(rs.getString("cep"));
        endereco.setLogradouro(rs.getString("logradouro"));
        endereco.setBairro(rs.getString("bairro"));
        endereco.setLocalidade(rs.getString("localidade"));
        endereco.setUf(rs.getString("uf"));

        Cliente cliente = new Cliente(
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("cpf"),
                rs.getString("email"),
                endereco
        );
        cliente.setId(rs.getInt("id"));
        return cliente;
    }
}
