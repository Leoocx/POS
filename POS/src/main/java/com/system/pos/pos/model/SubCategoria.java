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

    @Override
    public String toString() {
        switch (this) {
            case NOTEBOOKS: return "Notebooks";
            case SMARTPHONES: return "Smartphones";
            case CAMISAS: return "Camisas";
            case CALÇADOS: return "Calçados";
            case LIVROS_TECNICOS: return "Livros Técnicos";
            case LIVROS_FICCAO: return "Livros de Ficção";
            case MOVEIS_ESCRITORIO: return "Móveis para Escritório";
            case MOVEIS_SALA: return "Móveis para Sala";
            case BEBIDAS_ALCOOLICAS: return "Bebidas Alcoólicas";
            case BEBIDAS_NAO_ALCOOLICAS: return "Bebidas Não Alcoólicas";
            default: return super.toString();
        }
    }

}
