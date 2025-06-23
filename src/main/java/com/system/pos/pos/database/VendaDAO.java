package com.system.pos.pos.database;

import com.system.pos.pos.model.ItemVenda;
import com.system.pos.pos.repository.VendaRepository;

import java.sql.*;
import java.util.List;

public class VendaDAO implements VendaRepository {
    private final Connection CONEXAO_DB;

    public VendaDAO() {
        this.CONEXAO_DB = ConnectionManager.getConnection();
    }

    @Override
    public int registrarVenda(List<ItemVenda> itens, String formaPagamento) throws SQLException {
        String sqlVenda = "INSERT INTO vendas (data, formaPagamento, status) VALUES (date('now'), ?, 'CONCLUIDA')";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, formaPagamento);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idVenda = rs.getInt(1);
                    registrarItensVenda(idVenda, itens); // Passa idVenda aqui
                    return idVenda;
                }
            }
        }
        return -1; // Retorna -1 se não conseguir registrar a venda
    }

    @Override
    public void registrarItensVenda(int idVenda, List<ItemVenda> itens) throws SQLException {
        String sqlItem = "INSERT INTO itens_venda (id_venda, id_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = CONEXAO_DB.prepareStatement(sqlItem)) {
            for (ItemVenda item : itens) {
                stmt.setInt(1, idVenda); // Usa idVenda corretamente
                stmt.setInt(2, item.getIdProduto());
                stmt.setInt(3, item.getQuantidade()); // Certifique-se de que quantidade não é nula
                stmt.setBigDecimal(4, item.getPrecoUnitario()); // Certifique-se de que preco_unitario não é nulo
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
