package com.system.pos.pos.view;

import com.system.pos.pos.model.Funcionario;

import java.time.LocalDateTime;

public class AberturaFechamentoCaixa {
    private int id;
    private LocalDateTime dataHoraAbertura;  //Guarda a data e hora exata em que o caixa foi aberto.
    private LocalDateTime dataHoraFechamento; //Guarda a data e hora em que o caixa foi fechado.
    private double valorAbertura;  //É o valor que o operador colocou no caixa no começo do dia para troco.
    private double valorFechamento; //É o valor que ficou no caixa no final do dia (após vendas, entradas e saídas).
    private double totalEntradas;  //É o valor total de dinheiro que entrou no caixa ao longo do período (vendas, depósitos, etc.).
    private double totalSaidas;  //Quantidade de vezes que o caixa foi fechado
    private Funcionario funcionarioResponsavel; //Funcionário responsável
    private boolean caixaFechado;  // retorna se o caixa está fechado

    public AberturaFechamentoCaixa(){

    }


    public LocalDateTime getDataHoraAbertura() {
        return dataHoraAbertura;
    }

    public void setDataHoraAbertura(LocalDateTime dataHoraAbertura) {
        this.dataHoraAbertura = dataHoraAbertura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValorFechamento() {
        return valorFechamento;
    }

    public void setValorFechamento(double valorFechamento) {
        this.valorFechamento = valorFechamento;
    }

    public boolean isCaixaFechado() {
        return caixaFechado;
    }

    public void setCaixaFechado(boolean caixaFechado) {
        this.caixaFechado = caixaFechado;
    }

    public Funcionario getFuncionarioResponsavel() {
        return funcionarioResponsavel;
    }

    public void setFuncionarioResponsavel(Funcionario funcionarioResponsavel) {
        this.funcionarioResponsavel = funcionarioResponsavel;
    }

    public double getTotalSaidas() {
        return totalSaidas;
    }

    public void setTotalSaidas(double totalSaidas) {
        this.totalSaidas = totalSaidas;
    }

    public double getTotalEntradas() {
        return totalEntradas;
    }

    public void setTotalEntradas(double totalEntradas) {
        this.totalEntradas = totalEntradas;
    }

    public double getValorAbertura() {
        return valorAbertura;
    }

    public void setValorAbertura(double valorAbertura) {
        this.valorAbertura = valorAbertura;
    }

    public LocalDateTime getDataHoraFechamento() {
        return dataHoraFechamento;
    }

    public void setDataHoraFechamento(LocalDateTime dataHoraFechamento) {
        this.dataHoraFechamento = dataHoraFechamento;
    }
}
