package com.system.pos.pos.model;

import java.time.LocalDate;

public class Conta {
    private int id;
    private String descricao;
    private double valor;
    private LocalDate vencimento;
    private boolean pago;
    private boolean pagar; // true para contas a pagar, false para contas a receber
    private LocalDate dataPagamento;
    private Pagamento pagamento;

    // Construtores, getters e setters
    public Conta() {
        this.pagamento = new Pagamento(); // Inicializa o pagamento
    }

    public Conta(String descricao, double valor, LocalDate vencimento, boolean isPagar) {
        this.descricao = descricao;
        this.valor = valor;
        this.vencimento = vencimento;
        this.pagar = isPagar;
        this.pago = false;
        this.pagamento = new Pagamento(); // Inicializa o pagamento
    }

    // Getters e Setters para todos os campos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDate getVencimento() { return vencimento; }
    public void setVencimento(LocalDate vencimento) { this.vencimento = vencimento; }
    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }
    public boolean isPagar() { return pagar; }
    public void setPagar(boolean pagar) { this.pagar = pagar; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public Pagamento getPagamento() { return pagamento; } // Getter para pagamento
    public void setPagamento(Pagamento pagamento) { this.pagamento = pagamento; } // Setter para pagamento
}
