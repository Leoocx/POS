package com.system.pos.pos.service;

import com.system.pos.pos.database.ConnectionDB;
import com.system.pos.pos.database.VendaDAO;
import com.system.pos.pos.model.ItemVenda;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class VendaService {
    private final VendaDAO vendaDAO = new VendaDAO();
    private final ProdutoService produtoService = new ProdutoService();
    private final Connection CONEXAO_DB = ConnectionDB.conectar();

    public boolean registrarVenda(List<ItemVenda> itens, String formaPagamento) {
        try {
            CONEXAO_DB.setAutoCommit(false); // Desativa auto-commit

            // 1. Registrar a venda
            int idVenda = vendaDAO.registrarVenda(itens, formaPagamento);
            if (idVenda == -1) {
                CONEXAO_DB.rollback();
                return false;
            }

            // 2. Atualizar estoque para cada item
            for (ItemVenda item : itens) {
                boolean atualizado = produtoService.atualizarEstoque(item.getIdProduto(), item.getQuantidade());
                if (!atualizado) {
                    CONEXAO_DB.rollback();
                    return false; // Retorna false se a atualização do estoque falhar
                }
            }

            CONEXAO_DB.commit(); // Confirma todas as operações
            return true;

        } catch (SQLException e) {
            try {
                CONEXAO_DB.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                CONEXAO_DB.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}