package com.system.pos.pos.model;

import java.time.LocalDate;

public class Caixa {
    private int id;
    private LocalDate dataAbertura;
    private LocalDate dataFechamento;
    private String vendedor;
    private double caixaInicial;
    private double caixaFinal;
    private String status;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public double getCaixaInicial() {
        return caixaInicial;
    }

    public void setCaixaInicial(double caixaInicial) {
        this.caixaInicial = caixaInicial;
    }

    public double getCaixaFinal() {
        return caixaFinal;
    }

    public void setCaixaFinal(double caixaFinal) {
        this.caixaFinal = caixaFinal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}