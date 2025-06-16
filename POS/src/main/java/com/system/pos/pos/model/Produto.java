package com.system.pos.pos.model;

import javafx.beans.property.*;
import java.math.BigDecimal;

public class Produto {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final IntegerProperty quantidade = new SimpleIntegerProperty();
    private final ObjectProperty<BigDecimal> preco = new SimpleObjectProperty<>();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty codigoBarras = new SimpleStringProperty();
    private final ObjectProperty<Categoria> categoria = new SimpleObjectProperty<>();
    private final ObjectProperty<SubCategoria> subCategoria = new SimpleObjectProperty<>();

    public Produto() {
    }

    public Produto(String nome, int quantidade, BigDecimal preco, String status, String codigoBarras, Categoria categoria, SubCategoria subCategoria) {
        this.nome.set(nome);
        this.quantidade.set(quantidade);
        this.preco.set(preco);
        this.status.set(status);
        this.codigoBarras.set(codigoBarras);
        this.categoria.set(categoria);
        this.subCategoria.set(subCategoria);
    }

    public Produto(int id, String nome, int quantidade, BigDecimal preco, String status, String codigoBarras, Categoria categoria, SubCategoria subCategoria) {
        this.id.set(id);
        this.nome.set(nome);
        this.quantidade.set(quantidade);
        this.preco.set(preco);
        this.status.set(status);
        this.codigoBarras.set(codigoBarras);
        this.categoria.set(categoria);
        this.subCategoria.set(subCategoria);
    }

    // Getters and Setters
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public String getNome() { return nome.get(); }
    public StringProperty nomeProperty() { return nome; }
    public void setNome(String nome) { this.nome.set(nome); }

    public int getQuantidade() { return quantidade.get(); }
    public IntegerProperty quantidadeProperty() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade.set(quantidade); }

    public BigDecimal getPreco() { return preco.get(); }
    public ObjectProperty<BigDecimal> precoProperty() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco.set(preco); }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public void setStatus(String status) { this.status.set(status); }

    public String getCodigoBarras() { return codigoBarras.get(); }
    public StringProperty codigoBarrasProperty() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras.set(codigoBarras); }

    public Categoria getCategoria() { return categoria.get(); }
    public ObjectProperty<Categoria> categoriaProperty() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria.set(categoria); }

    public SubCategoria getSubCategoria() { return subCategoria.get(); }
    public ObjectProperty<SubCategoria> subCategoriaProperty() { return subCategoria; }
    public void setSubCategoria(SubCategoria subCategoria) { this.subCategoria.set(subCategoria); }
}
