package com.system.pos.pos.service;

import com.system.pos.pos.database.ConnectionManager;
import com.system.pos.pos.database.VendaDAO;
import com.system.pos.pos.model.ItemVenda;
import com.system.pos.pos.model.Venda;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class VendaService {
    private final VendaDAO vendaDAO = new VendaDAO();
    private final ProdutoService produtoService = new ProdutoService();
    private final Connection CONEXAO_DB;

    public VendaService() {
        this.CONEXAO_DB = ConnectionManager.getConnection();
    }

    public boolean registrarVenda(Venda venda) {
        try {
            CONEXAO_DB.setAutoCommit(false); // Desativa auto-commit

            // 1. Registrar a venda
            int idVenda = vendaDAO.registrarVenda(venda.getItensVenda(), venda.getPagamento().getFormaPagamento());
            if (idVenda == -1) {
                CONEXAO_DB.rollback();
                return false;
            }

            // 2. Atualizar estoque para cada item
            for (ItemVenda item : venda.getItensVenda()) {
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
