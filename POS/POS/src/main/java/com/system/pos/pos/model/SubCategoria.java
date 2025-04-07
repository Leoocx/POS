package com.system.pos.pos.model;
public enum SubCategoria {
    NOTEBOOKS(Categoria.ELETRONICOS),
    SMARTPHONES(Categoria.ELETRONICOS),
    CAMISAS(Categoria.ROUPAS),
    CALÇADOS(Categoria.ROUPAS),
    LIVROS_TECNICOS(Categoria.LIVROS),
    LIVROS_FICCAO(Categoria.LIVROS),
    MOVEIS_ESCRITORIO(Categoria.MOVEIS),
    MOVEIS_SALA(Categoria.MOVEIS),
    BEBIDAS_ALCOOLICAS(Categoria.BEBIDAS),
    BEBIDAS_NAO_ALCOOLICAS(Categoria.BEBIDAS);

    private final Categoria categoria;

    SubCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}
