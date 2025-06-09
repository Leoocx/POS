package com.system.pos.pos.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Venda {
    private final ObservableList<ItemVenda> itensVenda = FXCollections.observableArrayList();
    private final double totalVenda;

    public Venda() {
        this.totalVenda = 0.0;
    }

    public void adicionarItem(ItemVenda item) {
        itensVenda.add(item);
    }

    public ObservableList<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public double calcularTotal() {
        double total = 0.0;
        for (ItemVenda item : itensVenda) {
            total += item.getTotalItem();
        }
        return total;
    }

    public double getTotalVenda() {
        return calcularTotal();
    }
}
