package com.system.pos.pos.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class Venda {
    private final ObservableList<ItemVenda> itensVenda = FXCollections.observableArrayList();
    private BigDecimal totalVenda;
    private Pagamento pagamento;

    public Venda() {
        this.totalVenda = BigDecimal.ZERO; // Inicializa como BigDecimal.ZERO
        this.pagamento = new Pagamento(); // Inicializa o pagamento
    }

    public void adicionarItem(ItemVenda item) {
        itensVenda.add(item);
        atualizarTotal(); // Atualiza o total sempre que um item é adicionado
    }

    public ObservableList<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO; // Inicializa como BigDecimal.ZERO
        for (ItemVenda item : itensVenda) {
            total = total.add(item.getTotalItem()); // Adiciona o total do item
        }
        return total;
    }

    public BigDecimal getTotalVenda() {
        totalVenda = calcularTotal(); // Atualiza o totalVenda
        return totalVenda;
    }

    private void atualizarTotal() {
        totalVenda = calcularTotal(); // Atualiza o totalVenda
    }

    // Métodos para gerenciar o pagamento
    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
        pagamento.setValorTotal(getTotalVenda()); // Define o valor total do pagamento
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public boolean isPagamentoValido() {
        return pagamento.isPagamentoValido(); // Verifica se o pagamento é válido
    }
}
