package com.system.pos.pos.model;
import java.time.LocalDateTime;

public class Venda {
    private int codigo;
    private int quantidade;
    private int precoUnitario;
    private LocalDateTime data;
    private Cliente cliente;
    private Pagamento formaPagamento;
    private float desconto;
    private StatusVenda status;
    private float valorTotal;

    public Venda(int codigo, int quantidade, float valorTotal, StatusVenda status, float desconto, Pagamento formaPagamento, LocalDateTime data, Cliente cliente, int precoUnitario) {
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

}



