package com.system.pos.pos.database;

import com.system.pos.pos.model.ItemVenda;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class VendaDAO {
    private final Connection CONEXAO_DB;

    public VendaDAO() {
        this.CONEXAO_DB = ConnectionDB.conectar();
    }

    public int registrarVenda(List<ItemVenda> itens, String formaPagamento) throws SQLException {
        String sqlVenda = "INSERT INTO vendas (data, formaPagamento, status) VALUES (date('now'), ?, 'CONCLUIDA')";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, formaPagamento);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idVenda = rs.getInt(1);
                    registrarItensVenda(idVenda, itens);
                    return idVenda;
                }
            }
        }
        return -1;
    }

    private void registrarItensVenda(int idVenda, List<ItemVenda> itens) throws SQLException {
        String sqlItem = "INSERT INTO itens_venda (id_venda, id_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sqlItem)) {
            for (ItemVenda item : itens) {
                stmt.setInt(1, idVenda);
                stmt.setInt(2, item.getIdProduto());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getPrecoUnitario());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public void registrarMovimentacaoCaixa(BigDecimal valor, String formaPagamento) throws SQLException {
        String sql = "INSERT INTO movimentacao_caixa (id_caixa, tipo, valor, forma_pagamento, descricao) " +
                "VALUES ((SELECT id FROM caixa WHERE status = 'ABERTO'), 'ENTRADA', ?, ?, 'Venda de produtos')";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sql)) {
            stmt.setBigDecimal(1, valor);
            stmt.setString(2, formaPagamento);
            stmt.executeUpdate();
        }
    }
}