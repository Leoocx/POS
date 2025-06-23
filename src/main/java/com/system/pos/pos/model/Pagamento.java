package com.system.pos.pos.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Pagamento {
    private BigDecimal valorTotal;
    private BigDecimal valorPago;
    private BigDecimal desconto;
    private String formaPagamento;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public Pagamento() {
        this.valorTotal = BigDecimal.ZERO;
        this.valorPago = BigDecimal.ZERO;
        this.desconto = BigDecimal.ZERO;
        this.formaPagamento = "DINHEIRO";
    }

    // Getters e Setters
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    // Métodos de cálculo
    public BigDecimal calcularTotalLiquido() {
        return valorTotal.subtract(desconto).max(BigDecimal.ZERO);
    }

    public BigDecimal calcularTroco() {
        BigDecimal totalLiquido = calcularTotalLiquido();
        return valorPago.compareTo(totalLiquido) >= 0 ? valorPago.subtract(totalLiquido) : BigDecimal.ZERO;
    }

    public boolean isPagamentoValido() {
        return valorPago.compareTo(calcularTotalLiquido()) >= 0;
    }

    public String getTrocoFormatado() {
        return currencyFormat.format(calcularTroco());
    }

    public String getTotalLiquidoFormatado() {
        return currencyFormat.format(calcularTotalLiquido());
    }
}