package com.system.pos.pos.model;

import java.math.BigDecimal;


public class Pagamento {
    private String formaPagamento;
    private BigDecimal valor;

    public Pagamento(String formaPagamento, BigDecimal valor) {
        this.formaPagamento = formaPagamento;
        this.valor = valor;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
