package com.system.pos.pos.model;

import javafx.beans.property.*;

public class Produto {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final IntegerProperty quantidade = new SimpleIntegerProperty();
    private final DoubleProperty preco = new SimpleDoubleProperty();
    private final StringProperty status = new SimpleStringProperty();
    private StringProperty codigoBarras= new SimpleStringProperty();

    public Produto() {
    }
    public Produto(String nome, int quantidade, double preco, String status) {
        this.nome.set(nome);
        this.quantidade.set(quantidade);
        this.preco.set(preco);
        this.status.set(status);
    }
    public Produto(int id, String nome, int quantidade, double preco, String status) {
        this.id.set(id);
        this.nome.set(nome);
        this.quantidade.set(quantidade);
        this.preco.set(preco);
        this.status.set(status);
    }

    // Métodos de acesso para as properties
    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
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

    public DoubleProperty precoProperty() {
        return preco;
    }

    public double getPreco() {
        return preco.get();
    }

    public void setPreco(double preco) {
        this.preco.set(preco);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getCodigoBarras() {
        return codigoBarras.get();
    }

    public StringProperty codigoBarrasProperty() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras.set(codigoBarras);
    }
}