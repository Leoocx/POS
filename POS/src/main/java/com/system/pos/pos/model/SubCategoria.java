package com.system.pos.pos.model;

public enum SubCategoria {
    CELULARES(1, "Celulares"),
    NOTEBOOKS(2, "Notebooks"),
    FRUTAS(3, "Frutas"),
    VEGETAIS(4, "Vegetais"),
    CAMISAS(5, "Camisas"),
    CALCAS(6, "Calças"),
    SOFAS(7, "Sofás"),
    MESAS(8, "Mesas");

    private final int id;
    private final String descricao;

    SubCategoria(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }

    public static SubCategoria fromId(int id) {
        for (SubCategoria sub : values()) {
            if (sub.id == id) return sub;
        }
        return null;
    }
}