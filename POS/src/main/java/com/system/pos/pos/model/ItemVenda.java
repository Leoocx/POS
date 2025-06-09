package com.system.pos.pos.model;

import javafx.beans.property.*;
import java.math.BigDecimal;

public class ItemVenda {
    private final Produto produto;
    private final IntegerProperty quantidade = new SimpleIntegerProperty();
    private final ReadOnlyObjectWrapper<Double> totalItem = new ReadOnlyObjectWrapper<>();

    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        setQuantidade(quantidade);
        atualizarTotal();

        this.quantidade.addListener((obs, oldVal, newVal) -> atualizarTotal());
    }

    private void atualizarTotal() {
        Double total = produto.getPreco()*getQuantidade();
        totalItem.set(total);
    }

    public Produto getProduto() {
        return produto;
    }

    public int getIdProduto() {
        return produto.getId();
    }

    public String getNomeProduto() {
        return produto.getNome();
    }

    public double getPrecoUnitario() {
        return produto.getPreco();
    }

    public IntegerProperty quantidadeProperty() {
        return quantidade;
    }

    public int getQuantidade() {
        return quantidade.get();
    }

    public void setQuantidade(int quantidade) {
        this.quantidade.set(quantidade);
    }

    public ReadOnlyObjectProperty<Double> totalItemProperty() {
        return totalItem.getReadOnlyProperty();
    }

    public double getTotalItem() {
        return totalItem.get();
    }

    public double getSubTotal() {
        return getTotalItem();
    }

    public String getDummy() {
        return "";
    }
}
