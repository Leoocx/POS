package com.system.pos.pos.model;
import java.time.LocalDateTime;

public class Venda {
    private int codigo;
    private int quantidade;
    private int precoUnitario;
    private LocalDateTime data;
    private Cliente cliente;
    private Pagamento formaPagamento;
    private double desconto;
    private StatusVenda status;

    public Venda(int codigo, int quantidade, double valorTotal, StatusVenda status, double desconto, Pagamento formaPagamento, LocalDateTime data, Cliente cliente, int precoUnitario) {
        this.codigo = codigo;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
        this.status = status;
        this.desconto = desconto;
        this.formaPagamento = formaPagamento;
        this.data = data;
        this.cliente = cliente;
        this.precoUnitario = precoUnitario;
    }

    private double valorTotal;

    public StatusVenda getStatus() {
        return status;
    }

    public void setStatus(StatusVenda status) {
        this.status = status;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(int precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Pagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(Pagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

}



