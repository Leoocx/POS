package com.system.pos.pos.database;

import com.system.pos.pos.model.Conta;
import com.system.pos.pos.repository.ContaRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO implements ContaRepository {
    private final Connection CONEXAO_DB;

    public ContaDAO() {
        this.CONEXAO_DB = ConnectionManager.getConnection();
    }

    @Override
    public void insertConta(Conta conta) throws SQLException {
        String sql = "INSERT INTO contas (descricao, valor, vencimento, pago, tipo, data_pagamento) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, conta.getDescricao());
            stmt.setDouble(2, conta.getValor());
            stmt.setDate(3, Date.valueOf(conta.getVencimento()));
            stmt.setBoolean(4, conta.isPago());
            stmt.setString(5, conta.isPagar() ? "PAGAR" : "RECEBER");
            stmt.setDate(6, conta.isPago() ? Date.valueOf(conta.getDataPagamento()) : null); // Novo campo
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    conta.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateConta(Conta conta) throws SQLException {
        String sql = "UPDATE contas SET descricao = ?, valor = ?, vencimento = ?, pago = ?, tipo = ?, data_pagamento = ? WHERE id = ?";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setString(1, conta.getDescricao());
            stmt.setDouble(2, conta.getValor());
            stmt.setDate(3, Date.valueOf(conta.getVencimento()));
            stmt.setBoolean(4, conta.isPago());
            stmt.setString(5, conta.isPagar() ? "PAGAR" : "RECEBER");
            stmt.setDate(6, conta.isPago() ? Date.valueOf(conta.getDataPagamento()) : null); // Novo campo
            stmt.setInt(7, conta.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteConta(int id) throws SQLException {
        String sql = "DELETE FROM contas WHERE id = ?";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Conta searchByID(int id) throws SQLException {
        String sql = "SELECT * FROM contas WHERE id = ?";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarContaFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Conta> showAll(boolean isPagar) throws SQLException {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT * FROM contas WHERE tipo = ? ORDER BY vencimento";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setString(1, isPagar ? "PAGAR" : "RECEBER");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contas.add(criarContaFromResultSet(rs));
            }
        }
        return contas;
    }

    @Override
    public void registrarPagamento(int id) throws SQLException {
        String sql = "UPDATE contas SET pago = 1, data_pagamento = ? WHERE id = ?";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Nenhuma conta encontrada com o ID: " + id);
            }
        }
    }

    private Conta criarContaFromResultSet(ResultSet rs) throws SQLException {
        Conta conta = new Conta();
        conta.setId(rs.getInt("id"));
        conta.setDescricao(rs.getString("descricao"));
        conta.setValor(rs.getDouble("valor"));
        conta.setVencimento(rs.getDate("vencimento").toLocalDate());
        conta.setPago(rs.getInt("pago") == 1);
        conta.setPagar("PAGAR".equals(rs.getString("tipo")));

        Date dataPagamento = rs.getDate("data_pagamento");
        if (dataPagamento != null) {
            conta.setDataPagamento(dataPagamento.toLocalDate());
        }

        return conta;
    }
}
