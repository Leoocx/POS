package com.system.pos.pos.model;

import javafx.beans.property.*;
import java.math.BigDecimal;

public class ItemVenda {
    private final Produto produto;
    private final IntegerProperty quantidade = new SimpleIntegerProperty();
    private final ReadOnlyObjectWrapper<BigDecimal> totalItem = new ReadOnlyObjectWrapper<>(BigDecimal.ZERO);

    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        setQuantidade(quantidade);
        atualizarTotal();

        this.quantidade.addListener((obs, oldVal, newVal) -> atualizarTotal());
    }

    private void atualizarTotal() {
        BigDecimal total = produto.getPreco().multiply(BigDecimal.valueOf(getQuantidade()));
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

    public BigDecimal getPrecoUnitario() {
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

    public ReadOnlyObjectProperty<BigDecimal> totalItemProperty() {
        return totalItem.getReadOnlyProperty();
    }

    public BigDecimal getTotalItem() {
        return totalItem.get();
    }

    public BigDecimal getSubTotal() {
        return getTotalItem();
    }
}
