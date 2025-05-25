package com.system.pos.pos.service;

import com.system.pos.pos.database.CaixaDAO;
import com.system.pos.pos.model.Caixa;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CaixaService {

    private final CaixaDAO caixaDAO = new CaixaDAO();

    public boolean abrirCaixa() {
        Caixa caixa = new Caixa();
        caixa.setDataAbertura(LocalDate.now());
        caixa.setVendedor("Vendedor Padrão"); // Substituir por usuário logado
        caixa.setCaixaInicial(100.00); // Valor padrão ou pegar do último fechamento
        caixa.setStatus("ABERTO");

        return caixaDAO.abrirCaixa(caixa);
    }

    public boolean isCaixaAberto() {
        return caixaDAO.verificarCaixaAberto();
    }

    public String getNomeVendedor() {
        return "Vendedor Padrão"; // Substituir por usuário logado
    }

    public String getDataAtual() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getNumeroCaixa() {
        return "001"; // Número do terminal/caixa
    }

    public String getCaixaInicialFormatado() {
        return String.format("%.2f", caixaDAO.getCaixaInicial());
    }

    public String getCaixaAtualFormatado() {
        return String.format("%.2f", caixaDAO.getCaixaAtual());
    }

    public String getTotalSaidasFormatado() {
        return String.format("%.2f", caixaDAO.getTotalSaidas());
    }

    public String getTotalDinheiroFormatado() {
        return String.format("%.2f", caixaDAO.getTotalPorTipo("DINHEIRO"));
    }

    public String getTotalCartaoFormatado() {
        return String.format("%.2f", caixaDAO.getTotalPorTipo("CARTAO"));
    }

    public String getTotalChequeFormatado() {
        return String.format("%.2f", caixaDAO.getTotalPorTipo("CHEQUE"));
    }

    public String getLucroTotalFormatado() {
        return String.format("%.2f", caixaDAO.getLucroTotal());
    }
}