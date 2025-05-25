package com.system.pos.pos.database;

import com.system.pos.pos.model.Caixa;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CaixaDAO {

    private Connection conexao;

    public CaixaDAO() {
        this.conexao=ConnectionDB.conectar();
    }

    public boolean abrirCaixa(Caixa caixa) {
        String sql = "INSERT INTO caixa (data_abertura, vendedor, caixa_inicial, status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(caixa.getDataAbertura()));
            stmt.setString(2, caixa.getVendedor());
            stmt.setDouble(3, caixa.getCaixaInicial());
            stmt.setString(4, caixa.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verificarCaixaAberto() {
        String sql = "SELECT COUNT(*) FROM caixa WHERE status = 'ABERTO' AND data_abertura = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getCaixaInicial() {
        String sql = "SELECT caixa_inicial FROM caixa WHERE status = 'ABERTO' AND data_abertura = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getCaixaAtual() {
        // Implementar lógica para calcular caixa atual (inicial + entradas - saídas)
        return 1500.00; // Exemplo
    }

    public double getTotalSaidas() {
        // Implementar lógica para calcular total de saídas
        return 200.00; // Exemplo
    }

    public double getTotalPorTipo(String tipoPagamento) {
        // Implementar lógica para calcular total por tipo de pagamento
        Map<String, Double> valoresExemplo = new HashMap<>();
        valoresExemplo.put("DINHEIRO", 800.00);
        valoresExemplo.put("CARTAO", 500.00);
        valoresExemplo.put("CHEQUE", 200.00);

        return valoresExemplo.getOrDefault(tipoPagamento, 0.0);
    }

    public double getLucroTotal() {
        // Implementar lógica para calcular lucro total
        return 300.00; // Exemplo
    }
}