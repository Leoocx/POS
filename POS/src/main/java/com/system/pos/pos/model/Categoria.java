package com.system.pos.pos.model;
public enum Categoria {
    ELETRONICOS,
    ROUPAS,
    LIVROS,
    MOVEIS,
    BEBIDAS;

    @Override
    public String toString() {
        switch (this) {
            case ELETRONICOS: return "Eletrônicos";
            case ROUPAS: return "Roupas";
            case LIVROS: return "Livros";
            case MOVEIS: return "Moveis";
            case BEBIDAS: return "Bebidas";
            default: return super.toString();
        }
    }
}


