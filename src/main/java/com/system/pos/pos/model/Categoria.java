package com.system.pos.pos.model;

public enum Categoria {
    ELETRONICOS(1, "Eletrônicos", new SubCategoria[] { SubCategoria.CELULARES, SubCategoria.NOTEBOOKS }),
    ALIMENTOS(2, "Alimentos", new SubCategoria[] { SubCategoria.FRUTAS, SubCategoria.VEGETAIS }),
    ROUPAS(3, "Roupas", new SubCategoria[] { SubCategoria.CAMISAS, SubCategoria.CALCAS }),
    MOVEIS(4, "Móveis", new SubCategoria[] { SubCategoria.SOFAS, SubCategoria.MESAS });

    private final int id;
    private final String descricao;
    private final SubCategoria[] subCategorias;

    Categoria(int id, String descricao, SubCategoria[] subCategorias) {
        this.id = id;
        this.descricao = descricao;
        this.subCategorias = subCategorias;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public SubCategoria[] getSubCategorias() { return subCategorias; }

    public static Categoria fromId(int id) {
        for (Categoria categoria : values()) {
            if (categoria.id == id) return categoria;
        }
        return null;
    }
}