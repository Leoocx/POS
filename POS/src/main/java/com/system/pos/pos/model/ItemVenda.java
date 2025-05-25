package com.system.pos.pos.model;

import javafx.beans.property.*;
import java.math.BigDecimal;

public class ItemVenda {
    private final IntegerProperty idProduto = new SimpleIntegerProperty();
    private final StringProperty nomeProduto = new SimpleStringProperty();
    private final IntegerProperty quantidade = new SimpleIntegerProperty();
    private final DoubleProperty precoUnitario = new SimpleDoubleProperty();

    // Getters e Setters para as properties
    public IntegerProperty idProdutoProperty() {
        return idProduto;
    }

    public int getIdProduto() {
        return idProduto.get();
    }

    public void setIdProduto(int idProduto) {
        this.idProduto.set(idProduto);
    }

    public StringProperty nomeProdutoProperty() {
        return nomeProduto;
    }

    public String getNomeProduto() {
        return nomeProduto.get();
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto.set(nomeProduto);
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

    public DoubleProperty precoUnitarioProperty() {
        return precoUnitario;
    }

    public double getPrecoUnitario() {
        return precoUnitario.get();
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario.set(precoUnitario);
    }

    public ReadOnlyObjectProperty<BigDecimal> totalItemProperty() {
        return new SimpleObjectProperty<>(
                BigDecimal.valueOf(precoUnitario.get()).multiply(BigDecimal.valueOf(quantidade.get()))
        );
    }

    public BigDecimal getTotalItem() {
        return BigDecimal.valueOf(precoUnitario.get()).multiply(BigDecimal.valueOf(quantidade.get()));
    }

    public String getDummy() {
        return ""; // Retorna string vazia para a coluna de remoção
    }
}