package com.system.pos.pos.service;

import com.system.pos.pos.database.VendaDAO;
import com.system.pos.pos.model.ItemVenda;
import com.system.pos.pos.model.Produto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class VendaService {
    private final VendaDAO vendaDAO = new VendaDAO();
    private final ProdutoService produtoService = new ProdutoService();

    public boolean registrarVenda(List<ItemVenda> itens, String formaPagamento) {
        try {
            // 1. Registrar a venda
            int idVenda = vendaDAO.registrarVenda(itens, formaPagamento);

            if (idVenda == -1) return false;

            // 2. Atualizar estoque para cada item
            for (ItemVenda item : itens) {
                produtoService.atualizarEstoque(item.getIdProduto(), item.getQuantidade());
            }

            // 3. Registrar movimentação no caixa
            BigDecimal total = calcularTotalVenda(itens);
            vendaDAO.registrarMovimentacaoCaixa(total, formaPagamento);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private BigDecimal calcularTotalVenda(List<ItemVenda> itens) {
        return itens.stream()
                .map(item -> BigDecimal.valueOf(item.getPrecoUnitario()))
                .reduce(BigDecimal.ZERO, (total, preco) -> total.add(preco));
    }
}